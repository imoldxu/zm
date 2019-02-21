package com.zm.service.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.entity.Account;
import com.zm.service.entity.House;
import com.zm.service.entity.Message;
import com.zm.service.entity.Order;
import com.zm.service.entity.Reserve;
import com.zm.service.entity.SimpleHouse;
import com.zm.service.entity.User;
import com.zm.service.feign.client.HouseClient;
import com.zm.service.feign.client.MessageClient;
import com.zm.service.feign.client.OrderClient;
import com.zm.service.feign.client.UserClient;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.mapper.ReserveMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ReserveService {

	@Autowired
	ReserveMapper reserveMapper;
	@Autowired
	HouseClient houseClient;
	@Autowired
	MessageClient msgClient;
	@Autowired
	UserClient userClient;
	@Autowired
	OrderClient orderClient;
	
	@Transactional
	public Reserve create(int uid, Long houseid, Long datetime) {
		
		Response resp = houseClient.getSimpleHouseById(houseid);
		SimpleHouse house = null;
		
		ObjectMapper mapper = new ObjectMapper();
		house = mapper.convertValue(resp.fetchOKData(), SimpleHouse.class);
		//检查预约创建者是否与发布者一致
		if(uid==house.getUid()){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "无法向自己预约");
		}
		//检查是否已经预约过了
		Reserve reserve = getReserve(houseid, uid);
		if(reserve!=null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "你已经预约过了，无需重复预约");
		}
		
		reserve = new Reserve();
		reserve.setHouse(house);
		reserve.setHouseid(houseid);
		reserve.setIscomment(0);
		reserve.setIstate(Reserve.USER_STATE_UNCONFIRM);
		reserve.setIuid(house.getUid());
		reserve.setRuid(uid);
		reserve.setRstate(Reserve.USER_STATE_CONFIRM);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = new Date(datetime);
