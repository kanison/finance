/**
 * 
 */
package com.tenpay.sm.web.mashup;

import com.tenpay.sm.client.resource.TagResource;

/**
 * @author li.hongtl
 *
 */
public interface TagResourceEngine {
	public String resolve(TagResource tagResource,Object expressionTarget) throws TagResourceException;
}
