package com.zhaocb.zcb_app.finance.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class FinanceServiceRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="20110000";//系统错误
	public static final String APP_ERROR="20120000";//系统错误
	public FinanceServiceRetException(String msg) {
		super(msg);
	}

	public FinanceServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public FinanceServiceRetException(Exception e) {
		super(e);
	}
	
	public FinanceServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
