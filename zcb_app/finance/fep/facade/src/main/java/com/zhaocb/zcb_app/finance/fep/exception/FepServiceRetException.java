package com.zhaocb.zcb_app.finance.fep.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class FepServiceRetException extends ErrorCodeException {

	private static final long serialVersionUID = -5296524740798554632L;

	public static final String SYSTEM_ERROR = "20110001";// ϵͳ����
	
	public static final String CFT_ERROR = "20110002";// �Ƹ�ͨ�����쳣
	
	public static final String CERT_ERROR = "20110003";// �Ƹ�ͨ�����쳣

	public FepServiceRetException(String msg) {
		super(msg);
	}

	public FepServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public FepServiceRetException(Exception e) {
		super(e);
	}

	public FepServiceRetException(String retcode, String retmsg) {

		super(retcode, retmsg);
	}
}
