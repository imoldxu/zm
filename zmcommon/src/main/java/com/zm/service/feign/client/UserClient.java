package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultUserClient;

@FeignClient(name="user-service", fallback=DefaultUserClient.class)
public interface UserClient {

	@RequestMapping(value = "/internal/getUser", method = RequestMethod.GET)
	public Response getUser(@RequestParam(name = "uid") Integer uid);
	
	@RequestMapping(value = "/internal/getAccount", method = RequestMethod.GET)
	public Response getAccount(@RequestParam(name = "uid") Integer uid);
	
	@RequestMapping(value = "/internal/addCoin", method = RequestMethod.POST)
	public Response addCoin(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);
	
	@RequestMapping(value = "/internal/reduceCoin", method = RequestMethod.POST)
	public Response reduceCoin(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);	
		
	@RequestMapping(value = "/internal/addCash", method = RequestMethod.POST)
	public Response addCash(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);
	
	@RequestMapping(value = "/internal/lockCash", method = RequestMethod.POST)
	public Response lockCash(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);
	
	@RequestMapping(value = "/internal/unlockCash", method = RequestMethod.POST)
	public Response unlockCash(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);

	@RequestMapping(value = "/internal/unlockCash", method = RequestMethod.POST)
	Response payCash(@RequestParam(name = "uid") Integer uid, @RequestParam(name = "touid") Integer touid, @RequestParam(name = "amount") int amount, @RequestParam(name = "msg") String msg);
	
}
