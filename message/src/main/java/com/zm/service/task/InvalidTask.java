package com.zm.service.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zm.service.entity.WxFormId;
import com.zm.service.mapper.WxFormIdMapper;

import tk.mybatis.mapper.entity.Example;

@Component
public class InvalidTask {

	@Autowired
	WxFormIdMapper wxFormIdMapper;
	
	@Scheduled(fixedRate = 60000)
	public void task(){
		Date now = new Date();
		Example ex = new Example(WxFormId.class);
		ex.createCriteria().andLessThan("expiretime", now);
		wxFormIdMapper.deleteByExample(ex);
	}
	
}
