package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.ErrorCode;
import com.zm.service.context.Response;
import com.zm.service.feign.client.TransactionClient;

@Component
public class DefaultTransactionClient implements TransactionClient {

	@Override
	public Response cancel(long transid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "Transaction服务调用失败");
	}

	@Override
	public Response payOver(long transid) {
		return Response.Error(ErrorCode.MODULE_ERROR, "Transaction服务调用失败");
	}

}
