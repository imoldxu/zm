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
	* @return {"code":1,"data":{"id":10,"houseid":2,"house":{"id":2,"name":"世豪瑞丽","imglist":"[/1.png]","createtime":1545725854000,"area":72,"parlor":1,"room":2,"toilet":1,"amount":250000,"distance":0,"uid":2,"phone":"18866661234","tags":"[\"有车位\",\"精装修\",\"有阳台\"]","state":1},"order":null,"time":1544500800000,"iuid":2,"ruid":1,"ruser":null,"istate":1,"rstate":2,"state":1,"iscomment":0,"createtime":1549945987274},"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
	* @return {"code":1,"data":{"id":10,"houseid":2,"house":null,"order":null,"time":1544500800000,"iuid":2,"ruid":1,"ruser":null,"istate":1,"rstate":2,"state":1,"iscomment":0,"createtime":1549945987274},"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
	* @return {"code":1,"data":{"id":10,"houseid":2,"house":null,"order":null,"time":1544500800000,"iuid":2,"ruid":1,"ruser":null,"istate":1,"rstate":2,"state":1,"iscomment":0,"createtime":1549945987274},"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
	* @return {"code":1,"data":{"id":10,"houseid":2,"house":null,"order":null,"time":1544500800000,"iuid":2,"ruid":1,"ruser":null,"istate":1,"rstate":2,"state":1,"iscomment":0,"createtime":1549945987274},"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
	* @return {"code":1,"data":[{"id":8,"houseid":1,"house":null,"order":null,"time":1548043200000,"iuid":1,"ruid":4,"ruser":{"id":4,"nick":"LeiTest","avatar":"https://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEJsFqmtePTkj622fAJfsibBvCBUc1O73zLZUsl4f7Ptib963lLVykGJADmDbn4MGb1LBnMQibux0YO7w/64","name":null,"sex":null,"idcardtype":null,"idcardnum":null,"birthday":null,"phone":"13888888888","password":"1234","wxunionid":null,"createtime":7581000,"lastlogintime":1546999584000,"age":0,"subscribe":0,"sessionID":null},"istate":1,"rstate":2,"state":2,"iscomment":0,"createtime":1548050200000}],"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
	* @return {"code":1,"data":[{"id":10,"houseid":2,"house":{"id":2,"name":"世豪瑞丽","imglist":"[/1.png]","createtime":1545725854000,"area":72,"parlor":1,"room":2,"toilet":1,"amount":250000,"distance":0,"uid":2,"phone":"18866661234","tags":"[\"有车位\",\"精装修\",\"有阳台\"]","state":1},"order":null,"time":1544500800000,"iuid":2,"ruid":1,"ruser":null,"istate":1,"rstate":2,"state":2,"iscomment":0,"createtime":1549945987000}],"msg":"成功"}
	* @return_param id long 预约id，后续的reserveid
	* @return_param houseid long 房源id
	* @return_param house jsonObject 房源基础信息，可参见搜索房源接口返回的信息说明
	* @return_param order jsonObject 订单信息，若此接口不为null，则表示需要走支付流程
	* @return_param time int 预约时间，距离1970年1月1日的时间
	* @return_param iuid int 发布者用户id
	* @return_param ruid int 求租者用户id
	* @return_param ruser jsonObject 求租者用户信息，可参看用户登录返回的信息说明
	* @return_param istate int 发布者预约确认状态，双方确认表示预约已确认，1为未确认，2为已确认
	* @return_param rstate int 求租者预约确认状态，双方确认表示预约已确认，1位未确认，2为已确认
	* @return_param state int 预约的状态，1为新建状态，还未付款，2为有效状态，3为已看房，4为预约已取消
	* @return_param iscomment int 预约是否评论
	* @return_param createtime int 预约创建的时间，距离1970年1月1日的时间
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
