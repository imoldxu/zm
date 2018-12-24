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

import com.zm.service.entity.Tag;
import com.zm.service.entity.User;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.HouseCommentService;
import com.zm.service.service.TagService;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("标签接口")
@RequestMapping("/tag")
public class TagController{

	@Autowired
	TagService tagService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getTags", method = RequestMethod.GET)
	@ApiOperation(value = "获取全部的tag", notes = "获取全部的tag")
	public Response getTags(
			HttpServletRequest request, HttpServletResponse response) {
		try{
			List<Tag> tagList = tagService.getTags();
			return Response.OK(tagList);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
		
	}
	
	
	
}
