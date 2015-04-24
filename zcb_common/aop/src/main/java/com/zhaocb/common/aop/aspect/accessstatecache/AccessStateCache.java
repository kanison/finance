package com.zhaocb.common.aop.aspect.accessstatecache;

import java.util.Date;
import java.util.Map;

/**
 * �����趨ά�ȣ�key�� ��¼����״̬
 * 
 * @author aixxia
 * 
 */
public class AccessStateCache {
	private Map<String, CachedState> cache;

	private long auditInterval;

	private long maxAccessTimes;

	/**
	 * �����������ɾ���keyȷ��ʱʹ��
	 * 
	 * @param cacheSize
	 * @param auditInterval
	 */
	public AccessStateCache(int cacheSize, long auditInterval) {
		cache = new LRUMap<String, CachedState>(cacheSize);
		this.auditInterval = auditInterval;
	}

	/**
	 * ����������ͳһʱʹ��
	 * 
	 * @param cacheSize
	 * @param auditInterval
	 * @param maxAccessTimes
	 */
	public AccessStateCache(int cacheSize, long auditInterval,
			long maxAccessTimes) {
		this(cacheSize, auditInterval);
		this.maxAccessTimes = maxAccessTimes;
	}

	/**
	 * ��¼���� ���ʵ����������ݾ������ָ�� ���Ը��ݲ�ͬ��keyָ��
	 * 
	 * @param key
	 * @param maxAccessTimes
	 * @return
	 */
	public boolean recodeAccess(String key, long maxAccessTimes) {
		long currentTime = new Date().getTime();
		synchronized (cache) {
			CachedState state = cache.get(key);
			if (state == null) {
				// û�м�¼״̬
				state = new CachedState();
				cache.put(key, state);
				state.setRecodeStartTime(currentTime);
				state.setMaxAccessTimes(maxAccessTimes);
			} else if (currentTime - state.getRecodeStartTime() > auditInterval) {
				// ����״̬�����޶�ʱ��
				state.setRecodeStartTime(currentTime);
				state.setAccessTimes(1);
			} else if (state.getAccessTimes() >= state.getMaxAccessTimes()) {
				state.setAccessTimes(state.getAccessTimes() + 1);
				return false;
			} else {
				state.setAccessTimes(state.getAccessTimes() + 1);
			}
			return true;
		}
	}

	/**
	 * ��¼���� ���ʵ���������������ʱ��ָ�� ����keyͳһ
	 * 
	 * @param key
	 * @return
	 */
	public boolean recodeAccess(String key) {
		return recodeAccess(key, maxAccessTimes);
	}
}

/**
 * ����״̬
 * 
 * @author aixxia
 * 
 */
class CachedState {
	// �����1970������� ms
	private long recodeStartTime;
	private int accessTimes = 1;
	private long maxAccessTimes;

	public long getRecodeStartTime() {
		return recodeStartTime;
	}

	public void setRecodeStartTime(long recodeStartTime) {
		this.recodeStartTime = recodeStartTime;
	}

	public int getAccessTimes() {
		return accessTimes;
	}

	public void setAccessTimes(int accessTimes) {
		this.accessTimes = accessTimes;
	}

	public long getMaxAccessTimes() {
		return maxAccessTimes;
	}

	public void setMaxAccessTimes(long maxAccessTimes) {
		this.maxAccessTimes = maxAccessTimes;
	}

}

/**
 * lru map
 * 
 * @author aixxia
 * 
 * @param <K>
 * @param <V>
 */
class LRUMap<K, V> extends java.util.LinkedHashMap<K, V> {

	private static final long serialVersionUID = -348656573172586525L;

	/**
	 * �������
	 */
	private final int maxCapacity;

	/**
	 * Ĭ�ϵļ�������
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private Map.Entry<K, V> eldestEntry;

	public LRUMap(int maxCapacity) {
		super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
		this.maxCapacity = maxCapacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		boolean remove = size() > maxCapacity;
		if (remove) {
			this.eldestEntry = eldest;
		}
		return remove;
	}

	public Map.Entry<K, V> getEldestEntry() {
		return eldestEntry;
	}

}
