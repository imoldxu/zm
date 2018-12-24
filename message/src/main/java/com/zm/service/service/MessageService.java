package com.zm.service.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zm.service.entity.Message;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.mapper.MessageMapper;
import com.zm.service.utils.IdCardUtil;
import com.zm.service.utils.RedissonUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class MessageService {

	@Autowired
	RedissonUtil redissonUtil;
	@Autowired
	MessageMapper msgMapper;
	
	
	public List<Message> getMyMessage(Integer uid, int pageIndex, int pageSize) {
		Example ex = new Example(Message.class);
		ex.createCriteria().andEqualTo("uid", uid);
		ex.setOrderByClause("id desc");
		RowBounds rowBounds = new RowBounds(pageIndex*pageSize, pageSize);
		List<Message> ret = msgMapper.selectByExampleAndRowBounds(ex, rowBounds);
		return ret;
	}


	public void push(Integer targetuid, int type, String content) {
		
		Message msg = new Message();
		msg.setContent(content);
		msg.setType(type);
		msg.setUid(targetuid);
		msg.setCreatetime(new Date());
		msg.setState(1);
		//TODO 推送到微信
		
		return;
	}
	
	
	
}
