package com.zhaocb.zcb_app.finance.service.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class FundServiceRetException  extends ErrorCodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660797621140148970L;
	public static final String SYSTEM_ERROR="20010000";//ÏµÍ³´íÎó
	
	public FundServiceRetException(String msg) {
		super(msg);
	}

	public FundServiceRetException(String msg, Exception e) {
		super(msg, e);
	}

	public FundServiceRetException(Exception e) {
		super(e);
	}
	
	public FundServiceRetException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
