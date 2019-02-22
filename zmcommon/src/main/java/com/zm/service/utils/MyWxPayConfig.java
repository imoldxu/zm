package com.zm.service.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

public class MyWxPayConfig extends WXPayConfig{

	private static final String appid = "wx27274648aadcf410";
	private static final String merchantid = "1525781591";
	private static final String merchant_secret = "fjlsjdlfp28301231j203812031jp203";
	private static final String certPath = "classpath:apiclient_cert.p12";
	private byte[] certData = null;
	
	public MyWxPayConfig() throws Exception{
		if(!certPath.isEmpty()){
			File file = ResourceUtils.getFile(certPath);
	    	InputStream certStream = new FileInputStream(file);
	    	certData = new byte[(int) file.length()];
	    	certStream.read(certData);
	    	certStream.close();
		}
	}
	
	/**
     * 获取 App ID
     *
     * @return App ID
     */
    public String getAppID(){
    	return appid;
    }


    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    public String getMchID(){
    	return merchantid;
    }


    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    public String getKey(){
    	return merchant_secret;
    }


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public InputStream getCertStream(){
		
    	return new ByteArrayInputStream(certData);
    }
	
    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     * @return
     */
    public IWXPayDomain getWXPayDomain(){
    	IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
	
}
