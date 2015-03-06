package com.tenpay.sm.lang.lb;

import java.util.List;

/**
 * 负载均衡策略接口
 * 
 * @author aixxia
 * 
 */
public interface LoadbalanceStrategy {

	/**
	 * 设置调用目标
	 * 
	 * @param targets
	 */
	public void setTargets(List<Object> targets);

	/**
	 * 设置自动切回热备目标
	 * 
	 * @param targets
	 */
	public void setHotBackupTargetsWithSwitchBack(List<Object> targets);

	/**
	 * 设置热备目标
	 * 
	 * @param targets
	 */
	public void setHotBackupTargets(List<Object> targets);

	/**
	 * 获取调用目标
	 * 
	 * @param tag
	 *            返回值不等于此参数，用于二次调用，避免得到之前错误的tag 无需此功能传null
	 * @return
	 */
	public InvokeTagetInfo getOne(InvokeTagetInfo tag);

	/**
	 * 上报调用结果
	 * 
	 * @param res
	 */
	public void uploadInvokeResult(InvokeResult res);

	/**
	 * 获取失败重试次数
	 */
	public int getRetryTimes();

	/**
	 * 设置探测活动性间隔 单位ms
	 */
	public void setCheckAliveInterval(int checkAliveInterval);

	/**
	 * 设置探测超时时间 单位ms
	 */
	public void setCheckAliveTimeout(int checkAliveTimeout);

	/**
	 * 初始化
	 */
	public void init();
}
