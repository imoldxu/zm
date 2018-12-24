package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.Response;
import com.zm.service.feign.client.UserClient;

@Component
public class DefaultUserClient implements UserClient{

	@Override
	public Response getUser(Integer uid) {
		return Response.SystemError();
	}

	@Override
	public Response getAccount(Integer uid) {
		return Response.SystemError();
	}

	@Override
	public Response addCoin(Integer uid, int amount, String msg) {
		return Response.SystemError();
	}

	@Override
	public Response reduceCoin(Integer uid, int amount, String msg) {
		return Response.SystemError();
	}

	@Override
	public Response addCash(Integer uid, int amount, String msg) {
		return Response.SystemError();
	}

	@Override
	public Response lockCash(Integer uid, int amount, String msg) {
		return Response.SystemError();
	}

	@Override
	public Response unlockCash(Integer uid, int amount, String msg) {
		return Response.SystemError();
	}

	@Override
	public Response payCash(Integer uid, Integer touid, int amount, String msg) {
		return Response.SystemError();
	}

	
}
