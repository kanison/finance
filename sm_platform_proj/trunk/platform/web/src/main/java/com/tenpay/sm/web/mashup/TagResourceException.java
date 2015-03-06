/**
 * 
 */
package com.tenpay.sm.web.mashup;

/**
 * @author li.hongtl
 *
 */
public class TagResourceException extends RuntimeException {
	private static final long serialVersionUID = 7102641661188620547L;

	/**
	 * 
	 */
	public TagResourceException() {
	}

	/**
	 * @param arg0
	 */
	public TagResourceException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public TagResourceException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TagResourceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
