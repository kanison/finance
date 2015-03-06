/**
 * 
 */
package com.tenpay.sm.web.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

/**
 * 抽象Page 有 多个 module
 * @author li.hongtl
 *
 */
public class AbstractPage implements Page {
	private Map<String,Object> modules = new LinkedMap();

	public Map<String, Object> getModules() {
		return modules;
	}

	public void setModules(Map<String, Object> modules) {
		this.modules = modules;
	}
	
	
}
