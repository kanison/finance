package com.zhaocb.app.website.web.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class WebServiceRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="20210000";//ϵͳ����
	public static final String UNEXPECTED_ERR="20210001";  // ���������ֵ��쳣����־����ô����������ʲô�����γ��ֵ���ʲô
	
	
	
	public static final String APP_ERROR="20220000";//ҵ�����
	public static final String APP_CREDIT_QUOTO_NOT_ENOUGH="20220001";//�̻����ö�Ȳ���
	public static final String SP_CANNOT_USE="20220002";//�̻�������
	
	public static final String USER_REG_ERROR="30000001";//�û��Ѵ���
	
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