package com.app.common.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class CommonException  extends ErrorCodeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 581278450530305167L;
	public static final String SYSTEM_ERROR="10010000";//ϵͳ����
	public static final String PARSE_PARAM_ERROR="10010001";//���������쳣
	
	public static final String SHARE_EDIT_APK_ERROR="10020001";//����༭apk����idʧ��
	
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
