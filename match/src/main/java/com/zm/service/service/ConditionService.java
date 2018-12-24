package com.zm.service.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zm.service.entity.Condition;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.mapper.ConditionMapper;
import com.zm.service.utils.IdCardUtil;
import com.zm.service.utils.RedissonUtil;
import com.zm.service.utils.ValidDataUtil;
import com.zm.service.utils.WxUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class ConditionService {

	@Autowired
	ConditionMapper conditionMapper;
	
	public Condition create(Integer uid, Condition condition) {
		
		if( getConditionByUid(uid) != null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "只允许同时发布一个求租请求");
		}
		
		condition.setUid(uid);
		condition.setCreatetime(new Date());
		condition.setIsnotify(0);
		condition.setState(Condition.STATE_VALID);
		conditionMapper.insertUseGeneratedKeys(condition);
		
		return condition;
	}

	public Condition getConditionByUid(Integer uid) {
		Example example = new Example(Condition.class);
		example.createCriteria().andEqualTo("uid", uid).andEqualTo("state", Condition.STATE_VALID);
		Condition ret = conditionMapper.selectOneByExample(example);
		return ret;
	}

	public void del(Integer uid, Long conditionid) {
		// TODO 检查权限
		Condition condition = conditionMapper.selectByPrimaryKey(conditionid);
		if(condition == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(condition.getUid()!=uid){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "无权此操作");
		}
		
		condition.setState(Condition.STATE_INVALID);
		
	    conditionMapper.updateByPrimaryKey(condition);
	}

	public Condition modifyCondition(Integer uid, Condition condition) {
		if(condition.getUid() !=  uid){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "无权此操作");
		}
		
		conditionMapper.updateByPrimaryKey(condition);
		
		return condition;
	}

	
}
