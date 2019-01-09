package com.zm.service.service;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zm.service.entity.User;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.mapper.UserMapper;
import com.zm.service.utils.IdCardUtil;
import com.zm.service.utils.RedissonUtil;
import com.zm.service.utils.ValidDataUtil;
import com.zm.service.utils.WxUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserService {

	@Autowired
	RedissonUtil redissonUtil;
	@Autowired
	UserMapper userMapper;
	
	public User loginByWxMiniprogram(String wxCode,String rawData, String signature, String encryptedData, String iv) {
		
		try {
			JsonNode wx_session = WxUtil.getOauthInfobylittleApp(wxCode);
			String sessionKey = wx_session.get("session_key").asText();
			String openID = wx_session.get("openid").asText();
			String unionID = wx_session.get("unionid").asText();
			
			JsonNode userJson = WxUtil.getUserInfo(encryptedData, sessionKey, iv);
			String nick = userJson.get("nickName").asText();
			nick = WxUtil.converWxNick(nick);
			String avatar = userJson.get("avatarUrl").asText();
			avatar = WxUtil.convertAvatar(avatar);
		
			User user = getUserByWxUnionID(unionID, nick, avatar);	
			
			return user;
		} catch (IOException e) {
			throw new HandleException(ErrorCode.WX_NET_ERROR, e.getMessage());
		}
	}
	
	public User loginByWx(String wxCode) throws IOException{

		JsonNode wxOauthInfo = WxUtil.getOauthInfo(wxCode);
		String accessToken = null;
		accessToken = (String)redissonUtil.get("wechat_access_token");
		
		JsonNode wxUserInfo = WxUtil.getUserInfo2(accessToken, wxOauthInfo);
		// 获得微信的数据
		String unionID = wxUserInfo.get("unionid").asText();
		String headerImgURL = wxUserInfo.get("headimgurl").asText();
		headerImgURL = WxUtil.convertAvatar(headerImgURL);
		String wxnick = wxUserInfo.get("nickname").asText();
		String nick = WxUtil.converWxNick(wxnick);
		// 获取微信账号对应的账号
		User user = getUserByWxUnionID(unionID, nick ,headerImgURL);
		
		if(user.getIdcardtype() == User.TYPE_IDCARD){
			String idcard = user.getIdcardnum();
			if(idcard!=null && !idcard.isEmpty()){
				try{
					int age = IdCardUtil.getAge(idcard);
					user.setAge(age);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		JsonNode subscribeNode = wxUserInfo.get("subscribe");
		if(subscribeNode!=null){
			int subscribe = subscribeNode.intValue();
			user.setSubscribe(subscribe);
		}else{
			user.setSubscribe(0);//视为没有关注
		}
		
		return user;
	}

	

	private User getUserByWxUnionID(String unionID, String nick, String avatar) {
		Example wxUserExample = new Example(User.class);
		wxUserExample.createCriteria().andEqualTo("wxunionid", unionID);
		wxUserExample.setOrderByClause("id asc");
		User user = userMapper.selectOneByExample(wxUserExample);
		if (user == null) {
			// 微信用户未注册
			user = new User();
			user.setWxunionid(unionID);
			user.setAvatar(avatar);
			user.setNick(nick);
			user.setCreatetime(new Date());
			userMapper.insertUseGeneratedKeys(user);
		} else {
			user.setAvatar(avatar);
			user.setNick(nick);
			userMapper.updateByPrimaryKey(user);			
		}
		return user;
	}

//	public void updateInfo(int uid, String name, String phone, int idcardtype, String idcardnum) {
//		
//		User user = userMapper.selectByPrimaryKey(uid);
//		if(user == null){
//			throw new HandleException(ErrorCode.NORMAL_ERROR, "系统异常,用户不存在");
//		}
//		if(idcardtype != User.TYPE_IDCARD && idcardtype != User.TYPE_JG){
//			throw new HandleException(ErrorCode.ARG_ERROR, "证件类型异常,请检查客户端");
//		}
//		if(idcardtype==User.TYPE_IDCARD){
//			if(!IdCardUtil.isIDCard(idcardnum)){
//				throw new HandleException(ErrorCode.NORMAL_ERROR, "身份证号有误,请检查");
//			}
//		}
//		if(!ValidDataUtil.isPhone(phone)){
//			throw new HandleException(ErrorCode.NORMAL_ERROR, "手机号有误,请检查"); 
//		}
//		
//		user.setName(name);
//		user.setPhone(phone);
//		user.setIdcardtype(idcardtype);
//		user.setIdcardnum(idcardnum);
//		
//		userMapper.updateByPrimaryKey(user);
//	}

	public User login(String phone, String password) {
		Example ex = new Example(User.class);
		ex.createCriteria().andEqualTo("phone", phone);
		User user = userMapper.selectOneByExample(ex);
		if(user == null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "用户不存在");
		}else{
			if(user.getPassword().equals(password)){
				return user;
			}else{
				throw new HandleException(ErrorCode.NORMAL_ERROR, "密码错误");
			}
		}
	}

	public void register(String phone, String password) {
		if(!ValidDataUtil.isPhone(phone)){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "手机号有误,请检查"); 
		}
		
		Example ex = new Example(User.class);
		ex.createCriteria().andEqualTo("phone", phone);
		User user = userMapper.selectOneByExample(ex);
		if(user != null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "用户已存在");
		}else{
			user = new User();
			user.setPhone(phone);
			user.setPassword(password);
			userMapper.insertUseGeneratedKeys(user);
		}
	}

	public void getVerificationCode(String phone) {
		Random r = new Random();
		int number = r.nextInt(999999);
		String code = String.format("%06d", number);
		
		redissonUtil.set("VERCODE_"+phone, code, 300000L);
		System.out.println("vercode:"+code);
		//TODO: 发送短信
	}

	public void verifyIDCard(int uid, String code, String name, String phone, int idcardtype, String idcardnum) {
		String vercode = (String) redissonUtil.get("VERCODE_"+phone);
		if(vercode==null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "验证码已过期");
		}
		if(!vercode.equals(code)){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "验证码错误");
		}
		User user = userMapper.selectByPrimaryKey(uid);
		if(user.getIdcardnum()!=null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "用户已实名认证，不可重复认证");
		}
		if(!IdCardUtil.isIDCard(idcardnum)){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "身份证号格式有误");
		}
		//TODO 调用实名认证接口
		
		//FIXME:验证成功
		user.setIdcardnum(idcardnum);
		user.setName(name);
		user.setPhone(phone);
		String birthday = IdCardUtil.getBirthday(idcardnum);
		user.setBirthday(birthday);
		//String sex = IdCardUtil.getSex(idcardnum);
		//user.setSex(sex);
		userMapper.updateByPrimaryKey(user);
	}

	public User getUserById(Integer uid) {
		User ret = userMapper.selectByPrimaryKey(uid);
		if(ret==null){
			throw new HandleException(ErrorCode.DATA_ERROR, "内部数据错误");
		}
		return ret;
	}

	
}
