/**
 * 
 */
package com.tenpay.sm.context.conversation;

/**
 * @author li.hongtl
 * °∞∂‘ª∞°±“Ï≥£
 */
public class ConversationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7178187769541332415L;

	/**
	 * 
	 */
	public ConversationException() {
	}

	/**
	 * @param message
	 */
	public ConversationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConversationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConversationException(String message, Throwable cause) {
		super(message, cause);
	}

}
