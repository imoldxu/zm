package com.zm.service.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.entity.Order;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.OrderService;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.MyWxPayUtil;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("支付订单接口")
public class PaymentController{

	@Autowired
	OrderService orderService;
	
	/**
	* showdoc
	* @catalog 接口文档/支付相关
	* @title 获取我的交易
	* @description 获取我的交易
	* @method get
	* @url /payment-service/getMyOrders
	* @param pageIndex 必选 int 页码1-n  
	* @param pageSize 必选 int 每页最大数量  
	* @return {"code":1,"data":[{"id":1,"sn":"201902180001321312","uid":1,"code":1,"payway":"WX_XCX","paysn":"12321312312","amount":3000,"createtime":1550467481000,"invalidtime":1550467785000,"payovertime":1550467492000,"info":"2321","state":3}],"msg":"成功"}
	* @return_param id string 订单id
	* @return_param sn string 订单序列号
	* @return_param uid int   订单归属用户id
	* @return_param code int  订单交易码，1为定金交易，2为预约交易
	* @return_param payway string 支付渠道
	* @return_param paysn string 支付渠道订单号
	* @return_param amount int 订单支付金额
	* @return_param info string 订单交易关联的交易id
	* @return_param createtime int 订单的创建时间戳
	* @return_param invalidtime int 订单的失效时间戳
	* @return_param payovertime int 订单的支付成功时间戳
	* @return_param state int 订单的状态，1为新建、2为支付中、3为支付成功，4为已失效
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyOrders", method = RequestMethod.GET)
	@ApiOperation(value = "获取我的交易", notes = "获取我的交易")
	public Response getMyOrders(@ApiParam(name="pageIndex", value="页码") @RequestParam(name="pageIndex") int pageIndex,
			@ApiParam(name="pageSize", value="每页最大数量") @RequestParam(name="pageSize") int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			Integer uid = SessionUtil.getUserId(request);
			List<Order> ret = orderService.getMyOrder(uid, pageIndex, pageSize);
			
			return Response.OK(ret);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
		
	}
	
	/**
	* showdoc
	* @catalog 接口文档/支付相关
	* @title 获取支付凭证
	* @description 获取支付凭证
	* @method post
	* @url /payment-service/getCharge
	* @param openid 必选 Long wx用户id 
	* @param orderid 必选 Long 订单编号  
	* @param payWay 必选 string 支付渠道:"WX_XCX"  
	* @return {"code":1,"data":{"paySign":"fadff123123","signType":"1","prepay_id":"20190212321323131113300","nonceStr":"123123ff","timeStamp":"20190212131231"},"msg":"成功"}
	* @return_param paySign string 支付签名
	* @return_param signType string 签名类型
	* @return_param prepay_id string 支付渠道订单号
	* @return_param nonceStr string 随机数
	* @return_param timeStamp string 时间戳
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getCharge", method = RequestMethod.POST)
	@ApiOperation(value = "获取支付凭证", notes = "获取支付凭证")
	public Response getCharge(
			@ApiParam(name="openid", value="wx用户id") @RequestParam(name="openid") String openid,
			@ApiParam(name="orderid", value="订单编号") @RequestParam(name="orderid") Long orderid,
			@ApiParam(name="payWay", value="支付渠道") @RequestParam(name="payWay") String payWay,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			Integer uid = SessionUtil.getUserId(request);
			String ip = request.getRemoteHost();
			Object ret = orderService.getCharge(uid, openid, orderid, payWay, ip);
			return Response.OK(ret);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
		
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	@ApiOperation(value = "创建支付通知", notes = "创建支付通知")
	public String notify(HttpServletRequest request, HttpServletResponse response) {
		String resXml="";
	    try{
	        //
	        InputStream is = request.getInputStream();
	        //将InputStream转换成String
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        resXml=sb.toString();
	        Map<String, String> respMap = MyWxPayUtil.payBack(resXml);
	        
	        //TODO
	        String ordersn = respMap.get("out_trade_no");
	        String time_end = respMap.get("time_end");
	        String attach = respMap.get("attach");
	        String transaction_id =  respMap.get("transaction_id");
	        String settlement_total_fee = respMap.get("settlement_total_fee");
	        orderService.payOver(ordersn, settlement_total_fee, attach, time_end);
	        
	        String result = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
	        return result;
	    }catch (Exception e){
	        String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
	        return result;
	    }
	}
}
