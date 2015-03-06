package com.tenpay.sm.lang.health.vm;


/**
 * 
 * @author eniacli
 *
 */
public class MemoryStatus {
	private Long freeMemory;
	private Long maxMemory;
	private Long totalMemory;
	
	public Long getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}
	public Long getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}
	public Long getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}
}
