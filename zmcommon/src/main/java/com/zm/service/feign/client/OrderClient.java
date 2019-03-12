package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultOrderClient;

@FeignClient(name="payment-service", fallback=DefaultOrderClient.class)
public interface OrderClient {

	@RequestMapping(value = "/internal/create", method = RequestMethod.POST)
	Response create( @RequestParam(name="uid")Integer uid,
			 @RequestParam(name="code")Integer code,
			 @RequestParam(name="amount")int amount,
			 @RequestParam(name="info")String info);

}
