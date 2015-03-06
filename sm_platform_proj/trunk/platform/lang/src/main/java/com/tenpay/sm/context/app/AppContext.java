package com.tenpay.sm.context.app;

import java.util.*;

import com.tenpay.sm.context.AbstractContext;
import com.tenpay.sm.context.conversation.Conversation;

/**
 * 
 * <p>
 * Title: 
 * </p>
 * <p>
 * Description: xx框架
 * 客户端应用程序的Context实现，用于Junit等环境，可以虚拟设置session,application作用域等属性
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: 洪桃李
 * </p>
 * 
 * @author 洪桃李
 * @version 1.0
 */
public class AppContext extends AbstractContext {
	protected final Map<String,Object> currentAttributes = new HashMap<String,Object>();
	protected final Map<String,Object> sessionAttributes = new HashMap<String,Object>();
	private static final Map<String,Object> applicationAttributes = new HashMap<String,Object>();
	protected Map<String,String[]> requestParams = new HashMap<String,String[]>();
	
	public Object getCurrentAttribute(String key) {
		return currentAttributes.get(key);
	}

	public void setCurrentAttribute(String key, Object value) {
		currentAttributes.put(key, value);
	}

	public void removeCurrentAttribute(String key) {
		currentAttributes.remove(key);
	}

	public Set getCurrentrAttributeNames() {
		return currentAttributes.keySet();
	}


	public Set getSessionAttributeNames() {
		return sessionAttributes.keySet();
	}

	public void setSessionAttribute(String key, Object value) {
		sessionAttributes.put(key, value);
	}

	public Object getSessionAttribute(String key) {
		return sessionAttributes.get(key);
	}



	public Object getApplicationAttribute(String key) {
		return applicationAttributes.get(key);
	}

	public Set getContextAttributeNames() {
		return applicationAttributes.keySet();
	}

	public void setApplicationAttribute(String key, Object value) {
		applicationAttributes.put(key, value);
	}

	public void removeApplicationAttribute(String key) {
		applicationAttributes.remove(key);
	}

	public void removeSessionAttribute(String key) {
		sessionAttributes.remove(key);
	}

	public Map<String, Object> getCurrentAttributesMap() {
		return this.currentAttributes;
	}

	public String getRequestParameter(String name) {
		String[] arr = this.requestParams.get(name);
		if(arr!=null && arr.length>0) {
			return arr[0];
		}
		return null;
	}

	public String[] getRequestParameterValues(String name) {
		return this.requestParams.get(name);
	}

	public Map<String, String[]> getRequestParamsMap() {
		return this.requestParams;
	}
	
	public void setRequestParamsMap(Map<String, String[]> params) {
		this.requestParams = params;
	}

}
