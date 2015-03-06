/**
 * 
 */
package com.tenpay.sm.web.resubmit;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.collections.map.LRUMap;

/**
 * 服务器端防止多个token的一个环，基于LRU淘汰
 * @author li.hongtl
 *
 */
public class FormTokenCircle implements Serializable {
	public static final int DEFAULT_CIRCLE_LENGTH = 5;
	private static final long serialVersionUID = -5452787744450959937L;
	
	public FormTokenCircle() {
		this(DEFAULT_CIRCLE_LENGTH);
	}
	public FormTokenCircle(int size) {
		tokens = new LRUMap(size);
	}
	
	private LRUMap tokens;
	
	synchronized public String createToken() {
		String token = UUID.randomUUID().toString();
		tokens.put(token, null);
		return token;
	}
	
	public boolean isValid(String token) {
		return isValid(token,true);
	}
	
	synchronized public boolean isValid(String token, boolean voidToken) {
		boolean valid = this.tokens.containsKey(token);
		if(valid && voidToken) {
			this.voidToken(token);
		}
		return valid;
	}
	
	synchronized public void voidToken(String token) {
		this.tokens.remove(token);
	}

}
