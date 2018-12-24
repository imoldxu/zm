package com.zm.service.context;

public class HandleException extends RuntimeException{

	private static final long serialVersionUID = -7604792672088123562L;
	
	private int errorCode;
	
	public HandleException(int errorCode, String msg){
		super(msg);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
