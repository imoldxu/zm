package com.zm.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Order;
import com.zm.service.feign.client.OrderClient;
import com.zm.service.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("支付内部接口")
@RequestMapping("/internal")
public class OrderClientImpl implements OrderClient{

	@Autowired
	OrderService orderService;
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ApiOperation(value = "创建订单,在创建交易时同步创建", notes = "创建订单")
	public Response create(
			@ApiParam(name="uid", value="订单的持有者") @RequestParam(name="uid") Integer uid,
			@ApiParam(name="code", value="交易码") @RequestParam(name="code") Integer code,
			@ApiParam(name="amount", value="交易金额") @RequestParam(name="amount") int amount,
			@ApiParam(name="info", value="交易信息") @RequestParam(name="info") String info
			) {
		try{
			
			Order order = orderService.create(uid, code, amount, info);
			
			return Response.OK(order);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
}
