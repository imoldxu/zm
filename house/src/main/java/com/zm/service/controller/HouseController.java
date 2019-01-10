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

import com.zm.service.entity.Condition;
import com.zm.service.entity.House;
import com.zm.service.entity.SimpleHouse;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.HouseService;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("房源接口")
public class HouseController{

	@Autowired
	HouseService houseService;
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 发布房源
	* @description 发布房源
	* @method post
	* @url /house-service/issueHouse
	* @param houseStr 必选 string house对象的JSON字符串{"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"toilet":1,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","tagList":[],"longitude":104.050779,"latitude":30.551915,"imglist":"[/1.png,/2.png]","des":"我很喜欢，安静"}  
	* @return {"code":1,"data":{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"toilet":1,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"createtime":1542077910000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915,"imglist":"[/1.png,/2.png]","des":"我很喜欢，安静"},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/issueHouse", method = RequestMethod.POST)
	@ApiOperation(value = "发布房源", notes = "发布房源")
	public Response issueHouse(
			@ApiParam(name = "houseStr", value = "house的json传") @RequestParam(name = "houseStr") String houseStr,
			HttpServletRequest request, HttpServletResponse response) {
		House house = null;
		
		try{
			house = JSONUtils.getObjectByJson(houseStr, House.class);
		}catch (Exception e) {
			System.out.println("houseStr:=====>"+houseStr);
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误:"+houseStr);
		}
		
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			house = houseService.issue(uid, house);
			
			return Response.OK(house);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 搜索房源
	* @description 搜索房源
	* @method get
	* @url /house-service/searchHouse
	* @param conditionStr 必选 string condition对象的JSON字符串{"type":1,"min":150000,"max":400000,"tip":40000,"room":2,"place":"世豪广场","longitude":104.05072,"latitude":30.552,"indate":1542211200000,"tags":"[1,4,8]"}  
	* @param pageIndex 必选 int 页码  
	* @param pageSize 必选 int 每页最大数量
	* @return {"code":1,"data":[],"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/searchHouse", method = RequestMethod.GET)
	@ApiOperation(value = "查询房源", notes = "查询房源")
	public Response searchHouse(
			@ApiParam(name = "conditionStr", value = "查询条件") @RequestParam(name = "conditionStr") String conditionStr,
			@ApiParam(name = "pageIndex", value = "页码") @RequestParam(name = "pageIndex") int pageIndex,
			@ApiParam(name = "pageSize", value = "每页数量") @RequestParam(name = "pageSize") int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Condition condition;
		try{
			condition = JSONUtils.getObjectByJson(conditionStr, Condition.class);
		}catch (Exception e) {
			System.out.println("conditionStr:=====>"+conditionStr);
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
		}
		
		try{
			List<SimpleHouse> list = houseService.search(condition, pageIndex, pageSize);
			return Response.OK(list);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 获取房源详情
	* @description 获取房源详情
	* @method get
	* @url /house-service/getDetail
	* @param houseid 必选 Long 房源id  
	* @return {"code":1,"data":{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"createtime":1542077910000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDetail", method = RequestMethod.GET)
	@ApiOperation(value = "获取房源详情", notes = "获取房源详情")
	public Response getDetail(
			@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") Long houseid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			
			House house = houseService.getDetail(houseid);
			
			return Response.OK(house);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 获取我的房源
	* @description 获取我的房源
	* @method get
	* @url /house-service/getMyHouse
	* @return {"code":1,"data":{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"createtime":1542077910000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyHouse", method = RequestMethod.GET)
	@ApiOperation(value = "查询我的房源", notes = "查询我的房源")
	public Response getMyHouse(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			House house = houseService.getMyHouse(uid);
			
			return Response.OK(house);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 修改房源信息
	* @description 修改房源信息
	* @method post
	* @url /house-service/modifyHouse
	* @param houseStr 必选 string house对象的JSON字符串{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":"2018-11-13","createtime":1542077910000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915}  
	* @return {"code":1,"data":{"id":1,"uid":1,"name":"保利星语天涯国际","building":2,"unit":1,"num":"703","issue":2,"parlor":2,"room":3,"area":88,"type":1,"amount":300000,"tip":20000,"tenantlimit":"[\"限女生\",\"不养宠物\"]","outdate":1542211200000,"createtime":1542077910000,"reservedate":"非工作日","reservetime":"晚上7点以后","installation":"[xiyiji:0,kongtiao:1]","state":1,"tagList":null,"longitude":104.050779,"latitude":30.551915},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modifyHouse", method = RequestMethod.POST)
	@ApiOperation(value = "修改房源信息", notes = "修改房源信息")
	public Response modifyHouse(
			@ApiParam(name = "houseStr", value = "房源json串") @RequestParam(name = "houseStr") String houseStr,
			HttpServletRequest request, HttpServletResponse response) {
		House house = null;
		try{
			house = JSONUtils.getObjectByJson(houseStr, House.class);
			if(house.getId() == null){
				return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
			}
		}catch (Exception e) {
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
		}
		
		try{
			int uid = SessionUtil.getUserId(request);
			
			house = houseService.modify(uid, house);
			
			return Response.OK(house);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/房源相关
	* @title 删除房源
	* @description 删除房源
	* @method post
	* @url /house-service/delHouse
	* @param houseid 必选 Long 房源id  
	* @return {"code":1,"data":null,"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/delHouse", method = RequestMethod.POST)
	@ApiOperation(value = "删除房源", notes = "删除房源")
	public Response delHouse(
			@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") Long houseid,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			houseService.del(uid, houseid);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}

}
