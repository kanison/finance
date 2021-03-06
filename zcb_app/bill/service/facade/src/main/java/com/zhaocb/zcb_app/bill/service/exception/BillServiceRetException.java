package com.zhaocb.zcb_app.bill.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class BillServiceRetException extends ErrorCodeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5296524740798554632L;
	
	public static final String SYSTEM_ERROR = "20110000";// 系统错误
	public static final String APP_ERROR = "20120000";// 系统错误
	public static final String SEQ_LIMIT_ERROR = "30000000";// 最大单号限制为6亿
	public static final String MAC_NO_ERROR = "30000001";// 最大单号限制为6亿

	public BillServiceRetException(String msg) {
		super(msg);
	}

	public BillServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public BillServiceRetException(Exception e) {
		super(e);
	}

	public BillServiceRetException(String retcode, String retmsg) {

		super(retcode, retmsg);
	}
}
