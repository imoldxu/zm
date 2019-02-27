package com.zm.service.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.WXPayCharge;
import com.zm.service.entity.Order;
import com.zm.service.entity.Transaction;
import com.zm.service.feign.client.ReserveClient;
import com.zm.service.feign.client.TransactionClient;
import com.zm.service.feign.client.UserClient;
import com.zm.service.mapper.OrderMapper;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.MyWxPayUtil;
import com.zm.service.utils.RedissonUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class OrderService {

	@Autowired
	OrderMapper orderMapper;
	@Autowired
	TransactionClient transClient;
	@Autowired
	UserClient userClient;
	@Autowired
	ReserveClient reserveClient;
	@Autowired
	RedissonUtil redissonUtil;

	public List<Order> getMyOrder(Integer uid, int pageIndex, int pageSize) {
		Example ex = new Example(Order.class);
		ex.createCriteria().andEqualTo("uid", uid);
		ex.setOrderByClause("id desc");
		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
		List<Order> orderList = orderMapper.selectByExampleAndRowBounds(ex, rowBounds);
		return orderList;
	}

	public Order create(Integer uid, Integer code, int amount, String info) {
		
		Order order = new Order();
		order.setUid(uid);
		order.setAmount(amount);
		order.setCode(code);
		order.setInfo(info);
		order.setCreatetime(new Date());
		order.setState(Order.STATE_NEW);
		orderMapper.insertUseGeneratedKeys(order);
		
		return order;
	}

	public Object getCharge(Integer uid, String openid, Long orderid, String payWay, String ip) {
		Order order = orderMapper.selectByPrimaryKey(orderid);
		if(order == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "order不存在");
		}
		
		if(payWay.equals("WX_XCX")){
			order.setPayway(payWay);
			WXPayCharge charge = MyWxPayUtil.getPayCharge(openid, "租盟-订金", "", order.getSn(), order.getAmount(), ip);
			order.setPaysn(charge.getPrepay_id());
			order.setState(Order.STATE_PAYING);
			orderMapper.updateByPrimaryKey(order);
			return charge;
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "不支持的支付方式");
		}
	}

	/**
	 * 生成按日期的订单号
	 * @param uid
	 * @return
	 */
	private String generateOrderSN(Integer uid){
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
        String sn = sdf.format(date);
        
        String uidStr = String.format("%60d", uid);
        
        Random r = new Random(date.getTime()+uid);
        int number = r.nextInt(999999);
		String randomStr = String.format("%06d", number);
		
		sn = sn+uidStr+randomStr;
        
        return sn;
	}

	public void payOver(String ordersn, String settlement_total_fee, String attach, String time_end) {
		String info;
		redissonUtil.tryLock(ordersn, TimeUnit.MILLISECONDS, 300, 300);
		Example ex = new Example(Order.class);
		ex.createCriteria().andEqualTo("sn", ordersn);
		Order order = orderMapper.selectOneByExample(ex);
		if(order.getState() == order.STATE_COMPELTE){
			redissonUtil.unlock(ordersn);
			return;//状态不符不处理
		}else{
			if(order.getAmount() != Integer.parseInt(settlement_total_fee)){
				redissonUtil.unlock(ordersn);
				return;//金额不符不处理
			}
			order.setState(Order.STATE_COMPELTE);
			DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date payovertime = null;
			try {
				payovertime = sdf.parse(time_end);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			order.setPayovertime(payovertime);
			info = order.getInfo();
			orderMapper.updateByPrimaryKey(order);
		}
		
		redissonUtil.unlock(ordersn);
		
		//支付成功后的处理
		int transCode = order.getCode();
		if(transCode == Order.CODE_TRANSACTION){
			//给账户充值
			userClient.addCash(order.getUid(), order.getAmount(), "充值订金");
			//通知交易完成
			transClient.payOver(Long.valueOf(info));
		} else if(transCode == Order.CODE_RESERVE){
			//给账户充值
			userClient.addCoin(order.getUid(), order.getAmount()/100, "充值看房币");
			//通知完成看房预约
			reserveClient.payOver(Long.valueOf(info));
		}
	}

	public void handleInvalidOrder() {
		Example ex = new Example(Order.class);
		ex.createCriteria().andLessThan("state", Order.STATE_COMPELTE).andLessThanOrEqualTo("invalidtime", new Date());
		
		List<Order> orders = orderMapper.selectByExample(ex);
		for(Order order : orders){
			order.setState(Order.STATE_INVALID);
			orderMapper.updateByPrimaryKey(order);
			
			//支付成功后的处理
			String info = order.getInfo();
			int transCode = order.getCode();
			if(transCode == Order.CODE_TRANSACTION){
				//通知交易取消
				transClient.cancel(Long.valueOf(info));
			} else if(transCode == Order.CODE_RESERVE){
				//通知完成看房取消
				reserveClient.cancel(Long.valueOf(info));
			}
		}
	
	}

	
}