//		try {
//			d = sdf.parse(datetime);
//		} catch (ParseException e) {
//			throw new HandleException(ErrorCode.ARG_ERROR, "日期格式错误");
//		}
		reserve.setTime(d);
		reserve.setState(Reserve.STATE_NEW);
		reserve.setCreatetime(new Date());
		reserveMapper.insertUseGeneratedKeys(reserve);
		
		resp = userClient.getAccount(uid);
		mapper = new ObjectMapper();
		Account account = mapper.convertValue(resp.fetchOKData(), Account.class);
		
		if(account.getCoin()>=2){
			payOver(reserve.getId());
		}else{
			resp = orderClient.create(uid, Order.CODE_RESERVE, (2-account.getCoin())*100, reserve.getId().toString());
			mapper = new ObjectMapper();
			Order order = mapper.convertValue(resp.fetchOKData(), Order.class);
			reserve.setOrder(order);
		}
		
		return reserve;
	}

	public Reserve getReserve(Long houseid, int uid){
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("houseid", houseid).andEqualTo("ruid", uid);
		Reserve r = reserveMapper.selectOneByExample(ex);
		return r;
	}
	
	public Reserve confirm(Long reserveid, int uid) {
		Reserve reserve = reserveMapper.selectByPrimaryKey(reserveid);
		if(reserve == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(reserve.getState() > Reserve.STATE_VALID){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "预约已完成，不可再修改");
		}
		
		if(reserve.getIuid() == uid){
			reserve.setIstate(Reserve.USER_STATE_CONFIRM);
			//TODO:通知对方
			Integer ruid = reserve.getRuid();
			msgClient.push(ruid, Message.TYPE_RESERVE_NOTICE, "预约时间已被确认");
		}else if(reserve.getRuid() == uid){
			reserve.setRstate(Reserve.USER_STATE_CONFIRM);
			//TODO:通知对方
			Integer iuid = reserve.getIuid();
			msgClient.push(iuid, Message.TYPE_RESERVE_NOTICE, "预约时间已被确认");
		}else{
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权操作此数据");
		}
		reserveMapper.updateByPrimaryKey(reserve);
		return reserve;
	}

	public Reserve modify(Long reserveid, int uid, Long datetime) {
		Reserve reserve = reserveMapper.selectByPrimaryKey(reserveid);
		if(reserve == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(reserve.getState() > Reserve.STATE_VALID){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "预约已完成，不可再修改");
		}
		
		if(reserve.getIuid() == uid){
			reserve.setIstate(Reserve.USER_STATE_CONFIRM);
			reserve.setRstate(Reserve.USER_STATE_UNCONFIRM);
			//TODO:通知对方
			msgClient.push(reserve.getRuid(), Message.TYPE_RESERVE_NOTICE, "预约时间已被修改");
			
		}else if(reserve.getRuid() == uid){
			reserve.setRstate(Reserve.USER_STATE_CONFIRM);
			reserve.setIstate(Reserve.USER_STATE_UNCONFIRM);
			//TODO:通知对方
			msgClient.push(reserve.getIuid(), Message.TYPE_RESERVE_NOTICE, "预约时间已被修改");
		}else{
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权操作此数据");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = new Date(datetime);
//		try {
//			d = sdf.parse(datetime);
//		} catch (ParseException e) {
//			throw new HandleException(ErrorCode.ARG_ERROR, "日期格式错误");
//		}
		reserve.setTime(d);
		reserveMapper.updateByPrimaryKey(reserve);
		return reserve;
	}

	public Reserve complete(int uid, Long houseid) {
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("ruid", uid).andEqualTo("houseid", houseid);
		Reserve reserve = reserveMapper.selectOneByExample(ex);
		if(reserve == null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "请先预约再看房");
		}
		if(reserve.getState() > Reserve.STATE_VALID){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "预约已完成，不重复完成");
		}
		if(reserve.getRuid() == uid){
			reserve.setState(Reserve.STATE_COMPLETE);//完成了看房	
		}else{
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权操作此数据");
		}
		reserveMapper.updateByPrimaryKey(reserve);
		return reserve;
	}

	public List<Reserve> getMyIssuerReserve(int uid, int type) {
		List<Reserve> ret;
		if(type == 1){
			ret = getIUnConfirmReserve(uid);
		}else if(type == 2){
			ret = getIConfirmReserve(uid);
		}else if(type == 3){
			ret = getICompleteReserve(uid);
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		for(Reserve reserve : ret){
			ObjectMapper om = new ObjectMapper();
			User user = om.convertValue(userClient.getUser(reserve.getRuid()).fetchOKData(), User.class);
			reserve.setRuser(user);
		}
		return ret;
	}

	public List<Reserve> getMyRenderReserve(int uid, int type) {
		List<Reserve> ret;
		if(type == 1){
			ret = getRUnConfirmReserve(uid);
		}else if(type == 2){
			ret = getRConfirmReserve(uid);
		}else if(type == 3){
			ret = getRCompleteReserve(uid);
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		for(Reserve reserve : ret){
			ObjectMapper om = new ObjectMapper();
			SimpleHouse house = om.convertValue(houseClient.getSimpleHouseById(reserve.getHouseid()).fetchOKData(), SimpleHouse.class);
			reserve.setHouse(house);
		}
		return ret;
	}
	
	private List<Reserve> getRUnConfirmReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.and().andEqualTo("ruid", uid).andEqualTo("state", Reserve.STATE_VALID);
		
		Criteria stateCondition = ex.or().andNotEqualTo("istate", Reserve.USER_STATE_CONFIRM).orNotEqualTo("rstate", Reserve.USER_STATE_CONFIRM);
		ex.and(stateCondition);
		
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}
	
	private List<Reserve> getIUnConfirmReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.and().andEqualTo("iuid", uid).andEqualTo("state", Reserve.STATE_VALID);
		
		Criteria stateCondition = ex.or().andNotEqualTo("istate", Reserve.USER_STATE_CONFIRM).orNotEqualTo("rstate", Reserve.USER_STATE_CONFIRM);
		ex.and(stateCondition);
		
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}
	
	private List<Reserve> getRCompleteReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("ruid", uid).andEqualTo("state", Reserve.STATE_COMPLETE);
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}
	
	private List<Reserve> getICompleteReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("iuid", uid).andEqualTo("state", Reserve.STATE_COMPLETE);
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}
	
	private List<Reserve> getRConfirmReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("ruid", uid).andEqualTo("state", Reserve.STATE_VALID).andEqualTo("istate", Reserve.USER_STATE_CONFIRM).andEqualTo("rstate", Reserve.USER_STATE_CONFIRM);
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}
	
	private List<Reserve> getIConfirmReserve(int uid){
		Example ex = new Example(Reserve.class);
		ex.createCriteria().andEqualTo("iuid", uid).andEqualTo("state", Reserve.STATE_VALID).andEqualTo("istate", Reserve.USER_STATE_CONFIRM).andEqualTo("rstate", Reserve.USER_STATE_CONFIRM);
		List<Reserve> ret = reserveMapper.selectByExample(ex);
		return ret;
	}

	public Reserve getReserveById(Long reserveid) {
		return reserveMapper.selectByPrimaryKey(reserveid);
	}

	public Reserve payOver(Long reserveid) {
		Reserve reserve = getReserveById(reserveid);
		if(reserve.getState() != Reserve.STATE_NEW){
			return reserve;
		}else{
			
			userClient.reduceCoin(reserve.getRuid(), 2, "扣除看房币");
			
			reserve.setState(Reserve.STATE_VALID);
			reserveMapper.updateByPrimaryKey(reserve);
			return reserve;
		}
	}

	public Reserve cancel(Long reserveid) {
		//TODO 取消预约无需做任务和事
		return null;
	}

	public void close(Integer uid, int type) {
		if(1==type) {
			Example ex = new Example(Reserve.class);
			ex.createCriteria().andEqualTo("ruid", uid).andEqualTo("state", Reserve.STATE_VALID);
			Reserve r = new Reserve();
			r.setState(Reserve.STATE_CANCEL);
			reserveMapper.updateByExampleSelective(r, ex);
		}else {
			Example ex = new Example(Reserve.class);
			ex.createCriteria().andEqualTo("iuid", uid).andEqualTo("state", Reserve.STATE_VALID);
			List<Reserve> rlist = reserveMapper.selectByExample(ex);
			for(Reserve r:rlist) {
				r.setState(Reserve.STATE_CANCEL);
				userClient.addCoin(r.getRuid(), 2, "发布者取消预约退还币");
				reserveMapper.updateByPrimaryKey(r);
			}
		}
	}
	
}
