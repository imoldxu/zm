package com.zm.service.context;

public class ErrorCode {
	
	public static final int OK = 1;
	
	public static final int SYSTEM_ERROR = 13;
	
	public static final int NORMAL_ERROR = 2;
	
	public static final int LOGIN_ERROR = 3;
	
	public static final int SESSION_ERROR = 4;

	public static final int ARG_ERROR = 5;

	public static final int MODULE_ERROR = 9;

	public static final int RECOMMIT_ERROR = 10;

	public static final int DATA_ERROR = 11; //内部数据异常

	public static final int LOCK_ERROR = 12; //锁异常

	public static final int WX_NET_ERROR = 13;

	public static final int WX_CRYPTO_ERROR = 14;

	public static final int WX_ERROR = 15;
	
	public static final int DOMAIN_ERROR = 16; //权限错误


}
