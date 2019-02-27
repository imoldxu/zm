package com.zm.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Account;
import com.zm.service.entity.House;
import com.zm.service.entity.Message;
import com.zm.service.entity.Order;
import com.zm.service.entity.Reserve;
import com.zm.service.entity.Transaction;
import com.zm.service.feign.client.HouseClient;
import com.zm.service.feign.client.MessageClient;
import com.zm.service.feign.client.OrderClient;
import com.zm.service.feign.client.ReserveClient;
import com.zm.service.feign.client.UserClient;
import com.zm.service.mapper.TransactionMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class TransactionService {

	@Autowired
	TransactionMapper transMapper;
	@Autowired
	MessageClient msgClient;
	@Autowired
	HouseClient houseClient;
	@Autowired
	ReserveClient reserveClient;
	@Autowired
	OrderClient orderClient;
	@Autowired
	UserClient userClient;
	
	@Transactional
	public void cancel(Integer uid, Long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(uid != trans.getIuid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		if(trans.getState() != Transaction.STATE_R_CONFIRM){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "交易已被确认无法取消");
		}
		
		trans.setState(Transaction.STATE_CANCEL);
		
		transMapper.updateByPrimaryKey(trans);
		//通知房源可以进行再确认
		Response resp = houseClient.modifyState(trans.getHouseid(), House.STATE_LOCKED);
		resp.fetchOKData();
		//通知对方
		msgClient.push(trans.getRuid(), Message.TYPE_TRASACTION_RENDER_NOTICE, "发布者取消了你的锁定");
	}

	public void confirm(Integer uid, Long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(uid != trans.getIuid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		if(trans.getState() != Transaction.STATE_R_CONFIRM){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "交易已被处理");
		}
		trans.setState(Transaction.STATE_I_CONFIRM);
		
		transMapper.updateByPrimaryKey(trans);
	}

	@Transactional
	public void complete(Integer uid, Long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(uid != trans.getRuid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		if(trans.getState() != Transaction.STATE_I_CONFIRM){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "交易状态异常，无法完成交易");
		}
		
		trans.setState(Transaction.STATE_COMPLETE);
		transMapper.updateByPrimaryKey(trans);
		
		Response resp = houseClient.modifyState(trans.getHouseid(), House.STATE_COMPLETE);
		resp.fetchOKData();
		//TODO:账户划转
	}

	public Transaction getRenderTransaction(Integer uid) {
		Example ex = new Example(Transaction.class);

		List<Integer> validstatelist = new ArrayList<Integer>();
		validstatelist.add(Transaction.STATE_NEW);
		validstatelist.add(Transaction.STATE_R_CONFIRM);
		validstatelist.add(Transaction.STATE_I_CONFIRM);
		ex.createCriteria().andEqualTo("ruid", uid).andIn("state", validstatelist);
		
		Transaction t = transMapper.selectOneByExample(ex);
		return t;
	}

	public Transaction getIssuerTransaction(Integer uid) {
		Example ex = new Example(Transaction.class);
		
		List<Integer> validstatelist = new ArrayList<Integer>();
		validstatelist.add(Transaction.STATE_NEW);
		validstatelist.add(Transaction.STATE_R_CONFIRM);
		validstatelist.add(Transaction.STATE_I_CONFIRM);
		ex.createCriteria().andEqualTo("iuid", uid).andIn("state", validstatelist);
		Transaction t = transMapper.selectOneByExample(ex);
		return t;
	}

	public void applytips(Integer uid, Long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(uid != trans.getIuid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		if(trans.getState() != Transaction.STATE_I_CONFIRM){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "交易状态异常，无法申诉");
		}
	
		//TODO 申诉需要有一个单独的处理，暂缓
	}

	@Transactional
	public Transaction create(int ruid, long reserveid) {
		
		Response resp = reserveClient.getReserveById(reserveid);
		ObjectMapper mapper = new ObjectMapper();
		Reserve reserve = mapper.convertValue(resp.fetchOKData(), Reserve.class);
		 
		if(ruid != reserve.getRuid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		
		Long houseid = reserve.getHouseid();
		resp = houseClient.getHouseById(houseid);
		mapper = new ObjectMapper();
		House house = mapper.convertValue(resp.fetchOKData(), House.class);		
		
		Example ex = new Example(Transaction.class);
		
		List<Integer> validstatelist = new ArrayList<Integer>();
		validstatelist.add(Transaction.STATE_NEW);
		validstatelist.add(Transaction.STATE_R_CONFIRM);
		validstatelist.add(Transaction.STATE_I_CONFIRM);
		validstatelist.add(Transaction.STATE_COMPLETE);
		ex.createCriteria().andEqualTo("houseid", houseid).andIn("state", validstatelist);
		Transaction t = transMapper.selectOneByExample(ex);
		if(t!=null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "房源已被锁定，你无法再锁定");
		}
		
		t = getRenderTransaction(reserve.getRuid());
		if(t!=null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "你只能同时锁定一套房源");
		}
		
		Transaction trans = new Transaction();
		trans.setCreatetime(new Date());
		trans.setHouseid(reserve.getHouseid());
		trans.setIuid(reserve.getIuid());
		trans.setRuid(reserve.getRuid());
		int amount = (int) (house.getTip()*0.2);//交易金额为小费的20%
		trans.setAmount(amount);
		trans.setState(Transaction.STATE_NEW);
		
		transMapper.insertUseGeneratedKeys(trans);
	
		//锁定房源状态
		resp = houseClient.modifyState(houseid, House.STATE_LOCKED);
		resp.fetchOKData();
		
		//
		resp = userClient.getAccount(ruid);
		mapper = new ObjectMapper();
		Account raccount = mapper.convertValue(resp.fetchOKData(), Account.class);
		Order order = null;
		if(raccount.getCash() >= amount){
			rConfirm(trans.getId());
		}else{
			//下单支付(进支付剩余部分)
			resp = orderClient.create(ruid, Order.CODE_TRANSACTION, house.getAmount()-raccount.getCash(), trans.getId().toString());
			mapper = new ObjectMapper();
			order = mapper.convertValue(resp.fetchOKData(), Order.class);		
			trans.setOrder(order);
		}
		//发送房源锁定消息
		msgClient.push(reserve.getIuid(), Message.TYPE_TRASACTION_ISSUER_NOTICE, "你发布的房源已有人预定");			
		
		return trans;
	}

	@Transactional
	public void invalid(long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(trans.getState() != Transaction.STATE_NEW){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "状态异常");
		}
		
		trans.setState(Transaction.STATE_INVALID);
		
		transMapper.updateByPrimaryKey(trans);
		
		//解锁房源
		Response resp = houseClient.modifyState(trans.getHouseid(), House.STATE_VALID);
		resp.fetchOKData();
	}

	@Transactional
	public void rConfirm(long tid) {
		Transaction trans = transMapper.selectByPrimaryKey(tid);
		if(trans==null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(trans.getState() != Transaction.STATE_NEW){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "状态异常");
		}
		
		trans.setState(Transaction.STATE_R_CONFIRM);
		
		transMapper.updateByPrimaryKey(trans);
		
		//锁定订金
		userClient.lockCash(trans.getRuid(), trans.getAmount(), "锁定订金");
	}

	public Boolean check(int uid, int type) {
		List<Integer> validstatelist = new ArrayList<Integer>();
		validstatelist.add(Transaction.STATE_NEW);
		validstatelist.add(Transaction.STATE_R_CONFIRM);
		validstatelist.add(Transaction.STATE_I_CONFIRM);
		
		if(type==1) {
			Example ex = new Example(Transaction.class);
			ex.createCriteria().andEqualTo("ruid", uid).andIn("state", validstatelist);
			Transaction t = transMapper.selectOneByExample(ex);
			if(t==null) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}else{
			Example ex = new Example(Transaction.class);
			ex.createCriteria().andEqualTo("iuid", uid).andIn("state", validstatelist);
			Transaction t = transMapper.selectOneByExample(ex);
			if(t==null) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
	}
		
}
