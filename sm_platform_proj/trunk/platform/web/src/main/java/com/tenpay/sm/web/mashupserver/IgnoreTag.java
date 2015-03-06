/**
 * 
 */
package com.tenpay.sm.web.mashupserver;

import com.tenpay.sm.client.resource.TagResource;
import com.tenpay.sm.web.mashup.TagResourceContext;
import com.tenpay.sm.web.mashup.TagResourceHandler;
import com.tenpay.sm.web.mashup.TagResourceHandlerResultEnum;

/**
 * @author li.hongtl
 *
 */
public class IgnoreTag implements TagResourceHandler {

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.mashup.TagResourceHandler#doAfterBody(com.tenpay.sm.client.resource.TagResource, com.tenpay.sm.web.mashup.TagResourceContext)
	 */
	public TagResourceHandlerResultEnum doAfterBody(TagResource tagResource,
			TagResourceContext tagResourceContext) {
		return TagResourceHandlerResultEnum.SKIP;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.mashup.TagResourceHandler#doEndTag(com.tenpay.sm.client.resource.TagResource, com.tenpay.sm.web.mashup.TagResourceContext)
	 */
	public TagResourceHandlerResultEnum doEndTag(TagResource tagResource,
			TagResourceContext tagResourceContext) {
		return TagResourceHandlerResultEnum.SKIP;
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.mashup.TagResourceHandler#doStartTag(com.tenpay.sm.client.resource.TagResource, com.tenpay.sm.web.mashup.TagResourceContext)
	 */
	public TagResourceHandlerResultEnum doStartTag(TagResource tagResource,
			TagResourceContext tagResourceContext) {
		return TagResourceHandlerResultEnum.SKIP;
	}

}
