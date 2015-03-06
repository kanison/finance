package com.tenpay.sm.lang.lb;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * ���ؾ�����Ե�Ĭ��ʵ�� 1��ʹ��blockingQueue�Ѽ����ý�� 2����������̻߳��ܵ��ý�� 3��check����̼߳��Ŀ������ ������ͳ�ƽ��
 * 4�������ض��㷨�ж���ѡ����
 * 
 * @author aixxia
 * 
 */
public class DefaultLoadbalanceStrategy implements LoadbalanceStrategy {
	private final Random rnd = new Random();
	private static final Logger LOG = Logger
			.getLogger(DefaultLoadbalanceStrategy.class);
	private InvokeTagetInfo activeHotBackupTarget = null;
	/**
	 * ����Ŀ���б� ��ʼ������nullpoint
	 */
	private List<Object> targets = new ArrayList<Object>(0);

	private List<Object> hotBackupTargetsWithSwitchBack = new ArrayList<Object>(
			0);
	private List<Object> hotBackupTargets = new ArrayList<Object>(0);

	/**
	 * ����Ŀ���б� ��ʼ������nullpoint
	 */
	private List<Object> aliveTargets = new ArrayList<Object>(0);

	/**
	 * Ĭ��̽��ʱ����
	 */
	private int checkAliveInterval = 60000;

	/**
	 * ̽�ⳬʱ
	 */
	private int checkAliveTimeout = 5000;

	public void init() {
		DeamonThreadCheckAlive deamonThreadCheckAlive = new DeamonThreadCheckAlive();
		deamonThreadCheckAlive.start();
	}

	public InvokeTagetInfo getOne(InvokeTagetInfo tag) {
		// �رո��ؾ���
		if (targets.size() == 1 && hotBackupTargetsWithSwitchBack.size() == 0
				&& hotBackupTargets.size() == 0)
			return (InvokeTagetInfo) targets.get(0);
		if (activeHotBackupTarget != null)
			return activeHotBackupTarget;
		// ��֤�̰߳�ȫ��
		List<Object> tmpList = new ArrayList<Object>();
		// ��ȡ����
		tmpList.addAll(aliveTargets);
		// �޳�֮ǰ�ù����Ǹ�
		if (tag != null)
			tmpList.remove(tag);
		if (tmpList == null || tmpList.isEmpty()) {
			// û�п���tagʱ����ȫ���б���ĳһ��
			return (InvokeTagetInfo) targets.get(rnd.nextInt(targets.size()));
		}
		// ��ʵ�� ���ȡһ��
		return (InvokeTagetInfo) tmpList.get(rnd.nextInt(tmpList.size()));
	}

	public int getRetryTimes() {
		return 2;
	}

	public void setTargets(List<Object> targets) {
		this.targets = targets;
	}

	public void uploadInvokeResult(InvokeResult res) {
	}

	protected final byte[] buffer = new byte[8192];

