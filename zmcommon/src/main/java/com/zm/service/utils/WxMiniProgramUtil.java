package com.zm.service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Formatter;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class WxMiniProgramUtil {

	private static Logger log = LoggerFactory.getLogger(WxMiniProgramUtil.class);
	
	/**
	 * 发送消息相关
	 */
	private static final String TOKEN = "zkhz";
	private static final String encodingAesKey = "WADge6EHVX3zxQ3OculChEommAdP5aV3ajk3Hww3RMm";

	
	public static final String grant_type = "client_credential";

	public static final String littleapp_appid = "wx27274648aadcf410";  //租盟
	public static final String littleapp_secret = "d0e80bf423848e0ebb647998e2228d63";
	
	public static JsonNode getOauthInfobylittleApp(String wxCode) throws IOException {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/sns/jscode2session", "get");
			h.addParameter("appid", littleapp_appid);
			h.addParameter("secret", littleapp_secret);
			h.addParameter("js_code", wxCode);
			h.addParameter("grant_type", "authorization_code");

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if (200 == status) {
				String response = h.getResponseBodyAsString("utf-8");
				ret = JSONUtils.getJsonObject(response);
				JsonNode errorcode = ret.get("errcode");
				if (null != errorcode) {
					System.out.println("weChat errorCode is:"+errorcode);
					String errMsg = ret.get("errmsg").asText();
					throw new IOException("微信授权请求错误:" + errMsg);
				} else {
					return ret;
				}
			} else {
				throw new IOException("微信服务器请求失败");
			}
		} finally {
			h.close();
		}
	}
	
	public static String convertAvatar(String headerImgURL) {
		if(null != headerImgURL &&  !headerImgURL.isEmpty()){
			//去掉微信图片为0的标号
			headerImgURL = headerImgURL.substring(0, headerImgURL.length()-3);
			//设置微信图片为64的标号
			headerImgURL = headerImgURL+"64";
		}
		return headerImgURL;
	}
	
	public static String converWxNick(String wxnick){
		String regEx = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(wxnick);
		return m.replaceAll("").trim();
	}
	
	public static String getToken() {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode node = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/token", "get");
			h.addParameter("grant_type", grant_type);
			h.addParameter("appid", littleapp_appid);
			h.addParameter("secret", littleapp_secret);

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if(status == 200){
				String context = h.getResponseBodyAsString("utf-8");
				node = JSONUtils.getJsonObject(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			h.close();
		}
		return node.get("access_token").asText();
	}
	
	/**
	 * 发送模板消息，接收微信的url验证
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 * @throws AesException
	 */
	public static String checkSignature(String signature, String timestamp, String nonce, String echostr) throws AesException{

		WXBizMsgCrypt pc = new WXBizMsgCrypt(TOKEN, encodingAesKey, littleapp_appid);
		String decodeEcho = pc.verifyUrl(signature, timestamp, nonce, "");
		
		return echostr;
	}
	
	/**
	 * 发送模板消息
	 * @param openid
	 * @param access_token
	 * @param userNick
	 * @param orderid
	 * @param giftName
	 * @param money
	 * @param time
	 * @return
	 * @throws IOException
	 */
	public static boolean pushTemplateMsg(String openid, String access_token, String template_id, String page, String form_id, JSONObject msg) {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token, "post");
			
			HashMap<String, Object> map = new HashMap<>();
			
//			String msg = "{\"first\": {\"value\":\"携玩会员:"+ userNick +"完成了兑换\",\"color\":\"#0000FF\"},"
//					+ "\"keyword1\":{\"value\":\""+orderid+"\",\"color\":\"#0000FF\"},"
//					+ "\"keyword2\":{\"value\":\""+giftName+"\",\"color\":\"#0000FF\"},"
//					+ "\"keyword3\":{\"value\":\""+money+"\",\"color\":\"#0000FF\"},"
//					+ "\"keyword4\":{\"value\":\""+time+"\",\"color\":\"#0000FF\"},"
//					+"\"remark\":{\"value\":\"请提供礼品\",\"color\":\"#0000FF\"}}";
//			
//			JsonNode msgObj = JSONUtils.getJsonObject(msg);
//			
			map.put("touser", openid);
			map.put("template_id", template_id);
			if(page!=null && !page.isEmpty()) {
				map.put("page", page);
			}
			map.put("form_id", form_id);
			log.info(msg.toJSONString());
			map.put("data", msg.toJSONString());
			
			JsonNode postData = JSONUtils.getJsonObject(map);
			
			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			h.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			
			
			log.info(postData.toString());
			int status = h.postJson(postData.toString());
			if (200 == status) {
				String response = h.getResponseBodyAsString("utf-8");
				ret = JSONUtils.getJsonObject(response);
				JsonNode errorcode = ret.get("errcode");
				if (errorcode != null && 0 != errorcode.asInt()) {
					String errmsg = ret.get("errmsg").asText();
					System.out.println("微信推送模板消息失败:" + errmsg);
					return false;
				} else {
					return true;
				}
			} else {
				throw new HandleException(ErrorCode.WX_NET_ERROR, "微信服务器请求失败:"+status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HandleException(ErrorCode.WX_NET_ERROR, "微信服务器请求失败");
		} finally {
			h.close();
		}
	}
	
	public static JsonNode getUserInfo(String encryptedData,String sessionKey,String iv){
	    // 被加密的数据
	    byte[] dataByte = Base64.decode(encryptedData.getBytes());
	    // 加密秘钥
	    byte[] keyByte = Base64.decode(sessionKey.getBytes());
	    // 偏移量
	    byte[] ivByte = Base64.decode(iv.getBytes());
	    try {
	        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
	        int base = 16;
	        if (keyByte.length % base != 0) {
	            int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
	            byte[] temp = new byte[groups * base];
	            Arrays.fill(temp, (byte) 0);
	            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
	            keyByte = temp;
	        }
	        // 初始化
	        Security.addProvider(new BouncyCastleProvider());
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
	        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
	        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
	        parameters.init(new IvParameterSpec(ivByte));
	        cipher.init( Cipher.DECRYPT_MODE, spec, parameters);// 初始化
	        byte[] resultByte = cipher.doFinal(dataByte);
	        if (null != resultByte && resultByte.length > 0) {
	            String result = new String(resultByte, "UTF-8");
	            return JSONUtils.getJsonObject(result);
	        }
	    } catch (Exception e) {
	        throw new HandleException(ErrorCode.WX_CRYPTO_ERROR, "微信解密异常");
	    }
		return null;
	}
	

}
