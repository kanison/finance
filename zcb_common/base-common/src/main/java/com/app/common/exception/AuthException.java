package com.app.common.exception;


import com.tenpay.sm.lang.error.ErrorCodeException;

public class AuthException extends ErrorCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 619525708248035786L;

	public AuthException(String msg) {
		super(msg);
	}

	public AuthException(String msg, Exception e) {
		super(msg, e);
	}

	public AuthException(Exception e) {
		super(e);
	}
}
