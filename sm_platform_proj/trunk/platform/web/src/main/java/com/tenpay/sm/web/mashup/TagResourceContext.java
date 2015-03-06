/**
 * 
 */
package com.tenpay.sm.web.mashup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li.hongtl
 *
 */
public class TagResourceContext {
	private Map<String,Object> attributes = new HashMap<String,Object>();
	private StringBuffer content = new StringBuffer();
	private Object expressionTarget;
	
	public String getResultContent() {
		return content.toString();
	}
	public void appendResultContent(String resultContent) {
		content.append(resultContent);
	}
	/**
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	/**
	 * @return the expressionTarget
	 */
	public Object getExpressionTarget() {
		return expressionTarget;
	}
	/**
	 * @param expressionTarget the expressionTarget to set
	 */
	public void setExpressionTarget(Object expressionTarget) {
		this.expressionTarget = expressionTarget;
	}
	
	
}
