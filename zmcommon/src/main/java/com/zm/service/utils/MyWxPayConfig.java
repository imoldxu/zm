package com.zm.service.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

public class MyWxPayConfig extends WXPayConfig{

	private static final String appid = "wx27274648aadcf410";
	private static final String merchantid = "1525781591";
	private static final String merchant_secret = "fjlsjdlfp28301231j203812031jp203";
	private static final String certPath = "apiclient_cert.p12";
	private byte[] certData = null;
	
	public MyWxPayConfig() throws Exception{
		if(!certPath.isEmpty()){
			//File file = ResourceUtils.getFile(certPath);
	    	//InputStream certStream = new FileInputStream(file);
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			System.out.println("classpath===>"+loader.getResource("").getPath());
			
	    	ClassPathResource res = new ClassPathResource(certPath);
	    	System.out.println("cerpath===>"+res.getPath());
			InputStream certStream = res.getInputStream();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = certStream.read(buffer))!=-1) {
				baos.write(buffer, 0, len);
			}
			certData = baos.toByteArray();
			baos.close();
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
