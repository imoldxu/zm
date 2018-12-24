package com.zm.service.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.entity.UComment;
import com.zm.service.mapper.UserCommentMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserCommentService {

	@Autowired
	UserCommentMapper ucMapper;
	
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
		RowBounds rowBounds = new RowBounds(pageIndex*pageSize, pageSize);
		List<UComment> ret = ucMapper.selectByExampleAndRowBounds(ex, rowBounds);
		
		return ret;
	}

}
