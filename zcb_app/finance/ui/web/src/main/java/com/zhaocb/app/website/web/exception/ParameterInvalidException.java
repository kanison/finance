package com.zhaocb.app.website.web.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class ParameterInvalidException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	
	public static final String SYSTEM_ERROR="20210000";//系统错误
	public static final String UNEXPECTED_ERR="20210001";  // 不期望出现的异常，日志中最好打出期望的是什么，本次出现的是什么
	
	
	
	public static final String APP_ERROR="20220000";//业务错误
	public static final String APP_CREDIT_QUOTO_NOT_ENOUGH="20220001";//商户可用额度不足
	public static final String SP_CANNOT_USE="20220002";//商户不可用
	
	public ParameterInvalidException(String msg) {
		super(msg);
	}

	public ParameterInvalidException(String msg, Exception e) {
		super(msg, e);
	}

	public ParameterInvalidException(Exception e) {
		super(e);
	}
	
	public ParameterInvalidException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}