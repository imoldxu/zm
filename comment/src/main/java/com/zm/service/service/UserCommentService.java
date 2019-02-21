package com.zm.service.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.context.ComplainState;
import com.zm.service.entity.UComment;
import com.zm.service.entity.User;
import com.zm.service.feign.client.UserClient;
import com.zm.service.mapper.UserCommentMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserCommentService {

	@Autowired
	UserCommentMapper ucMapper;
	@Autowired
	UserClient userClient;
	
	public void commit(Integer uid, Integer targetUid, String content) {
		
		UComment comment = new UComment();
		comment.setContent(content);
		comment.setTargetuid(targetUid);
		comment.setUid(uid);
		comment.setCreatetime(new Date());
		ucMapper.insertUseGeneratedKeys(comment);
	}

	public List<UComment> getComments(Integer targetUid, int pageIndex, int pageSize) {
		
		Example ex = new Example(UComment.class);
		ex.createCriteria().andEqualTo("targetuid", targetUid);
		ex.setOrderByClause("id desc");
		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
		List<UComment> ret = ucMapper.selectByExampleAndRowBounds(ex, rowBounds);
		
		for(UComment comment: ret) {
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.convertValue(userClient.getUser(comment.getUid()).fetchOKData(), User.class);
			
			comment.setUserNick(user.getNick());
			comment.setUserAvatar(user.getAvatar());
		}
		return ret;
	}

	public ComplainState getCommentState(int uid) {
		ComplainState state = new ComplainState();
		
		Example ex = new Example(UComment.class);
		ex.createCriteria().andEqualTo("targetuid", uid);
		int totalbeComplained = ucMapper.selectCountByExample(ex);
		state.setBeComplainedNum(totalbeComplained);
		
		ex = new Example(UComment.class);
		ex.createCriteria().andEqualTo("uid", uid);
		int totalComplain = ucMapper.selectCountByExample(ex);
		state.setComplainNum(totalComplain);
		
		return state;
	}

}
