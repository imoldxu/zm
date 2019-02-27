package com.zm.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zm.service.utils.RedissonUtil;
import com.zm.service.utils.WxMiniProgramUtil;

@Component
public class WxTask {

	@Autowired
	RedissonUtil redisson;
	
	//每隔一个小时刷新一次wx小程序的token
	@Scheduled(fixedRate=3600000)
	public void refreshAccessToken() {
		try {
			String token = WxMiniProgramUtil.getToken();
			redisson.set("wechat_mini_access_token", token, 7200000L);	
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
