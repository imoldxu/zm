package com.zm.service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.feign.client.TransactionClient;
import com.zm.service.service.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/internal")
@Api("内部交易接口")
public class TransClientImpl implements TransactionClient{

	@Autowired
	TransactionService transService;
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/payOver", method = RequestMethod.POST)
	@ApiOperation(value = "支付失效，取消交易", notes = "取消交易")
	public Response payOver(
			@ApiParam(name = "transid", value = "交易id") @RequestParam(name = "transid") long transid) {
		
		try{		
			transService.rConfirm(transid);
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ApiOperation(value = "支付失效，取消交易", notes = "取消交易")
	public Response cancel(
			@ApiParam(name = "transid", value = "交易id") @RequestParam(name = "transid") long transid) {
		
		try{
			
			transService.invalid(transid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ApiOperation(value = "检查是否还有存在的交易", notes = "取消交易")
	public Response check(@ApiParam(name = "uid", value = "用户id") @RequestParam(name = "uid")int uid,
			@ApiParam(name = "type", value = "交易类型，求租1，出租2") @RequestParam(name = "type")int type) {
		try{	
			Boolean ret = transService.check(uid, type);
			return Response.OK(ret);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
}
