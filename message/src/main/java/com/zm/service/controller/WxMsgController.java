package com.zm.service.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.qq.weixin.mp.aes.AesException;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.entity.Message;
import com.zm.service.entity.WxFormId;
import com.zm.service.mapper.WxFormIdMapper;
import com.zm.service.service.MessageService;
import com.zm.service.utils.DateUtils;
import com.zm.service.utils.SessionUtil;
import com.zm.service.utils.WxMiniProgramUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("消息接口")
public class WxMsgController {

	@Autowired
	MessageService msgService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/wxtoken", method = RequestMethod.GET)
	@ApiOperation(value = "微信模板消息验证接口", notes = "通用接口")
	public String getMyMessages(@RequestParam(name="signature") String signature,
			@RequestParam(name="timestamp") String timestamp,
			@RequestParam(name="nonce") String nonce,
			@RequestParam(name="echostr") String echostr,
			HttpServletRequest request, HttpServletResponse response) {
					
			String ret = "";
			try {
				ret = WxMiniProgramUtil.checkSignature(signature, timestamp, nonce, echostr);
			} catch (AesException e) {
				e.printStackTrace();
			}
		
			System.out.println(ret);
			return ret;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/uploadFormId", method = RequestMethod.POST)
	@ApiOperation(value = "上传formid", notes = "通用接口")
	public void uploadFormId(@ApiParam(name = "wxFormIds", value = "微信form_id的JSON数组，为消息推送准备") @RequestParam(name = "wxFormIds") JSONArray wxFormIds,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer uid = SessionUtil.getUserId(request);
			msgService.uploadFormId(uid, wxFormIds);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
