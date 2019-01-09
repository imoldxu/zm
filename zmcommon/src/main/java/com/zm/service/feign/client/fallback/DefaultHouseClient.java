package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.ErrorCode;
import com.zm.service.context.Response;
import com.zm.service.feign.client.HouseClient;

@Component
public class DefaultHouseClient implements HouseClient {

	@Override
	public Response getHouseById(Long houseid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "House服务调用失败");
	}

	@Override
	public Response modifyState(Long houseid, int state) {
		return Response.Error(ErrorCode.MODULE_ERROR, "House服务调用失败");
	}

	@Override
	public Response getSimpleHouseById(Long houseid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "House服务调用失败");
	}

}
