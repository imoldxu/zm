package com.zm.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.service.context.Response;
import com.zm.service.feign.client.fallback.DefaultReserveClient;

@FeignClient(name="reserve-service", fallback=DefaultReserveClient.class)
public interface ReserveClient {

	@RequestMapping(value = "/internal/getReserveById", method = RequestMethod.GET)
	Response getReserveById(@RequestParam(name="reserveid") Long reserveid);

	@RequestMapping(value = "/internal/payOver", method = RequestMethod.POST)
	Response payOver(@RequestParam(name="reserveid") Long reserveid);

	@RequestMapping(value = "/internal/cancel", method = RequestMethod.POST)
	Response cancel(@RequestParam(name="reserveid") Long reserveid);
	
	@RequestMapping(value = "/internal/close", method = RequestMethod.POST)
	Response close(@RequestParam(name="uid") Integer uid, @RequestParam(name="type")int type);

	@RequestMapping(value = "/internal/checkReserve", method = RequestMethod.GET)
	Response checkReserve( @RequestParam(name = "houseid") Long houseid);
}
