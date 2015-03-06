/**
 * 
 */
package com.tenpay.sm.lang.health.vm;

import java.util.Map;
import java.util.TreeMap;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class CpuHealth implements Health {
	private Runtime runtime = Runtime.getRuntime();
	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		Map map = new TreeMap();
		map.put("availableProcessors", runtime.availableProcessors());
		//TODO 获得cpu的load , 执行ps?
		return map;
	}

}
