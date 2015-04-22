package com.zhaocb.common.aop.exception;

import com.tenpay.sm.lang.error.ErrorCodeException;

public class AopException extends ErrorCodeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 441441517340852065L;

	public AopException(String msg) {
		super(msg);
	}

	public AopException(String msg, Throwable e) {
		super(msg, e);
	}

	public AopException(Throwable e) {
		super(e);
	}

	public AopException(String retcode, String retmsg) {
		super(retcode, retmsg);
	}
}
