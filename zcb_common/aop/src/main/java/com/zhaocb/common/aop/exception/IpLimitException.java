package com.zhaocb.common.aop.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class IpLimitException extends ErrorCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8324528280455478707L;

	public IpLimitException(String msg) {
		super(msg);
	}

	public IpLimitException(String msg, Exception e) {
		super(msg, e);
	}

	public IpLimitException(Exception e) {
		super(e);
	}
}
