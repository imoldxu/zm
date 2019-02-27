package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.ErrorCode;
import com.zm.service.context.Response;
import com.zm.service.feign.client.ReserveClient;

@Component
public class DefaultReserveClient implements ReserveClient {

	@Override
	public Response getReserveById(Long reserveid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "预约服务调用失败");
	}

	@Override
	public Response payOver(Long reserveid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "预约服务调用失败");
	}

	@Override
	public Response cancel(Long reserveid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "预约服务调用失败");
	}

	@Override
	public Response close(Integer uid, int type) {
		return Response.Error(ErrorCode.MODULE_ERROR, "预约服务调用失败");
	}

	@Override
	public Response checkReserve(Long houseid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "预约服务调用失败");
	}

}
