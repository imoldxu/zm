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
import com.zm.service.entity.Reserve;
import com.zm.service.feign.client.ReserveClient;
import com.zm.service.service.ReserveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@Api("预约接口")
@RequestMapping("/internal")
public class ReserveClientImpl implements ReserveClient {

	@Autowired
	ReserveService reserveService;
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getReserveById", method = RequestMethod.GET)
	@ApiOperation(value = "获取预约", notes = "获取预约")
	public Response getReserveById(@ApiParam(name="reserveid", value="预约的id") @RequestParam(name="reserveid") Long reserveid) {
		try{
			Reserve reserve = reserveService.getReserveById(reserveid);
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			return Response.SystemError();
		}
	}
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/payOver", method = RequestMethod.POST)
	@ApiOperation(value = "支付成功的处理", notes = "支付成功的处理")
	public Response payOver(@ApiParam(name="reserveid", value="预约的id") @RequestParam(name="reserveid") Long reserveid) {
		try{
			Reserve reserve = reserveService.payOver(reserveid);
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ApiOperation(value = "支付不成功的处理", notes = "支付成功的处理")
	public Response cancel(@ApiParam(name="reserveid", value="预约的id") @RequestParam(name="reserveid")Long reserveid) {
		try{
			Reserve reserve = reserveService.cancel(reserveid);
			return Response.OK(reserve);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	@ApiOperation(value = "关闭所有预约", notes = "")
	public Response close(@ApiParam(name="uid", value="预约者uid") @RequestParam(name="uid")Integer uid,
			@ApiParam(name="type", value="1为求租者，2为出租者") @RequestParam(name="type")int type) {
		try{
			reserveService.close(uid, type);
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			return Response.SystemError();
		}
	}
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/checkReserve", method = RequestMethod.GET)
	@ApiOperation(value = "检查房源是否有预约，返回true表示有预约，false表示没有预约", notes = "修改我的房源前的检查，是否有预约")
	public Response checkReserve(@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") Long houseid) {
		try{
			
			Boolean ret = reserveService.check(houseid);
			
			return Response.OK(ret);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
}
