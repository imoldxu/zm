package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.Response;
import com.zm.service.feign.client.TransactionClient;

@Component
public class DefaultTransactionClient implements TransactionClient {

	@Override
	public Response cancel(long transid) {
		return Response.SystemError();
	}

	@Override
	public Response payOver(long transid) {
		return Response.SystemError();
	}

}
