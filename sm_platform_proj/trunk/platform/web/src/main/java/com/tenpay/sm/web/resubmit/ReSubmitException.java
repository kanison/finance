/**
 * 
 */
package com.tenpay.sm.web.resubmit;

/**
 * 表单重复提交异常
 * @author li.hongtl
 *
 */
public class ReSubmitException extends RuntimeException {
	private static final long serialVersionUID = 7304552018962487170L;
	public ReSubmitException() {
		
	}
	public ReSubmitException(String message) {
		super(message);
	}
}
