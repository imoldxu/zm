package com.zm.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zm.service.service.OrderService;

@Component
public class InvalidTask {

	@Autowired
	OrderService orderService;
	
	@Scheduled(fixedRate = 60000)
	public void task(){
		orderService.handleInvalidOrder();
	}
	
}
