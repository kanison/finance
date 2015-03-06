/**
 * 
 */
package com.tenpay.sm.web.mashup;

import java.util.Map;

/**
 * @author li.hongtl
 *
 */
public interface MashupContentGetFacade {
	String DEFAULT_PARAM_KEY = "_mcid";
	Map<String,Object> getContent(String id);
	Map<String,Object> getDefaultContent();
	String resolveContent(Object content);
}
