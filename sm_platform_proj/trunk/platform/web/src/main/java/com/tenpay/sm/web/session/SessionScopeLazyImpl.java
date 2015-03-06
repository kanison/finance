/**
 * 
 */
package com.tenpay.sm.web.session;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;

/**
 * @author torryhong
 *
 */
public class SessionScopeLazyImpl extends SessionScope {

	public boolean containsKey(Object key) {
		return get(key)!=null;
	}

	public Object get(Object key) {
		Context wc = ContextUtil.getContext();
		return wc.getSessionAttribute(key.toString());
	}
	
}
