package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.ErrorCode;
import com.zm.service.context.Response;
import com.zm.service.feign.client.OrderClient;

@Component
public class DefaultOrderClient implements OrderClient {

	@Override
	public Response create(Integer uid, Integer code, int amount, String info) {
		return Response.Error(ErrorCode.MODULE_ERROR, "订单服务调用失败");
	}

}
