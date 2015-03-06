package com.tenpay.sm.lang.lb;

/**
 * 调用结果信息
 * @author aixxia
 * 
 */
public class InvokeResult {

	/**
	 * 调用目标
	 */
	private Object target;
	
	/**
	 * 是否成功
	 */
	private boolean success;
	
	/**
	 * 耗时
	 */
	private long timeCost;
	
	/**
	 * 异常
	 */
	private Throwable error;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(long timeCost) {
		this.timeCost = timeCost;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}
	
	
	
	
}
