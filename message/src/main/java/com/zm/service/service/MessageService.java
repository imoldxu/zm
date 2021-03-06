package com.zm.service.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.entity.Message;
import com.zm.service.entity.User;
import com.zm.service.entity.WxFormId;
import com.zm.service.feign.client.UserClient;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.ReserveMessage;
import com.zm.service.mapper.MessageMapper;
import com.zm.service.mapper.WxFormIdMapper;
import com.zm.service.utils.DateUtils;
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
	WxFormIdMapper wxFormIdMapper;
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
				push2RenderReserveMessage(user, msgObj);
			}else if(type == Message.TYPE_RESERVE_ISSUER_NOTICE) {
				ReserveMessage msgObj = JSONUtils.getObjectByJson(content, ReserveMessage.class);
				push2IssueReserveMessage(user, msgObj);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new HandleException(ErrorCode.NORMAL_ERROR, "消息发送失败");
		}
		return;
	}
	
	private String getFormId(Integer uid) {
		String ret = "";
		//清理过期的formid
		Date now = new Date();
		//Example ex = new Example(WxFormId.class);
		//ex.createCriteria().andLessThanOrEqualTo("expiretime", now);
		//wxFormIdMapper.deleteByExample(ex);
		
		Example ex = new Example(WxFormId.class);
		ex.createCriteria().andEqualTo("uid", uid).andGreaterThan("expiretime", now);
		ex.setOrderByClause("expiretime ASC");
		List<WxFormId> formIdList = wxFormIdMapper.selectByExample(ex);
		if(!formIdList.isEmpty()) {
			WxFormId formId = formIdList.get(0);
			ret = formId.getFormid();
			int opret = wxFormIdMapper.delete(formId);
			int i=0;
		}
		return ret;
	}
	
	private void push2RenderReserveMessage(User user, ReserveMessage msgObj){
		String openid = user.getWxminiopenid();
		String access_token = (String) redissonUtil.get("wechat_mini_access_token");
		String template_id = "EcMJSYNHukGBcpbXZyymi_j2J0zRv_cOb6VOEwnnU4A";
		String page = "";
		String form_id = getFormId(user.getId());
		if(form_id.isEmpty()) {
			return;
		}
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
	
	private void push2IssueReserveMessage(User user, ReserveMessage msgObj){
		String openid = user.getWxminiopenid();
		String access_token = (String) redissonUtil.get("wechat_mini_access_token");
		String template_id = "EcMJSYNHukGBcpbXZyymi1eXUnXIw6n_L5vGdJUX9Zc";
		String page = "";
		String form_id = getFormId(user.getId());
		if(form_id.isEmpty()) {
			return;
		}
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
	
	public void uploadFormId(Integer uid, JSONArray formids) {
		Object[] ids = formids.toArray();
		List<WxFormId> recordList = new ArrayList<WxFormId>();
		for(int i=0; i<ids.length; i++) {
			String id = ids[i].toString();
			WxFormId wxFormId = new WxFormId();
			wxFormId.setUid(uid);
			wxFormId.setFormid(id);
			wxFormId.setExpiretime(DateUtils.addDays(new Date(), 6));//7天有效，为保障临界值，设置为6天有效
			recordList.add(wxFormId);
		}

		wxFormIdMapper.insertList(recordList);
	}
}
