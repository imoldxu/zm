package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultTransactionClient;

@FeignClient(name="trans-service", fallback=DefaultTransactionClient.class)
public interface TransactionClient {

	//@RequestMapping(value = "/internal/create", method = RequestMethod.POST)
	//public Response create(@RequestParam(name = "reserveStr") String reserveStr);

	@RequestMapping(value = "/internal/cancel", method = RequestMethod.POST)
	Response cancel(@RequestParam(name = "transid") long transid);

	@RequestMapping(value = "/internal/cancel", method = RequestMethod.POST)
	Response payOver(@RequestParam(name = "transid") long transid);
	
}
