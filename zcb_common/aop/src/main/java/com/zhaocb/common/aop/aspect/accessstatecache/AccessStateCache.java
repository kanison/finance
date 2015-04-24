package com.zhaocb.common.aop.aspect.accessstatecache;

import java.util.Date;
import java.util.Map;

/**
 * 根据设定维度（key） 记录访问状态
 * 
 * @author aixxia
 * 
 */
public class AccessStateCache {
	private Map<String, CachedState> cache;

	private long auditInterval;

	private long maxAccessTimes;

	/**
	 * 访问最大次数由具体key确定时使用
	 * 
	 * @param cacheSize
	 * @param auditInterval
	 */
	public AccessStateCache(int cacheSize, long auditInterval) {
		cache = new LRUMap<String, CachedState>(cacheSize);
		this.auditInterval = auditInterval;
	}

	/**
	 * 访问最大次数统一时使用
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
	 * 记录访问 访问的最大次数根据具体情况指定 可以根据不同的key指定
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
				// 没有记录状态
				state = new CachedState();
				cache.put(key, state);
				state.setRecodeStartTime(currentTime);
				state.setMaxAccessTimes(maxAccessTimes);
			} else if (currentTime - state.getRecodeStartTime() > auditInterval) {
				// 或者状态超过限定时间
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
	 * 记录访问 访问的最大次数创建对象时候指定 所有key统一
	 * 
	 * @param key
	 * @return
	 */
	public boolean recodeAccess(String key) {
		return recodeAccess(key, maxAccessTimes);
	}
}

/**
 * 缓存状态
 * 
 * @author aixxia
 * 
 */
class CachedState {
	// 相对与1970年的秒数 ms
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
	 * 最大容量
	 */
	private final int maxCapacity;

	/**
	 * 默认的加载因子
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
