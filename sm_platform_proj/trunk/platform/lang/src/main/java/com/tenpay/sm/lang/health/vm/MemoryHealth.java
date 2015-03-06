/**
 * 
 */
package com.tenpay.sm.lang.health.vm;

import com.tenpay.sm.lang.health.Health;

/**
 * @author eniacli,torryhong
 *
 */
public class MemoryHealth implements Health {
	private Runtime runtime = Runtime.getRuntime();
	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.health.Health#check()
	 */
	public Object check() {
		MemoryStatus memoryStatus = new MemoryStatus();
		memoryStatus.setFreeMemory(runtime.freeMemory());
		memoryStatus.setMaxMemory(runtime.maxMemory());
		memoryStatus.setTotalMemory(runtime.totalMemory());
		return memoryStatus;
	}

}
