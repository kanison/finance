package com.tenpay.sm.lang.lb;

/**
 * ���ý����Ϣ
 * @author aixxia
 * 
 */
public class InvokeResult {

	/**
	 * ����Ŀ��
	 */
	private Object target;
	
	/**
	 * �Ƿ�ɹ�
	 */
	private boolean success;
	
	/**
	 * ��ʱ
	 */
	private long timeCost;
	
	/**
	 * �쳣
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
