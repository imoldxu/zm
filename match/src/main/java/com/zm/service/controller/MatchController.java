package com.zm.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.entity.Condition;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.ConditionService;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("匹配接口")
public class MatchController{

	@Autowired
	ConditionService conditionService;
	
	/**
	* showdoc
	* @catalog 接口文档/求租相关
	* @title 发布租房请求
	* @description 发布租房请求
	* @method post
	* @url /match-service/commitMyCondition
	* @param conditionStr 必选 string condition的JSON字符串{"type":1,"min":150000,"max":200000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":"2018-11-14","tags":"[1,4,8]"}  
	* @return {"code":1,"data":{"id":1,"uid":1,"type":1,"min":150000,"max":200000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":1542124800000,"createtime":1542096209000,"isnotify":0,"tags":"[1,4,8]","state":1},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/commitMyCondition", method = RequestMethod.POST)
	@ApiOperation(value = "发布租房请求", notes = "发布租房请求")
	public Response commitMyCondition(
			@ApiParam(name = "conditionStr", value = "conditon的json传") @RequestParam(name = "conditionStr") String conditionStr,
			HttpServletRequest request, HttpServletResponse response) {
		
		Condition condition = null;
		try{
			condition = JSONUtils.getObjectByJson(conditionStr, Condition.class);
		}catch (Exception e) {
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
		}
		
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			condition = conditionService.create(uid, condition);
			
			return Response.OK(condition);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/求租相关
	* @title 取消租房请求
	* @description 取消租房请求
	* @method post
	* @url /match-service/delMyCondition
	* @param conditionid 必选 Long 条件id  
	* @return {"code":1,"data":null,"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/delMyCondition", method = RequestMethod.POST)
	@ApiOperation(value = "取消租房请求", notes = "取消租房请求")
	public Response delMyCondition(
			@ApiParam(name = "conditionid", value = "条件编号") @RequestParam(name = "conditionid") Long conditionid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			conditionService.del(uid, conditionid);
			
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
	* @catalog 接口文档/求租相关
	* @title 获取我的租房要求
	* @description 获取我的租房要求
	* @method get
	* @url /match-service/getMyCondition
	* @return {"code":1,"data":{"id":1,"uid":1,"type":1,"min":150000,"max":200000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":1542124800000,"createtime":1542096209000,"isnotify":0,"tags":"[1,4,8]","state":1},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyCondition", method = RequestMethod.GET)
	@ApiOperation(value = "获取我的租房要求", notes = "获取我的租房要求")
	public Response getMyCondition(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			Condition condition = conditionService.getConditionByUid(uid);
			
			return Response.OK(condition);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/求租相关
	* @title 修改我的租房要求
	* @description 修改我的租房要求
	* @method post
	* @url /match-service/modifyCondition
	* @param conditionStr 必选 string condition对象JSON字符串{"id":1,"uid":1,"type":1,"min":150000,"max":200000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":1542124800000,"createtime":1542096209000,"isnotify":0,"tags":"[1,4,8]","state":1}
	* @return {"code":1,"data":{"id":1,"uid":1,"type":1,"min":150000,"max":200000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":1542124800000,"createtime":1542096209000,"isnotify":0,"tags":"[1,4,8]","state":1},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modifyCondition", method = RequestMethod.POST)
	@ApiOperation(value = "修改我的租房要求", notes = "修改我的租房要求")
	public Response modifyCondition(
			@ApiParam(name = "conditionStr", value = "查询条件") @RequestParam(name = "conditionStr") String conditionStr,
			HttpServletRequest request, HttpServletResponse response) {
		Condition condition = null;
		try{
			condition = JSONUtils.getObjectByJson(conditionStr, Condition.class);
			if(condition.getId() ==  null){
				return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
			}
		}catch (Exception e) {
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
		}
		
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			condition = conditionService.modifyCondition(uid, condition);
			
			return Response.OK(condition);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
}
