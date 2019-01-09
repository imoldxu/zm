package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultHouseClient;

@FeignClient(name="house-service", fallback=DefaultHouseClient.class)
public interface HouseClient {

	@RequestMapping(value = "/internal/getHouseById", method = RequestMethod.GET)
	public Response getHouseById(@RequestParam(name="houseid") Long houseid);

	@RequestMapping(value = "/internal/modifyState", method = RequestMethod.POST)
	Response modifyState(@RequestParam(name="houseid")Long houseid, @RequestParam(name="state")int state);

	@RequestMapping(value = "/internal/getSimpleHouseById", method = RequestMethod.GET)
	public Response getSimpleHouseById(@RequestParam(name="houseid") Long houseid);
}
