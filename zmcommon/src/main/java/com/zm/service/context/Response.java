package com.zm.service.context;

public class Response {
	
    public static final String SUCCESS_MSG = "成功";
    
	private int code;

    private Object data;

    private String msg;

    public Response(){
    	this.code = ErrorCode.NORMAL_ERROR;
    	this.data = null;
    	this.msg = "失败";
    }
    
    public Response(int code, Object data, String msg) {
        this.code = code;
        this.setData(data);
        this.msg = msg;
    }

    public Response(int i) {
        this.code = i;
    }


    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Response setMsg(String msg) {
        this.msg = msg;
        return this;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object fetchOKData(){
		if(code ==  ErrorCode.OK){
			return data;
		}else{
			throw new HandleException(code, msg);
		}
	}
	
	public static Response OK(Object object) {
		return new Response(ErrorCode.OK, object, SUCCESS_MSG);
	}
	
	public static Response Error(int code, String msg) {
		return new Response(code, null, msg);
	}

	public static Response SystemError() {
		return new Response(ErrorCode.NORMAL_ERROR, null, "系统异常");
	}

	public static Response NormalError(String msg) {
		return new Response(ErrorCode.NORMAL_ERROR, null, msg);
	}
}
