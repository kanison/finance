/**
 * 
 */
package com.tenpay.sm.lang.error;

import java.io.Serializable;

/**
 * @author torryhong
 * 
 */
public class ErrorCodeException extends RuntimeException implements
		Serializable {
	private static final long serialVersionUID = -1178568153654761967L;
	private ErrorCode errorCode;

	/**
	 * 
	 */
	public ErrorCodeException() {
		this.errorCode = new ErrorCode();
		this.errorCode.setRetcode("ErrorCodeException");
	}

	/**
	 * @param message
	 */
	public ErrorCodeException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	/**
	 * @param cause
	 */
	public ErrorCodeException(Throwable cause) {
		super(cause);
		this.errorCode = new ErrorCode();
		this.errorCode.setRetcode(cause.getClass().getSimpleName());
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ErrorCodeException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(String retcode, String retmsg) {
		super(retmsg);
		this.errorCode = new ErrorCode();
		this.errorCode.setRetcode(retcode);
		this.errorCode.setCode(this.getClass().getSimpleName());
		this.errorCode.setMessage(retmsg);
	}

	public ErrorCodeException(String msg) {
		super(msg);
		this.errorCode = new ErrorCode();
		this.errorCode.setCode(this.getClass().getSimpleName());
		this.errorCode.setMessage(msg);
	}

	public ErrorCodeException(String msg, Throwable cause) {
		super(msg, cause);
		this.errorCode = new ErrorCode();
		this.errorCode.setCode(cause.getClass().getSimpleName());
		this.errorCode.setMessage(msg);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
