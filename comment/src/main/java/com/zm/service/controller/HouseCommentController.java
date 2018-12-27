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

import com.zm.service.entity.HComment;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.context.TagComment;
import com.zm.service.service.HouseCommentService;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("评论接口")
@RequestMapping("/house")
public class HouseCommentController{

	@Autowired
	HouseCommentService commentService;
	
	/**
	* showdoc
	* @catalog 接口文档/评论相关
	* @title 提交房源评论
	* @description 提交房源评论
	* @method post
	* @url /comment-service/house/commit
	* @param reserveid 必选 Long 预约id  
	* @param content 必选 string 评论内容  
	* @param imglist 必选 string 图片列表[]的JSON对象
	* @param taglist 必选 string tag列表[]的JSON对象
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	@ApiOperation(value = "提交评论", notes = "提交评论")
	public Response commit(
			@ApiParam(name = "reserveid", value = "预约id") @RequestParam(name = "reserveid") Long reserveid,
			@ApiParam(name = "content", value = "内容") @RequestParam(name = "content") String content,
			@ApiParam(name = "imglist", value = "图片列表") @RequestParam(name = "imglist") String imglist,
			@ApiParam(name = "taglist", value = "tag的点评") @RequestParam(name = "taglist") String taglist,
			HttpServletRequest request, HttpServletResponse response) {
		List<TagComment> tagComments = null;
		try{
			tagComments = JSONUtils.getObjectListByJson(taglist, TagComment.class);
		}catch (Exception e) {
			return Response.Error(ErrorCode.ARG_ERROR, "参数错误");
		}
		try{
			
			Integer uid = SessionUtil.getUserId(request);
			
			commentService.commit(uid, reserveid, content, imglist, tagComments);
			
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
	* @catalog 接口文档/评论相关
	* @title 获取最近一条评论
	* @description 获取最近一条评论
	* @method post
	* @url /comment-service/house/getLastComment
	* @param houseid 必选 Long 房源id  
	* @return {"code":1,"data":{},"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getLastComment", method = RequestMethod.GET)
	@ApiOperation(value = "获取房源评论", notes = "获取房源评论")
	public Response getComments(
			@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") String houseid,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			HComment ret = commentService.getLastComment(houseid);
			return Response.OK(ret);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
	/**
	* showdoc
	* @catalog 接口文档/评论相关
	* @title 获取房源评论
	* @description 获取房源评论
	* @method get
	* @url /comment-service/house/getComments
	* @param houseid 必选 Long 预约id  
	* @param pageIndex 必选 int 页码  
	* @param pageSize 必选 int 每页最大数量
	* @return {"code":1,"data":[{}],"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getComments", method = RequestMethod.GET)
	@ApiOperation(value = "获取房源评论", notes = "获取房源评论")
	public Response getComments(
			@ApiParam(name = "houseid", value = "房源id") @RequestParam(name = "houseid") Long houseid,
			@ApiParam(name = "pageIndex", value = "页码") @RequestParam(name = "pageIndex") int pageIndex,
			@ApiParam(name = "pageSize", value = "每页数量") @RequestParam(name = "pageSize") int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			List<HComment> list = commentService.getComments(houseid, pageIndex, pageSize);
			return Response.OK(list);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
}
