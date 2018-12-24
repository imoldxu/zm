package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.Response;
import com.zm.service.feign.client.MessageClient;

@Component
public class DefaultMessageClient implements MessageClient {

	@Override
	public Response push(Integer targetuid, int type, String content) {
		return Response.OK(null);
	}

}
