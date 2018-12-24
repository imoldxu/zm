package com.zm.service.controller;

import java.util.List;

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
import com.zm.service.entity.Message;
import com.zm.service.service.MessageService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("消息接口")
public class MessageController {

	@Autowired
	MessageService msgService;
	
	/**
	* showdoc
	* @catalog 接口文档/消息相关
	* @title 获取我的消息
	* @description 获取我的消息
	* @method get
	* @url /msg-service/getMyMessages
	* @param pageIndex 必选 int 页码  
	* @param pageSize 必选 int 每页最大数量  
	* @return {"code":1,"data":[{},{}],"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyMessages", method = RequestMethod.GET)
	@ApiOperation(value = "获取我的消息", notes = "获取我的消息")
	public Response getMyMessages(
			@ApiParam(name = "pageIndex", value = "页码") @RequestParam(name = "pageIndex") int pageIndex,
			@ApiParam(name = "pageSize", value = "每页最大数量") @RequestParam(name = "pageSize") int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			List<Message> msgList = msgService.getMyMessage(uid, pageIndex, pageSize);
			
			return Response.OK(msgList);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
}
