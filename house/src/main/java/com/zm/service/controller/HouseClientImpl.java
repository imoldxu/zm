package com.zm.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.House;
import com.zm.service.entity.SimpleHouse;
import com.zm.service.feign.client.HouseClient;
import com.zm.service.service.HouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/internal")
@Api("房源内部接口")
public class HouseClientImpl implements HouseClient{

	@Autowired
	HouseService houseService;
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getHouseById", method = RequestMethod.GET)
	@ApiOperation(value = "获取房源", notes = "获取房源")
	public Response getHouseById(@ApiParam(name="houseid", value="房源id") @RequestParam(name="houseid") Long houseid) {
		
		try{
			House house = houseService.getHouseById(houseid);
			return Response.OK(house);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getSimpleHouseById", method = RequestMethod.GET)
	@ApiOperation(value = "获取房源", notes = "获取房源")
	public Response getSimpleHouseById(@ApiParam(name="houseid", value="房源id") @RequestParam(name="houseid") Long houseid) {
		
		try{
			SimpleHouse house = houseService.getSimpleHouseById(houseid);
			return Response.OK(house);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	@Override
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modifyState", method = RequestMethod.POST)
	@ApiOperation(value = "修改状态", notes = "修改状态")
	public Response modifyState(@ApiParam(name="houseid", value="房源id") @RequestParam(name="houseid") Long houseid,
			@ApiParam(name="state", value="房源状态") @RequestParam(name="state") int state) {
		
		try{
			houseService.modifyState(houseid, state);
			return Response.OK(null);
		}catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return Response.SystemError();
		}
	}

}
