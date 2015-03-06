package com.app.common.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class CommonException  extends ErrorCodeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 581278450530305167L;
	public static final String SYSTEM_ERROR="10010000";//系统错误
	public static final String PARSE_PARAM_ERROR="10010001";//解析参数异常
	
	public static final String SHARE_EDIT_APK_ERROR="10020001";//分享编辑apk分享id失败
	
	public CommonException(String msg) {
		super(msg);
	}

	public CommonException(String msg, Exception e) {
		super(msg, e);
	}

	public CommonException(Exception e) {
		super(e);
	}
	
	public CommonException(String retcode, String retmsg) {
		super(retcode, retmsg);
	}
}
