package com.zm.service.feign.client.fallback;

import org.springframework.stereotype.Component;

import com.zm.service.context.Response;
import com.zm.service.feign.client.TagClient;

@Component
public class DefaultTagClient implements TagClient{

	@Override
	public Response addHouseTags(long houseid, String tagListStr) {
		return Response.OK(null);
	}

	@Override
	public Response getHouseTags(long houseid) {
		return Response.OK(null);
	}

}