	/**
	 * ��֤�����Ƿ���� �ɼ̳���д
	 * 
	 * @param target
	 * @return
	 */
	protected boolean checkTargetAlive(Object target) {
		URL url = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(((InvokeTagetInfo) target).getUrl());
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(checkAliveTimeout);
			connection.setReadTimeout(checkAliveTimeout);
			connection.addRequestProperty("Connection", "Close");
			connection.setAllowUserInteraction(false);
			connection.connect();
			is = connection.getInputStream();
			is.read(buffer);
			return true;
		} catch (Throwable e) {
			LOG.warn("check url fail...:" + url + e);
			return false;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Throwable e2) {
				}
			if (connection != null)
				try {
					connection.disconnect();
				} catch (Exception e2) {
				}
		}
	}

	/**
	 * tag��Լ���߳� ���������������
	 * 
	 * @author aixxia
	 * 
	 */
	private class DeamonThreadCheckAlive extends Thread {
		public DeamonThreadCheckAlive() {
			this.setDaemon(true);
		}

		@Override
		public void run() {
			do {
				try {
					doRun();
				} catch (Throwable e) {
					LOG.warn("check alive fail...", e);
				}
				try {
					Thread.sleep(checkAliveInterval);
				} catch (InterruptedException e) {
				}
			} while (true);
		}

		private void checkNormalTargets(List<Object> tmpTags) {
			for (Object tmpTag : targets) {// ���ȷ��
				try {
					if (LOG.isDebugEnabled()) {
						LOG.debug("check url " + tmpTag);
					}
					if (checkTargetAlive(tmpTag)) {// �ж��Ƿ���ţ�������ż��뵽�Ŀ��
						tmpTags.add(tmpTag);
					}
				} catch (Exception e) {
					LOG.warn("check alive fail..." + tmpTag.toString(), e);
				}
			}
			if (tmpTags.size() == 0) {
				// ����ȫ���ˣ������лر���
				for (Object tmpTag : hotBackupTargetsWithSwitchBack) {// ���ȷ��
					try {
						if (LOG.isDebugEnabled()) {
							LOG.debug("check H url " + tmpTag);
						}
						if (checkTargetAlive(tmpTag)) {// �ж��Ƿ���ţ�������ż��뵽�Ŀ��
							tmpTags.add(tmpTag);
						}
					} catch (Exception e) {
						LOG
								.warn("check H alive fail..."
										+ tmpTag.toString(), e);
					}
				}
			}
		}

		public void doRun() {
			// �رո��ؾ���
			if (targets.size() <= 1
					&& hotBackupTargetsWithSwitchBack.size() == 0
					&& hotBackupTargets.size() == 0)
				return;
			List<Object> tmpTags = new ArrayList<Object>();
			if (activeHotBackupTarget == null) {
				checkNormalTargets(tmpTags);
				if (tmpTags.size() == 0)
					// ȫ���ˣ�����ȷ��
					checkNormalTargets(tmpTags);
				// ���� �̰߳�ȫ
				aliveTargets = tmpTags;
			}
			if (activeHotBackupTarget != null
					|| (tmpTags.size() == 0 && aliveTargets.size() == 0)) {
				// ���������лر��������ˣ���鲻���лر���
				// �����лأ������
				int index = 0;
				if (activeHotBackupTarget != null)
					index = hotBackupTargets.indexOf(activeHotBackupTarget);
				for (; index < hotBackupTargets.size(); index++) {
					Object tmpTag = hotBackupTargets.get(index);
					// ���ȷ��
					try {
						if (LOG.isDebugEnabled()) {
							LOG.debug("check B url " + tmpTag);
						}
						if (checkTargetAlive(tmpTag)) {
							if (activeHotBackupTarget != null
									|| aliveTargets.size() == 0)
								activeHotBackupTarget = (InvokeTagetInfo) tmpTag;
							break;
						}
					} catch (Exception e) {
						LOG
								.warn("check B alive fail..."
										+ tmpTag.toString(), e);
					}
				}
			}
		}
	}

	/**
	 * ���ý������߳�
	 * 
	 * @author aixxia
	 * 
	 */

	public void setCheckAliveInterval(int checkAliveInterval) {
		this.checkAliveInterval = checkAliveInterval;
	}

	public void setCheckAliveTimeout(int checkAliveTimeout) {
		this.checkAliveTimeout = checkAliveTimeout;
	}

	public List<Object> getHotBackupTargetsWithSwitchBack() {
		return hotBackupTargetsWithSwitchBack;
	}

	public void setHotBackupTargetsWithSwitchBack(
			List<Object> hotBackupTargetsWithSwitchBack) {
		this.hotBackupTargetsWithSwitchBack = hotBackupTargetsWithSwitchBack;
	}

	public List<Object> getHotBackupTargets() {
		return hotBackupTargets;
	}

	public void setHotBackupTargets(List<Object> hotBackupTargets) {
		this.hotBackupTargets = hotBackupTargets;
	}
}
