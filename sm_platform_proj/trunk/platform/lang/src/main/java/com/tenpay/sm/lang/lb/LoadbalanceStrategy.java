package com.tenpay.sm.lang.lb;

import java.util.List;

/**
 * ���ؾ�����Խӿ�
 * 
 * @author aixxia
 * 
 */
public interface LoadbalanceStrategy {

	/**
	 * ���õ���Ŀ��
	 * 
	 * @param targets
	 */
	public void setTargets(List<Object> targets);

	/**
	 * �����Զ��л��ȱ�Ŀ��
	 * 
	 * @param targets
	 */
	public void setHotBackupTargetsWithSwitchBack(List<Object> targets);

	/**
	 * �����ȱ�Ŀ��
	 * 
	 * @param targets
	 */
	public void setHotBackupTargets(List<Object> targets);

	/**
	 * ��ȡ����Ŀ��
	 * 
	 * @param tag
	 *            ����ֵ�����ڴ˲��������ڶ��ε��ã�����õ�֮ǰ�����tag ����˹��ܴ�null
	 * @return
	 */
	public InvokeTagetInfo getOne(InvokeTagetInfo tag);

	/**
	 * �ϱ����ý��
	 * 
	 * @param res
	 */
	public void uploadInvokeResult(InvokeResult res);

	/**
	 * ��ȡʧ�����Դ���
	 */
	public int getRetryTimes();

	/**
	 * ����̽���Լ�� ��λms
	 */
	public void setCheckAliveInterval(int checkAliveInterval);

	/**
	 * ����̽�ⳬʱʱ�� ��λms
	 */
	public void setCheckAliveTimeout(int checkAliveTimeout);

	/**
	 * ��ʼ��
	 */
	public void init();
}
