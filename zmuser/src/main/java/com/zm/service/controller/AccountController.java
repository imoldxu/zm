package com.zm.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Account;
import com.zm.service.service.AccountService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("账户接口")
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService accountService;
	
	/**
	* showdoc
	* @catalog 接口文档/账户相关
	* @title 获取我的账户的余额
	* @description 获取我的账户的余额
	* @method get
	* @url /user-service/account/getMyAccount
	* @return {"code":1,"data":{"id":1,"uid":1,"coin":998,"cash":96400,"lockedcash":3600},"msg":"成功"}
	* @return_param id int 账户id
	* @return_param uid int 归属用户id
	* @return_param coin int 看房币，单位是个
	* @return_param cash int 账户的余额，单位是分
	* @return_param lockedcash int 账户锁定的余额，因交易被锁定，单位是分
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyAccount", method = RequestMethod.GET)
	@ApiOperation(value = "获取我的账户", notes = "获取我的账户")
	public Response getMyAccount(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			Account a = accountService.getMyAccount(uid);
			
			return Response.OK(a);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	

	/**
	* showdoc
	* @catalog 接口文档/账户相关
	* @title 提取中介费
	* @description 提取中介费到银行
	* @method post
	* @url /user-service/account/withdrawCash
	* @param amount 必选 string 提取的金额 ，单位是分
	* @return {"code":1,"data":null,"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/withdrawCash", method = RequestMethod.POST)
	@ApiOperation(value = "提取cash", notes = "提取cash")
	public Response withdrawCash(
			@ApiParam(name="amount", value="提取的金额") @RequestParam(name="amount") int amount,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			accountService.withdrawCash(uid, amount);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
}
