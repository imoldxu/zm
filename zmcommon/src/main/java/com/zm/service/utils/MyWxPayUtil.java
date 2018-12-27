package com.zm.service.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.context.WXPayCharge;

public class MyWxPayUtil {

	private static final String notify_url = "http://";
	
	/**
	 * 微信支付统一下单接口
	 * @param openid 用户id在该商户下的openid
	 * @param payinfo 支付信息
	 * @param attach 附加数据
	 * @param orderno 订单号
	 * @param fee 交易金额
	 * @param ip app或h5，以及设备的ip
	 */
	public static WXPayCharge getPayCharge(String openid, String payinfo, String attach, String orderno, int fee, String ip){
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("body", payinfo);
		//signParams.put("device_info", "web");
		//reqData.put("notify_url", notify_url);
		reqData.put("openid", openid);
		reqData.put("out_trade_no", orderno);
		reqData.put("spbill_create_ip", ip);//TODO
		//reqData.put("sign_type", "MD5");
		//reqData.put("detail", "");
		//reqData.put("attach", attach);
		//reqData.put("fee_type", "CNY");
		reqData.put("total_fee", String.valueOf(fee));
		//reqData.put("time_start", "");
		//reqData.put("time_expire", "");
		//reqData.put("goods_tag", "");
		reqData.put("trade_type", "JSAPI");//小程序 JSAPI
		//reqData.put("product_id", "");
		//reqData.put("limit_pay", "no_credit");
		//reqData.put("receipt", "Y");	
		
		
		
		try{
			WXPayConfig config = new MyWxPayConfig();
			WXPay pay = new WXPay(config, notify_url);
			Map<String, String> resp = pay.unifiedOrder(reqData);
			
			String retcode = resp.get("return_code");
			if(retcode.equals("SUCCESS")){
				String resultcode = resp.get("result_code");
				if(resultcode.equals("SUCCESS")){
					Map<String, String> signData = new HashMap<String, String>();
					signData.put("appId", resp.get("appid"));
					String nonce_str = resp.get("nonce_str");
					signData.put("nonceStr", nonce_str);
					String prepay_id = resp.get("prepay_id");
					signData.put("package", "prepay_id="+prepay_id);
					signData.put("signType", WXPayConstants.MD5);
					String timeStamp = String.valueOf(new Date().getTime());
					signData.put("timeStamp", timeStamp);
					String paySign;
					try {
						paySign = WXPayUtil.generateSignature(signData, config.getKey());
					} catch (Exception e) {
						throw new HandleException(ErrorCode.WX_CRYPTO_ERROR, e.getMessage());
					}
					
					WXPayCharge charge = new WXPayCharge();
					charge.setNonceStr(nonce_str);
					charge.setPaySign(paySign);
					charge.setPrepay_id(prepay_id);
					charge.setSignType(WXPayConstants.MD5);
					charge.setTimeStamp(timeStamp);
					
					return charge;
				}else{
					throw new HandleException(ErrorCode.WX_ERROR, resp.get("err_code_des"));
				}
			}else{
				throw new HandleException(ErrorCode.WX_ERROR, resp.get("return_msg"));
			}
		}catch(HandleException e){
			throw new HandleException(e.getErrorCode(), e.getMessage());
		}catch (Exception e) {
			throw new HandleException(ErrorCode.WX_NET_ERROR, e.getMessage());
		}
		
		
	}
	
	public static Map<String, String> payBack(String notifyData) throws Exception {
	    Map<String, String> notifyMap = null;
	    
        WXPay wxpay = new WXPay(new MyWxPayConfig());

        notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            // 进行处理。
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            String return_code = notifyMap.get("return_code");//状态
            if(return_code.equals("SUCCESS")){
            	String result_code = notifyMap.get("result_code");
	            if(result_code.equals("return_code")){
	            	return notifyMap;
	            }else{
	            	throw new Exception();
	            }
            }else{
            	throw new Exception();
            }
        } else {
        	throw new Exception();
        }
	}
}
