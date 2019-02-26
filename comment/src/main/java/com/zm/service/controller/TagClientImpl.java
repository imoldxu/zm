package com.zm.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.service.context.ComplainState;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.context.TagComment;
import com.zm.service.entity.HouseTag;
import com.zm.service.feign.client.TagClient;
import com.zm.service.service.HouseTagService;
import com.zm.service.service.UserCommentService;
import com.zm.service.utils.JSONUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/internal")
@Api("tag标签内部接口")
public class TagClientImpl implements TagClient{

	@Autowired
	HouseTagService houseTagService;
	@Autowired
	UserCommentService userCommentService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/addHouseTags", method = RequestMethod.POST)
	@ApiOperation(value = "添加房源标签", notes = "获取房源评论")
	public Response addHouseTags(@ApiParam(name="houseid", value="房源id") @RequestParam(name="houseid") long houseid,
			@ApiParam(name="tagListStr", value="tagList的json传") @RequestParam(name="tagListStr") String tagListStr){
		List<TagComment> list;	
		try{
			list = JSONUtils.getObjectListByJson(tagListStr, TagComment.class);
		}catch (Exception e) {
			return Response.Error(ErrorCode.DATA_ERROR, "内部参数错误");
		}
		try{
			houseTagService.addHouseTags(houseid, list);
		
			return Response.OK(null);
		} catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			return Response.SystemError();
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getHouseTags", method = RequestMethod.GET)
	@ApiOperation(value = "添加房源标签", notes = "获取房源评论")
	public Response getHouseTags(@ApiParam(name="houseid", value="房源id") @RequestParam(name="houseid") long houseid){
		try{
			List<HouseTag> list = houseTagService.getHouseTags(houseid);
			
			return Response.OK(list);
		} catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			return Response.SystemError();
		}
	}

	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getUserCommentInfo", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户评论状态", notes = "获取用户评论状态")
	public Response getUserCommentInfo(int uid) {
		try{
			ComplainState state = userCommentService.getCommentState(uid);
			
			return Response.OK(state);
		} catch(HandleException e){
			return Response.Error(e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			return Response.SystemError();
		}
	}
}
