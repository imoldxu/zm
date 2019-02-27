package com.zm.service.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.entity.Message;
import com.zm.service.entity.User;
import com.zm.service.feign.client.UserClient;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.ReserveMessage;
import com.zm.service.mapper.MessageMapper;
import com.zm.service.utils.IdCardUtil;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.RedissonUtil;
import com.zm.service.utils.WxMiniProgramUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class MessageService {

	@Autowired
	RedissonUtil redissonUtil;
	@Autowired
	MessageMapper msgMapper;
	@Autowired
	UserClient userClient;
	
	public List<Message> getMyMessage(Integer uid, int pageIndex, int pageSize) {
		Example ex = new Example(Message.class);
		ex.createCriteria().andEqualTo("uid", uid);
		ex.setOrderByClause("id desc");
		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
		List<Message> ret = msgMapper.selectByExampleAndRowBounds(ex, rowBounds);
		return ret;
	}


	public void push(Integer targetuid, int type, String content) {
		
		Message msg = new Message();
		msg.setContent(content);
		msg.setType(type);
		msg.setUid(targetuid);
		msg.setCreatetime(new Date());
		//推送到微信
		ObjectMapper om = new ObjectMapper();
		User user = om.convertValue(userClient.getUser(targetuid).fetchOKData(), User.class);
		try {
			if(type == Message.TYPE_RESERVE_RENDER_NOTICE) {
				ReserveMessage msgObj = JSONUtils.getObjectByJson(content, ReserveMessage.class);
				push2RenderReserveMessage(user.getWxminiopenid(), msgObj);
			}else if(type == Message.TYPE_RESERVE_ISSUER_NOTICE) {
				ReserveMessage msgObj = JSONUtils.getObjectByJson(content, ReserveMessage.class);
				push2IssueReserveMessage(user.getWxminiopenid(), msgObj);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new HandleException(ErrorCode.NORMAL_ERROR, "消息发送失败");
		}
		return;
	}
	
	private void push2RenderReserveMessage(String openid, ReserveMessage msgObj){
		
		String access_token = (String) redissonUtil.get("wechat_mini_access_token");
		String template_id = "EcMJSYNHukGBcpbXZyymi_j2J0zRv_cOb6VOEwnnU4A";
		String page = "";
		String form_id = "";
		JSONObject msg = new JSONObject();
		JSONObject houseName = new JSONObject();
		houseName.put("value", msgObj.getHouseName());
		msg.put("keyword1", houseName);
		
		JSONObject issuerPhone = new JSONObject();
		issuerPhone.put("value", msgObj.getIssuerPhone());
		msg.put("keyword2", issuerPhone);
		
		JSONObject reserveTime = new JSONObject();
		reserveTime.put("value", msgObj.getDateTime());
		msg.put("keyword3", reserveTime);
		
		JSONObject state = new JSONObject();
		state.put("value", msgObj.getState());
		msg.put("keyword4", state);
		
		JSONObject remark = new JSONObject();
		remark.put("value", msgObj.getRemark());
		msg.put("keyword5", remark);
		
		WxMiniProgramUtil.pushTemplateMsg(openid, access_token, template_id, page, form_id, msg);
		return;
	}
	
	private void push2IssueReserveMessage(String openid, ReserveMessage msgObj){
		
		String access_token = (String) redissonUtil.get("wechat_mini_access_token");
		String template_id = "EcMJSYNHukGBcpbXZyymi1eXUnXIw6n_L5vGdJUX9Zc";
		String page = "";
		String form_id = "";
		JSONObject msg = new JSONObject();
		JSONObject houseName = new JSONObject();
		houseName.put("value", msgObj.getHouseName());
		msg.put("keyword1", houseName);
		
		JSONObject renderName = new JSONObject();
		renderName.put("value", msgObj.getRenderName());
		msg.put("keyword2", renderName);
		
		JSONObject renderPhone = new JSONObject();
		renderPhone.put("value", msgObj.getHouseName());
		msg.put("keyword3", renderPhone);
		
		JSONObject reserveTime = new JSONObject();
		reserveTime.put("value", msgObj.getDateTime());
		msg.put("keyword4", reserveTime);
		
		JSONObject state = new JSONObject();
		state.put("value", msgObj.getState());
		msg.put("keyword5", state);
		
		JSONObject remark = new JSONObject();
		remark.put("value", msgObj.getRemark());
		msg.put("keyword6", remark);
		
		WxMiniProgramUtil.pushTemplateMsg(openid, access_token, template_id, page, form_id, msg);
		return;
	}
}
