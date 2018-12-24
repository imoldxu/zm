package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultMessageClient;

@FeignClient(name="msg-service", fallback=DefaultMessageClient.class)
public interface MessageClient {

	@RequestMapping(value = "/internal/push", method = RequestMethod.POST)
	public Response push(@RequestParam(name="targetuid") Integer targetuid,
			@RequestParam(name="type")int type,
			@RequestParam(name="content")String content);
}
