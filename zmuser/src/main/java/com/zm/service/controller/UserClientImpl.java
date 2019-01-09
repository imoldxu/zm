package com.zm.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Account;
import com.zm.service.entity.User;
import com.zm.service.feign.client.UserClient;
import com.zm.service.service.AccountService;
import com.zm.service.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/internal")
@Api("用户账户操作内部接口")
public class UserClientImpl implements UserClient{

	@Autowired
	UserService userService;
	@Autowired
	AccountService accountService;
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户信息", notes = "获取用户信息")
	public Response getUser(@ApiParam(name="uid",value="用户id") @RequestParam(name="uid") Integer uid) {
		try{
			User user = userService.getUserById(uid);
			return Response.OK(user);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getAccount", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户账户信息", notes = "获取用户账户信息")
	public Response getAccount(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid") Integer uid) {
		try{
			Account account = accountService.getMyAccount(uid);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/addCoin", method = RequestMethod.POST)
	@ApiOperation(value = "添加看房币", notes = "添加看房币")
	public Response addCoin(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		try{
			Account account = accountService.addCoin(uid, amount, msg);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/reduceCoin", method = RequestMethod.POST)
	@ApiOperation(value = "减少看房币", notes = "减少看房币")
	public Response reduceCoin(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		try{
			Account account = accountService.reduceCoin(uid, amount, msg);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/addCash", method = RequestMethod.POST)
	@ApiOperation(value = "添加订金", notes = "添加订金")
	public Response addCash(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		try{
			Account account = accountService.addCash(uid, amount, msg);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/lockCash", method = RequestMethod.POST)
	@ApiOperation(value = "锁定订金", notes = "锁定订金")
	public Response lockCash(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		
		try{
			Account account = accountService.lockCash(uid, amount, msg);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/unlockCash", method = RequestMethod.POST)
	@ApiOperation(value = "锁定订金", notes = "锁定订金")
	public Response unlockCash(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		try{
			Account account = accountService.unlockCash(uid, amount, msg);
			return Response.OK(account);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/payCash", method = RequestMethod.POST)
	@ApiOperation(value = "划转订金", notes = "划转订金")
	public Response payCash(@ApiParam(name="uid", value="用户id") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="touid", value="目标用户") @RequestParam(name="touid")Integer touid,
			@ApiParam(name="amount", value="金额") @RequestParam(name="amount")int amount,
			@ApiParam(name="msg", value="内容") @RequestParam(name="msg")String msg) {
		try{
			accountService.payCash(uid, touid, amount, msg);
			return Response.OK(null);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage()); 
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}



}
