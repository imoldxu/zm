package com.zm.service.context;

public class ErrorCode {
	
	/**
	 * showdoc
	 * @catalog 接口文档/全局错误码
	 * @title 全局错误码
	 * @remark 1 成功
	 */
	public static final int OK = 1;
	
	public static final int NORMAL_ERROR = 2;
	
	public static final int SYSTEM_ERROR = 3;
	
	public static final int SESSION_ERROR = 4;
	
	public static final int LOGIN_ERROR = 5;

	public static final int ARG_ERROR = 6;

	public static final int MODULE_ERROR = 7;

	public static final int RECOMMIT_ERROR = 8;

	public static final int DATA_ERROR = 9; //内部数据异常

	public static final int LOCK_ERROR = 10; //锁异常

	public static final int WX_NET_ERROR = 11;

	public static final int WX_CRYPTO_ERROR = 12;

	public static final int WX_ERROR = 13;
	
	public static final int DOMAIN_ERROR = 14; //权限错误


}
