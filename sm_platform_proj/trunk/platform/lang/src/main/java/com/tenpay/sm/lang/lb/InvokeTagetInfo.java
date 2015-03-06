package com.tenpay.sm.lang.lb;

import java.io.Serializable;
import java.util.Comparator;


/**
 * 调用目标信息
 * @author aixxia
 *
 */
public class InvokeTagetInfo implements Comparator<InvokeTagetInfo>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 调用目标
	 */
	private Object target;
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * 优先级
	 */
	private int priority;
	
	/**
	 * 是否是死链
	 */
	private boolean deadOne = false;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(url != null && obj != null && (obj instanceof InvokeTagetInfo)){
			return url.equals(((InvokeTagetInfo)obj).url);
		}else{
			return super.equals(obj);
		}
	}
	
	public InvokeTagetInfo(Object target,String url){
		this.target = target;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean isDeadOne() {
		return deadOne;
	}

	public void setDeadOne(boolean deadOne) {
		this.deadOne = deadOne;
	}

	public int compare(InvokeTagetInfo o1, InvokeTagetInfo o2) {
		return o1.priority - o2.priority;
	}
	
	@Override
	public String toString() {
		return url;
	}
	
}
