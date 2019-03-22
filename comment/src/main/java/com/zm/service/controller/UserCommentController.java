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

import com.zm.service.entity.UComment;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.service.UserCommentService;
import com.zm.service.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("用户评论接口")
@RequestMapping("/user")
public class UserCommentController{

	@Autowired
	UserCommentService commentService;
	
	/**
	* showdoc
	* @catalog 接口文档/评论相关
	* @title 提交用户评论
	* @description 提交用户评论
	* @method post
	* @url /comment-service/user/commit
	* @param targetUid 必选 Integer 评论对象用户id  
	* @param content 必选 string 评论内容  
	* @return {"code":1,"data":null,"msg":"成功"}
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	@ApiOperation(value = "提交评论", notes = "提交评论")
	public Response commit(
			@ApiParam(name = "targetUid", value = "目标用户id") @RequestParam(name = "targetUid") Integer targetUid,
			@ApiParam(name = "content", value = "内容") @RequestParam(name = "content") String content,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer uid = SessionUtil.getUserId(request);
			
			commentService.commit(uid, targetUid, content);
			
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
	* @title 获取用户评论
	* @description 获取用户评论
	* @method get
	* @url /comment-service/user/getComments
	* @param targetUid 必选 Integer 评论对象用户id  
	* @param pageIndex 必选 int 页码 1-n
	* @param pageSize 必选 int 每页最大数量  
	* @return {"code":1,"data":[{"id":1,"targetuid":2,"uid":1,"content":"好人啊","imglist":null,"createtime":1549954256000}],"msg":"成功"}
	* @return_param id long 评论id
	* @return_param targetuid int 被评论用户的id
	* @return_param uid int 评论者id
	* @return_param content string 评论内容
	* @return_param imglist string 评论的图片列表数组
	* @return_param createtime int 评论创建时间，距离1970年1月1日的毫秒数
	* @remark 这里是备注信息
	* @number 99
	*/
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getComments", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户评论", notes = "获取用户评论")
	public Response getComments(
			@ApiParam(name = "targetUid", value = "目标id") @RequestParam(name = "targetUid") Integer targetUid,
			@ApiParam(name = "pageIndex", value = "页码") @RequestParam(name = "pageIndex") int pageIndex,
			@ApiParam(name = "pageSize", value = "每页数量") @RequestParam(name = "pageSize") int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			
			List<UComment> list = commentService.getComments(targetUid, pageIndex, pageSize);
			
			return Response.OK(list);
		}catch (HandleException e) {
			return Response.Error(e.getErrorCode(), e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			return Response.SystemError();
		}
	}
	
}
