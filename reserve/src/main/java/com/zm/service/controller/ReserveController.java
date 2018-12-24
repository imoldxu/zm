package com.zm.service.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Reserve;
import com.zm.service.service.ReserveService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("预约接口")
public class ReserveController{

	@Autowired
	ReserveService reserveService;
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 创建预约
	* @description 创建预约
	* @method post
	* @url /reserve-service/create
	* @param houseid 必选 Long 房源id
	* @param datetime 必选 string 预约时间,如："2018-12-11 12:00" 
	* @return {"code":1,"data":{"id":3,"houseid":1,"house":{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"area":88,"toilet":1,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"createtime":1543916152000,"reservedate":"非工作日","reservetime":"晚上7点以后","imglist":"[/1.png,/2.png]","des":"我很喜欢，安静","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915},"time":1544605200000,"iuid":1,"ruid":2,"istate":1,"rstate":null,"state":1,"iscomment":0,"createtime":1544431500865},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ApiOperation(value = "创建预约", notes = "创建预约")
	public Response create(
			@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") Long houseid,
			@ApiParam(name = "datetime", value = "预约时间") @RequestParam(name = "datetime") String datetime,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			int uid = SessionUtil.getUserId(request);
			
			Reserve r = reserveService.create(uid, houseid, datetime);
			
			return Response.OK(r);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 确认预约
	* @description 确认预约
	* @method post
	* @url /reserve-service/confirm
	* @param reserveid 必选 string 预约id  
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ApiOperation(value = "确认预约", notes = "确认预约")
	public Response confirm(
			@ApiParam(name = "reserveid", value = "预约id") @RequestParam(name = "reserveid") Long reserveid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			int uid = SessionUtil.getUserId(request);
			
			Reserve reserve = reserveService.confirm(reserveid, uid);
			
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 修改预约
	* @description 修改预约
	* @method post
	* @url /reserve-service/modify
	* @param reserveid 必选 string 预约id  
	* @param datetime 必选 string 预约时间  
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ApiOperation(value = "修改预约", notes = "修改预约")
	public Response modify(
			@ApiParam(name = "reserveid", value = "预约id") @RequestParam(name = "reserveid") Long reserveid,
			@ApiParam(name = "datetime", value = "预约时间") @RequestParam(name = "datetime") String datetime,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			int uid = SessionUtil.getUserId(request);
			
			Reserve reserve = reserveService.modify(reserveid, uid, datetime);
			
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 完成预约
	* @description 扫对方房源码
	* @method post
	* @url /reserve-service/complete
	* @param qrcode 必选 string 房源码  
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@ApiOperation(value = "扫码完成预约", notes = "扫码完成预约")
	public Response complete(
			@ApiParam(name = "qrcode", value = "扫码的信息") @RequestParam(name = "qrcode") Long qrcode,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			Reserve reserve = reserveService.complete(uid, qrcode);
			
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 获取发布者的预约
	* @description 获取发布者的预约
	* @method get
	* @url /reserve-service/getMyIssuerReserve
	* @param state 必选 int 预约状态:1.待确认,2.已确认,3.已看房
	* @return {"code":1,"data":[{},{}],"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyIssuerReserve", method = RequestMethod.GET)
	@ApiOperation(value = "获取发布者的预约", notes = "获取发布者的预约")
	public Response getMyIssuerReserve(
			@ApiParam(name = "state", value = "预约状态，1.待确认,2.已确认,3.已看房") @RequestParam(name = "state") int state,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			List<Reserve> reserveList = reserveService.getMyIssuerReserve(uid, state);
			
			return Response.OK(reserveList);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/预约管理
	* @title 获取求租者预约
	* @description 获取求租者预约
	* @method post
	* @url /reserve-service/getMyRenderReserve
	* @param state 必选 string 预约状态:1.待确认,2.已确认,3.已看房  
	* @return {"code":1,"data":[{},{}],"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyRenderReserve", method = RequestMethod.GET)
	@ApiOperation(value = "获取求租者预约", notes = "获取求租者预约")
	public Response getMyRenderReserve(
			@ApiParam(name = "state", value = "预约状态，1.待确认,2.已确认,3.已看房") @RequestParam(name = "state") int state,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			List<Reserve> reserveList = reserveService.getMyRenderReserve(uid, state);
			
			return Response.OK(reserveList);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}

}
