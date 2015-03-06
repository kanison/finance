/**
 * 
 */
package com.tenpay.sm.client.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author li.hongtl
 *
 */
public class TagResource implements Serializable {
	private static final long serialVersionUID = -5847458740006798149L;
	private String tagPrefix;
	private String tagName;
	
	private String expressionContent;
	private Map<String,String> attributes = new HashMap<String,String>();
	private List<TagResource> subContent = new ArrayList<TagResource>();
	
	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	/**
	 * @return the subContent
	 */
	public List<TagResource> getSubContent() {
		return subContent;
	}
	/**
	 * @param subContent the subContent to set
	 */
	public void setSubContent(List<TagResource> subContent) {
		this.subContent = subContent;
	}
	/**
	 * @return the expressionContent
	 */
	public String getExpressionContent() {
		return expressionContent;
	}
	/**
	 * @param expressionContent the expressionContent to set
	 */
	public void setExpressionContent(String elContent) {
		this.expressionContent = elContent;
	}
	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}
	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	/**
	 * @return the tagPrefix
	 */
	public String getTagPrefix() {
		return tagPrefix;
	}
	/**
	 * @param tagPrefix the tagPrefix to set
	 */
	public void setTagPrefix(String tagPrefix) {
		this.tagPrefix = tagPrefix;
	}


	
	
	
}
