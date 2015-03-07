package com.zhaocb.app.website.web.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class WebServiceRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="20210000";//系统错误
	public static final String APP_ERROR="20220000";//系统错误
	public WebServiceRetException(String msg) {
		super(msg);
	}

	public WebServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public WebServiceRetException(Exception e) {
		super(e);
	}
	
	public WebServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}