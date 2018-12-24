package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultTagClient;


@FeignClient(name="comment-service", fallback=DefaultTagClient.class)
public interface TagClient {

	@RequestMapping(value = "/internal/addHouseTags", method = RequestMethod.POST)
	public Response addHouseTags(@RequestParam(name="houseid") long houseid,
			@RequestParam(name="tagListStr") String tagListStr);
	
	@RequestMapping(value = "/internal/getHouseTags", method = RequestMethod.GET)
	public Response getHouseTags(@RequestParam(name="houseid") long houseid);
	
}
