package com.zm.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.entity.Transaction;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.TransactionService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("交易接口")
public class TransactionController{
	
	@Autowired
	TransactionService transService;
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 创建交易
	* @description 由求租者发起调用
	* @method post
	* @url /trans-service/create
	* @param reserveid 必选 string 预约id  
	* @return {"code":1,"data":{null},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ApiOperation(value = "创建交易", notes = "创建交易")
	public Response create(
			@ApiParam(name = "reserveid", value = "预约id") @RequestParam(name = "reserveid") long reserveid,
			HttpServletRequest request,
			HttpServletResponse response) {
		try{
			int uid = SessionUtil.getUserId(request);
			Transaction trans = transService.create(uid, reserveid);
			return Response.OK(trans);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 取消交易
	* @description 由发布者发起调用
	* @method post
	* @url /trans-service/cancel
	* @param tid 必选 string 交易id  
	* @return {"code":1,"data":{null},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ApiOperation(value = "取消交易", notes = "取消交易")
	public Response cancel(
			@ApiParam(name = "tid", value = "交易id") @RequestParam(name = "tid") Long tid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			transService.cancel(uid, tid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 确认交易
	* @description 由发布者调用
	* @method post
	* @url /trans-service/confirm
	* @param tid 必选 Long 交易id  
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ApiOperation(value = "确认交易", notes = "确认交易")
	public Response confirm(
			@ApiParam(name = "tid", value = "交易id") @RequestParam(name = "tid") Long tid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			transService.confirm(uid, tid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 完成交易
	* @description 由求租者点击完成
	* @method post
	* @url /trans-service/complete
	* @param tid 必选 Long 交易id  
	* @return {"code":1,"data":{null},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@ApiOperation(value = "完成交易", notes = "完成交易")
	public Response complete(
			@ApiParam(name = "tid", value = "交易id") @RequestParam(name = "tid") Long tid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			transService.complete(uid, tid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 申诉订金
	* @description 由发布者点击完成
	* @method post
	* @url /trans-service/applytips
	* @param tid 必选 Long 交易id  
	* @return {"code":1,"data":{null},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/applytips", method = RequestMethod.POST)
	@ApiOperation(value = "申诉交易", notes = "申诉交易")
	public Response applytips(
			@ApiParam(name = "tid", value = "交易id") @RequestParam(name = "tid") Long tid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			transService.applytips(uid, tid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 获取发布者的交易
	* @description 发布者我的房源已锁定中调用
	* @method get
	* @url /trans-service/getIssuerTransaction
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getIssuerTransaction", method = RequestMethod.GET)
	@ApiOperation(value = "获取发布者交易", notes = "获取发布者交易")
	public Response getIssuerTransaction(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			Transaction t = transService.getIssuerTransaction(uid);
			
			return Response.OK(t);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/交易相关
	* @title 获取求租者的交易
	* @description 在我的意向已锁定的交易中调用
	* @method get
	* @url /transaction-service/getRenderTransaction
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getRenderTransaction", method = RequestMethod.GET)
	@ApiOperation(value = "获取求租者的交易", notes = "获取求租者的交易")
	public Response getRenderTransaction(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			Transaction t = transService.getRenderTransaction(uid);
			
			return Response.OK(t);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
}
