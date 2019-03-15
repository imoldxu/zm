package com.zm.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.feign.client.MessageClient;
import com.zm.service.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/internal")
@Api("内部消息接口")
public class MessageClientImp implements MessageClient{

	@Autowired
	MessageService msgService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/push", method = RequestMethod.POST)
	@ApiOperation(value = "推送消息", notes = "推送消息")
	public Response push(@ApiParam(name="targetuid", value="目标用户") @RequestParam(name="targetuid") Integer targetuid,
			@ApiParam(name="type", value="消息类型") @RequestParam(name="type")int type,
			@ApiParam(name="content", value="消息内容") @RequestParam(name="content")String content){
		
		try{		
			msgService.push(targetuid, type, content);
			
			return Response.OK(null);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
}
