package com.tenpay.sm.lang.lb;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * 负载均衡策略的默认实现 1、使用blockingQueue搜集调用结果 2、分析监控线程汇总调用结果 3、check监控线程监控目标存活性 并整理统计结果
 * 4、根据特定算法判断挑选条件
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
	 * 调用目标列表 初始化避免nullpoint
	 */
	private List<Object> targets = new ArrayList<Object>(0);

	private List<Object> hotBackupTargetsWithSwitchBack = new ArrayList<Object>(
			0);
	private List<Object> hotBackupTargets = new ArrayList<Object>(0);

	/**
	 * 可用目标列表 初始化避免nullpoint
	 */
	private List<Object> aliveTargets = new ArrayList<Object>(0);

	/**
	 * 默认探测时间间隔
	 */
	private int checkAliveInterval = 60000;

	/**
	 * 探测超时
	 */
	private int checkAliveTimeout = 5000;

	public void init() {
		DeamonThreadCheckAlive deamonThreadCheckAlive = new DeamonThreadCheckAlive();
		deamonThreadCheckAlive.start();
	}

	public InvokeTagetInfo getOne(InvokeTagetInfo tag) {
		// 关闭负载均衡
		if (targets.size() == 1 && hotBackupTargetsWithSwitchBack.size() == 0
				&& hotBackupTargets.size() == 0)
			return (InvokeTagetInfo) targets.get(0);
		if (activeHotBackupTarget != null)
			return activeHotBackupTarget;
		// 保证线程安全性
		List<Object> tmpList = new ArrayList<Object>();
		// 获取快照
		tmpList.addAll(aliveTargets);
		// 剔除之前用过的那个
		if (tag != null)
			tmpList.remove(tag);
		if (tmpList == null || tmpList.isEmpty()) {
			// 没有可用tag时返回全部列表中某一个
			return (InvokeTagetInfo) targets.get(rnd.nextInt(targets.size()));
		}
		// 简单实现 随机取一个
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
	 * 验证服务是否活着 可继承重写
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
	 * tag活动性监控线程 并按调用情况排序
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
			for (Object tmpTag : targets) {// 逐个确认
				try {
					if (LOG.isDebugEnabled()) {
						LOG.debug("check url " + tmpTag);
					}
					if (checkTargetAlive(tmpTag)) {// 判断是否活着，如果活着加入到活动目标
						tmpTags.add(tmpTag);
					}
				} catch (Exception e) {
					LOG.warn("check alive fail..." + tmpTag.toString(), e);
				}
			}
			if (tmpTags.size() == 0) {
				// 主机全死了，检查可切回备机
				for (Object tmpTag : hotBackupTargetsWithSwitchBack) {// 逐个确认
					try {
						if (LOG.isDebugEnabled()) {
							LOG.debug("check H url " + tmpTag);
						}
						if (checkTargetAlive(tmpTag)) {// 判断是否活着，如果活着加入到活动目标
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
			// 关闭负载均衡
			if (targets.size() <= 1
					&& hotBackupTargetsWithSwitchBack.size() == 0
					&& hotBackupTargets.size() == 0)
				return;
			List<Object> tmpTags = new ArrayList<Object>();
			if (activeHotBackupTarget == null) {
				checkNormalTargets(tmpTags);
				if (tmpTags.size() == 0)
					// 全死了？二次确认
					checkNormalTargets(tmpTags);
				// 操作 线程安全
				aliveTargets = tmpTags;
			}
			if (activeHotBackupTarget != null
					|| (tmpTags.size() == 0 && aliveTargets.size() == 0)) {
				// 主机、可切回备机都死了，检查不可切回备机
				// 不可切回，需谨慎
				int index = 0;
				if (activeHotBackupTarget != null)
					index = hotBackupTargets.indexOf(activeHotBackupTarget);
				for (; index < hotBackupTargets.size(); index++) {
					Object tmpTag = hotBackupTargets.get(index);
					// 逐个确认
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
	 * 调用结果监控线程
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
