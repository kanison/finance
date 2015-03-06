/**
 * 
 */
package com.tenpay.sm.web.mashup;

import com.tenpay.sm.client.resource.TagResource;

/**
 * @author li.hongtl
 *
 */
public interface TagResourceHandler {
	
	public TagResourceHandlerResultEnum doStartTag(TagResource tagResource, TagResourceContext tagResourceContext);
	
	public TagResourceHandlerResultEnum doAfterBody(TagResource tagResource, TagResourceContext tagResourceContext);
	
	public TagResourceHandlerResultEnum doEndTag(TagResource tagResource, TagResourceContext tagResourceContext);
}
