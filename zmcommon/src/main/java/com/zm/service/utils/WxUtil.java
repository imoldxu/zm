package com.zm.service.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.codec.Base64;

import com.fasterxml.jackson.databind.JsonNode;
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

public class WxUtil {

	public static final String grant_type = "client_credential";
	public static final String appid = "wx163c4c3d188f8ee5";  //乐游
	public static final String secret = "b930acfb50a02e11d1c55882ac813a05";
	
	public static final String app_appid = "wxc342d56bf6ebb44e";  //乐游
	public static final String app_secret = "3557c60cb7981b2bd5165fe75696b584";

	public static final String littleapp_appid = "wx27274648aadcf410";  //租盟
	public static final String littleapp_secret = "d0e80bf423848e0ebb647998e2228d63";
	
	
	public static Map<String, String> sign(String url) {
		String token = getToken(grant_type, appid, secret);
		String ticket = getTicket(token);
		return sign(ticket, url);
	}

	private static String getToken(String grant_type, String appid, String secret) {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode node = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/token", "get");
			h.addParameter("grant_type", grant_type);
			h.addParameter("appid", appid);
			h.addParameter("secret", secret);

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

	public static String getTicket(String token) {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode node = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/ticket/getticket", "get");
			h.addParameter("access_token", token);
			h.addParameter("type", "jsapi");

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			String context = h.getResponseBodyAsString("utf-8");
			node = JSONUtils.getJsonObject(context);
			System.out.println(context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			h.close();
		}
		return node.get("ticket").asText();
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		//System.out.println(string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	public static String getToken() {
		return getToken(grant_type, appid, secret);
	}

	public static JsonNode getOauthInfo(String wxCode) throws IOException {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/sns/oauth2/access_token", "get");
			h.addParameter("appid", appid);
			h.addParameter("secret", secret);
			h.addParameter("code", wxCode);
			h.addParameter("grant_type", "authorization_code");

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if (200 == status) {
				String response = h.getResponseBodyAsString("utf-8");
				ret = JSONUtils.getJsonObject(response);
				JsonNode errorcode = ret.get("errcode");
				if (null != errorcode) {
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

	public static JsonNode getOauthInfobyAPP(String wxCode) throws IOException {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/sns/oauth2/access_token", "get");
			h.addParameter("appid", app_appid);
			h.addParameter("secret", app_secret);
			h.addParameter("code", wxCode);
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
	
	public static JsonNode getUserInfo(JsonNode wxOauthInfo) throws IOException {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/sns/userinfo", "get");
			h.addParameter("access_token", wxOauthInfo.get("access_token").asText());
			h.addParameter("openid", wxOauthInfo.get("openid").asText());
			h.addParameter("lang", "zh_CN");

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if (200 == status) {
				String response = h.getResponseBodyAsString("utf-8");
				ret = JSONUtils.getJsonObject(response);
				JsonNode errorcode = ret.get("errcode");
				if (null != errorcode) {
					String errmsg = ret.get("errmsg").asText();
					throw new IOException("微信获取用户信息错误:" + errmsg);
				} else {
					return ret;
				}
			} else {
				throw new IOException("微信服务器请求失败");
			}
		}  finally {
			h.close();
		}
	}
	
	//使用关注了公众号的用户来获取用户信息
	public static JsonNode getUserInfo2(String accessToken, JsonNode wxOauthInfo) throws IOException {
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/user/info", "get");
			h.addParameter("access_token", accessToken);
			h.addParameter("openid", wxOauthInfo.get("openid").asText());
			h.addParameter("lang", "zh_CN");

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if (200 == status) {
				String response = h.getResponseBodyAsString("utf-8");
				ret = JSONUtils.getJsonObject(response);
				JsonNode errorcode = ret.get("errcode");
				if (null != errorcode) {
					String errmsg = ret.get("errmsg").asText();
					throw new IOException("微信获取用户信息错误:" + errmsg);
				} else {
					int subscribe = ret.get("subscribe").intValue();
					if(subscribe==0){
						//若没有关注，就从SNS方式获取用户信息
						return getUserInfo(wxOauthInfo); 
					}else{
						return ret;
					}
				}
			} else {
				throw new IOException("微信服务器请求失败");
			}
		}  finally {
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
	public static boolean pushTemplateMsg(String openid, String access_token, String userNick, String orderid,String giftName, String money, String time ) throws IOException{
		HttpClientUtil h = new HttpClientUtil();
		JsonNode ret = null;
		try {
			h.open("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token, "post");
			
			HashMap<String, Object> map = new HashMap<>();
			
			String msg = "{\"first\": {\"value\":\"携玩会员:"+ userNick +"完成了兑换\",\"color\":\"#0000FF\"},"
					+ "\"keyword1\":{\"value\":\""+orderid+"\",\"color\":\"#0000FF\"},"
					+ "\"keyword2\":{\"value\":\""+giftName+"\",\"color\":\"#0000FF\"},"
					+ "\"keyword3\":{\"value\":\""+money+"\",\"color\":\"#0000FF\"},"
					+ "\"keyword4\":{\"value\":\""+time+"\",\"color\":\"#0000FF\"},"
					+"\"remark\":{\"value\":\"请提供礼品\",\"color\":\"#0000FF\"}}";
			
			JsonNode msgObj = JSONUtils.getJsonObject(msg);
			
			//h.addParameter("touser", openid);
			//h.addParameter("template_id", "-QqtuXZZlcYVTQm3_sRSb_tgGymougL7onz1EbjB0rY");
			//h.addParameter("url", "http://weixin.qq.com/download");
			//h.addParameter("data", xxx);
			map.put("touser", openid);
			map.put("template_id", "-QqtuXZZlcYVTQm3_sRSb_tgGymougL7onz1EbjB0rY");
			map.put("url", "http://weixin.qq.com/download");
			map.put("data", msgObj);
			
			JsonNode postData = JSONUtils.getJsonObject(map);
			
			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			
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
				throw new IOException("微信服务器请求失败");
			}
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
