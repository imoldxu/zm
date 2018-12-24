package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.Response;
import com.zm.service.feign.client.ReserveClient;

@Component
public class DefaultReserveClient implements ReserveClient {

	@Override
	public Response getReserveById(Long reserveid) {
		return Response.SystemError();
	}

	@Override
	public Response payOver(Long reserveid) {
		return Response.SystemError();
	}

	@Override
	public Response cancel(Long reserveid) {
		return Response.SystemError();
	}

	@Override
	public Response close(Integer uid) {
		return Response.SystemError();
	}

}
