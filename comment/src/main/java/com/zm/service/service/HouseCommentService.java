package com.zm.service.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.Response;
import com.zm.service.context.TagComment;
import com.zm.service.entity.HComment;
import com.zm.service.entity.Reserve;
import com.zm.service.entity.User;
import com.zm.service.feign.client.ReserveClient;
import com.zm.service.feign.client.UserClient;
import com.zm.service.mapper.HouseCommentMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class HouseCommentService {

	@Autowired
	HouseCommentMapper hCommentMapper;
	@Autowired
	ReserveClient reserveClient;
	@Autowired
	UserClient userClient;
	
	public void commit(Integer uid, Long reserveid, String content, String imglist, List<TagComment> tagComments) {
		//校验是否已经完成看房
		Response resp = reserveClient.getReserveById(reserveid);
		ObjectMapper mapper = new ObjectMapper();
		Reserve reserve = mapper.convertValue(resp.fetchOKData(), Reserve.class);
		if(reserve == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(reserve.getRuid() != uid){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你无权进行此操作");
		}
		if(reserve.getState() != Reserve.STATE_COMPLETE){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "只能在看房之后才可以进行评论");
		}
		//检查是否已经完成评论
		if(reserve.getIscomment() == 1){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "已经进行过评论，不能重复评论");
		}
		// TODO 提交评论
		HComment comment = new HComment();
		comment.setContent(content);
		comment.setCreatetime(new Date());
		comment.setHouseid(reserve.getHouseid());
		comment.setUid(uid);
		comment.setImglist(imglist);
		
		hCommentMapper.insert(comment);
		
		//奖励一个看房币
		resp = userClient.addCoin(uid, 1, "评论奖励");
		resp.fetchOKData();
	}

	public List<HComment> getComments(Long houseid, int pageIndex, int pageSize) {

		Example ex = new Example(HComment.class);
		ex.createCriteria().andEqualTo("houseid", houseid);
		ex.setOrderByClause("id desc");
		
		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
		List<HComment> comments = hCommentMapper.selectByExampleAndRowBounds(ex, rowBounds);
		for(HComment comment: comments) {
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.convertValue(userClient.getUser(comment.getUid()).fetchOKData(), User.class);
			
			comment.setUserNick(user.getNick());
			comment.setUserAvatar(user.getAvatar());
		}
		return comments;
	}

	public HComment getLastComment(Long houseid) {
		Example ex = new Example(HComment.class);
		ex.createCriteria().andEqualTo("houseid", houseid);
		ex.setOrderByClause("id desc");
		HComment comment = hCommentMapper.selectOneByExample(ex);
		
		if(comment!=null) {
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.convertValue(userClient.getUser(comment.getUid()).fetchOKData(), User.class);
		
			comment.setUserNick(user.getNick());
			comment.setUserAvatar(user.getAvatar());
		}
		return comment;
	}
	
	
}
