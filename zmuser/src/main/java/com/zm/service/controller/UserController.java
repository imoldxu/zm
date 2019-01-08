package com.zm.service.controller;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.entity.User;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.UserService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("用户接口")
public class UserController{

	@Autowired
	UserService userService;
	
	/**
	* showdoc
	* @catalog 接口文档/用户相关
	* @title 微信小程序登录
	* @description 微信小程序登录
	* @method post
	* @url /user-service/loginByWxMiniprogram
	* @param wxCode 必选 string 微信授权码  
	* @param rawData 必选 string 非敏感数据  
	* @param signature 必选 string 签名
	* @param encryptedData 必选 string 加密数据
	* @param iv 必选 string 偏移向量 
	* @return {"code":1,"data":{"id":1,"nick":"小明","avatar":"http:/www.163.com/1.png","name":"徐明","sex":null,"idcardtype":1,"idcardnum":"511102198202250032","birthday":null,"phone":"13880605659","password": "123456","wxunionid":"1231231231","createtime":31366000,"lastlogintime":1542012171000,"age":0,"subscribe":0},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/loginByWxMiniprogram", method = RequestMethod.POST)
	@ApiOperation(value = "微信小程序登录", notes = "微信小程序登录")
	public Response loginByMiniProgram(
			@ApiParam(name = "wxCode", value = "微信授权码") @RequestParam(name = "wxCode") String wxCode,
			@ApiParam(name = "rawData", value = "非敏感数据") @RequestParam(name = "rawData") String rawData,
			@ApiParam(name = "signature", value = "签名") @RequestParam(name = "signature") String signature,
			@ApiParam(name = "encryptedData", value = "加密数据") @RequestParam(name = "encryptedData") String encryptedData,
			@ApiParam(name = "iv", value = "偏移向量") @RequestParam(name = "iv") String iv,
			HttpServletRequest request, HttpServletResponse response) {
	
		if (StringUtils.isEmpty(wxCode) || StringUtils.isEmpty(encryptedData) || StringUtils.isEmpty(iv)) {
			return Response.NormalError("参数不允许为空串");
		} else {
			try{
				User user = userService.loginByWxMiniprogram(wxCode, rawData, signature, encryptedData, iv);
				String sessionID = request.getSession().getId();
				sessionID = Base64.getEncoder().encodeToString(sessionID.getBytes());
				user.setSessionID(sessionID);
				SessionUtil.setUserId(request, user.getId());
				return Response.OK(user);
			}catch (HandleException e) {
				return Response.Error(e.getErrorCode(), e.getMessage());
			}catch (Exception e){
				e.printStackTrace();
				return Response.SystemError();
			}
			
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation(value = "账号登录", notes = "账号登录")
	public Response login(
			@ApiParam(name = "phone", value = "电话号码") @RequestParam(name = "phone") String phone,
			@ApiParam(name = "password", value = "密码") @RequestParam(name = "password") String password,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			User user = userService.login(phone, password);
			String sessionID = request.getSession().getId();
			sessionID = Base64.getEncoder().encodeToString(sessionID.getBytes());
			user.setSessionID(sessionID);
			
			SessionUtil.setUserId(request, user.getId());
			return Response.OK(user);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/用户相关
	* @title 获取短信验证码
	* @description 实名认证获取验证码
	* @method get
	* @url /user-service/getVerificationCode
	* @param phone 必选 string 手机号  
	* @return {"code":1,"data":{null},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getVerificationCode", method = RequestMethod.GET)
	@ApiOperation(value = "获取验证码", notes = "获取验证码")
	public Response getVerificationCode(
			@ApiParam(name = "phone", value = "电话号码") @RequestParam(name = "phone") String phone,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			userService.getVerificationCode(phone);
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
	* @catalog 接口文档/用户相关
	* @title 实名认证
	* @description 实名认证接口
	* @method post
	* @url /user-service/verify
	* @param name 必选 string 姓名  
	* @param phone 必选 string 电话号码  
	* @param idcardtype 必选 int 证件类型:1、身份证
	* @param idcardnum 必选 string 证件号码
	* @param code 必选 string 验证码 
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	@ApiOperation(value = "实名认证", notes = "实名认证")
	public Response verify(
			@ApiParam(name = "name", value = "姓名") @RequestParam(name = "name") String name,
			@ApiParam(name = "phone", value = "电话") @RequestParam(name = "phone") String phone,
			@ApiParam(name = "idcardtype", value = "证件类型") @RequestParam(name = "idcardtype") int idcardtype,
			@ApiParam(name = "idcardnum", value = "证件号") @RequestParam(name = "idcardnum") String idcardnum,
			@ApiParam(name = "code", value = "认证码") @RequestParam(name = "code") String code,
			HttpServletRequest request, HttpServletResponse response) {
	
		try{
			int uid = SessionUtil.getUserId(request);
			
			userService.verifyIDCard(uid, code, name, phone, idcardtype, idcardnum);
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}

}
