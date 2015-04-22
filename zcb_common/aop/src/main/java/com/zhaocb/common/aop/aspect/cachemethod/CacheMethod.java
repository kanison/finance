package com.zhaocb.common.aop.aspect.cachemethod;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;

public class CacheMethod implements CacheMethodFacade {
	private Runtime runtime = Runtime.getRuntime();
	private long defaultCacheMs = 600 * 1000;
	private int defaultCacheSize = 20000;
	private int permCacheSize = 200000;
	private Map<String, CachedValueContainer> globalCache;
	private Map<String, Object> permCache;
	private static final Log LOG = LogFactory.getLog(CacheMethod.class);
	private static long lastAccessTime = 0;
	private final static int timeDiff = 60000;
	private CacheMethodFacade asyncCacheMethod;
	private static long nextCleanTime = 0;
	private static long nextCheckMemTime = 0;
	private static long cleanInterval = 600000L;
	private static long checkMemInterval = 20000;
	private static int memLowCount = 0;

	@SuppressWarnings("unchecked")
	public CacheMethod() {
		String defaultCacheMsStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("cacheMethod.defaultCacheMs"));
		if (defaultCacheMsStr != null && defaultCacheMsStr.length() > 0) {
			try {
				defaultCacheMs = Long.parseLong(defaultCacheMsStr);
			} catch (Exception e) {
				LOG.warn("defaultCacheMs配置格式不正确:" + defaultCacheMsStr, e);
			}
		}
		String defaultCacheSizeStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("cacheMethod.defaultCacheSize"));
		if (defaultCacheSizeStr != null && defaultCacheSizeStr.length() > 0) {
			try {
				defaultCacheSize = Integer.parseInt(defaultCacheSizeStr);
			} catch (Exception e) {
				LOG.warn("defaultCacheSize配置格式不正确:" + defaultCacheSizeStr, e);
			}
		}
		String cleanIntervalStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("cacheMethod.cleanInterval"));
		if (cleanIntervalStr != null && cleanIntervalStr.length() > 0) {
			try {
				cleanInterval = Integer.parseInt(cleanIntervalStr);
			} catch (Exception e) {
				LOG.warn("cleanInterval配置格式不正确:" + cleanIntervalStr, e);
			}
		}
		String checkMemIntervalStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("cacheMethod.checkMemInterval"));
		if (checkMemIntervalStr != null && checkMemIntervalStr.length() > 0) {
			try {
				checkMemInterval = Integer.parseInt(checkMemIntervalStr);
			} catch (Exception e) {
				LOG.warn("checkMemInterval配置格式不正确:" + checkMemIntervalStr, e);
			}
		}
		String permCacheSizeStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("cacheMethod.permCacheSize"));
		if (permCacheSizeStr != null && permCacheSizeStr.length() > 0) {
			try {
				permCacheSize = Integer.parseInt(permCacheSizeStr);
			} catch (Exception e) {
				LOG.warn("permCacheSize配置格式不正确:" + permCacheSizeStr, e);
			}
		}
		globalCache = java.util.Collections.synchronizedMap(new LRUMap(
				defaultCacheSize));
		permCache = java.util.Collections.synchronizedMap(new LRUMap(
				permCacheSize));
	}

	private Object getPermCache(ProceedingJoinPoint joinPoint, String key)
			throws Throwable {
		Object result = permCache.get(key);
		if (result == null) {
			try {
				result = joinPoint.proceed();
			} catch (Throwable t) {
				LOG.warn("getPermCache ex for " + key, t);
				throw (t);
			}
			// 特殊处理，不缓存null
			if (result == null) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("permCache返回数据无效:" + key);
				}
				return null;
			}
			permCache.put(key, result);
			if (LOG.isDebugEnabled()) {
				LOG.debug("permCache缓存未命中:" + key);
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("permCache缓存命中:" + key);
			}
		}
		return result;
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		return cacheMethod(joinPoint, defaultCacheMs);
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs)
			throws Throwable {
		return cacheMethod(joinPoint, cacheMs, 0);
	}

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs,
			int classOrObj) throws Throwable {
		Object targetObj = joinPoint.getTarget();
		String className = targetObj.getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		StringBuffer sb = new StringBuffer();
		sb.append(className);
		sb.append('@');
		if (classOrObj == 1) {
			// 按对象缓存
			sb.append(targetObj.hashCode());
			sb.append(".");
		}
		sb.append(methodName);
		sb.append(":");
		Object args[] = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			if ((obj instanceof CharSequence) || (obj instanceof Number)
					|| (obj instanceof Character) || (obj instanceof Boolean)
					|| (obj instanceof Date)) {
				sb.append(obj.toString());
			} else {
				sb.append(ToStringBuilder.reflectionToString(obj,
						ToStringStyle.SIMPLE_STYLE));
			}
			sb.append(";");
		}
		String key;
		// 保护行为，防止key过大导致内存溢出
		if (sb.length() > 8192)
			key = sb.substring(0, 8192);
		else
			key = sb.toString();
		if (cacheMs >= 31536000000L) {
			// 取长期缓存
			return getPermCache(joinPoint, key);
		}
		CachedValueContainer cachedValueContainer = null;
		long currentTime = System.currentTimeMillis();
		try {
			if (lastAccessTime - currentTime - timeDiff > 0) {
				LOG.warn("时间发生向前跳变，清空缓存!");
				globalCache.clear();
				nextCleanTime = currentTime + cleanInterval;
				nextCheckMemTime = currentTime + checkMemInterval;
			} else {
				cachedValueContainer = globalCache.get(key);
				if (cachedValueContainer != null
						&& cachedValueContainer.isValid()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("CacheMethod缓存命中:" + key);
					}
					// 是否需要预读取
					if (cachedValueContainer.expireTime - currentTime < (cacheMs >> 3)
							&& cachedValueContainer.preFetching == false) {
						cachedValueContainer.preFetching = true;
						if (LOG.isDebugEnabled()) {
							LOG.debug("CacheMethod预读取:" + key);
						}
						try {
							updateCache(joinPoint, cacheMs, key,
									cachedValueContainer);
						} catch (Throwable t) {
							LOG.info("预读取同步调用异常", t);
						}
					}
					return cachedValueContainer.cachedValue;
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("CacheMethod缓存未命中:" + key);
			}
			return updateCache(joinPoint, cacheMs, key, cachedValueContainer);
		} finally {
			lastAccessTime = currentTime;
			if (nextCheckMemTime < currentTime) {
				nextCheckMemTime = currentTime + checkMemInterval;
				// 检查系统可用内存
				long maxMemory = runtime.maxMemory();
				long totalFreeMemory = maxMemory - runtime.totalMemory()
						+ runtime.freeMemory();
				if (totalFreeMemory < (maxMemory >> 2)) {
					if (memLowCount == 0 || totalFreeMemory < (maxMemory >> 3))
						memLowCount++;
					if (memLowCount >= 3) {
						globalCache.clear();
						nextCleanTime = currentTime + cleanInterval;
						LOG.warn("内存长期偏低，清空缓存!");
					} else {
						if (memLowCount == 2) {
							permCache.clear();
							System.gc();
							LOG.info("清理permCache，剩余内存:" + totalFreeMemory);
						}
						nextCleanTime = 0;
						LOG.info("清理Cache，剩余内存:" + totalFreeMemory);
					}
				} else
					memLowCount = 0;
			}
			if (nextCleanTime < currentTime) {
				nextCleanTime = currentTime + cleanInterval;
				// 异步清理已经过期的CachedValue
				try {
					asyncCacheMethod.cleanCacheValue();
				} catch (Throwable t) {
					LOG.info("清理CachedValue异步调用异常", t);
				}
			}
		}
	}

	private boolean cleanFinished;

	public void cleanCacheValue() {
		int cleanCount = 0;
		long beginTime = System.currentTimeMillis();
		Set<Map.Entry<String, CachedValueContainer>> cacheValues = globalCache
				.entrySet();
		cleanFinished = false;
		synchronized (globalCache) {
			if (cleanFinished == false) {
				Iterator<Entry<String, CachedValueContainer>> it = cacheValues
						.iterator();
				while (it.hasNext()) {
					Entry<String, CachedValueContainer> entry = it.next();
					CachedValueContainer cachedValueContainer = entry
							.getValue();
					if (cachedValueContainer != null
							&& !cachedValueContainer.isValid()) {
						it.remove();
						cleanCount++;
					}
				}
				cleanFinished = true;
			}
		}
		/*
		 * Object[] keys = globalCache.keySet().toArray(); for (Object key :
		 * keys) { CachedValueContainer cachedValueContainer =
		 * globalCache.get(key); if (cachedValueContainer != null &&
		 * cachedValueContainer.getCachedValue() != null &&
		 * !cachedValueContainer.isValid()) synchronized (cachedValueContainer)
		 * { if (!cachedValueContainer.isValid()) {
		 * cachedValueContainer.setCachedValue(null); cleanCount++; } } }
		 */
		LOG.info((System.currentTimeMillis() - beginTime) + "ms, 清理失效数据:"
				+ cleanCount + ", Cahce容器大小:" + globalCache.size()
				+ ", permCache大小:" + permCache.size());
	}

	public Object updateCache(ProceedingJoinPoint joinPoint, long cacheMs,
			String key, CachedValueContainer cachedValueContainer)
			throws Throwable {
		if (cachedValueContainer != null) {
			// 加锁，减少并发重复调用对后台压力
			synchronized (cachedValueContainer) {
				if (cachedValueContainer.preFetching == false
						&& cachedValueContainer.isValid()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("CacheMethod排队后命中:" + key);
					}
					return cachedValueContainer.cachedValue;
				} else
					return fetchCacheValue(joinPoint, cacheMs, key,
							cachedValueContainer);
			}
		}
		return fetchCacheValue(joinPoint, cacheMs, key, cachedValueContainer);
	}

	private Object fetchCacheValue(ProceedingJoinPoint joinPoint, long cacheMs,
			String key, CachedValueContainer cachedValueContainer)
			throws Throwable {
		Object result;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			LOG.warn("fetchCacheValue ex for " + key, t);
			throw (t);
		}
		// 特殊处理，不缓存null
		if (result == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("CacheMethod返回null:" + key);
			}
			return null;
		}
		if (cachedValueContainer == null) {
			cachedValueContainer = new CachedValueContainer();
		}
		long expireTime = System.currentTimeMillis() + cacheMs;
		cachedValueContainer.cachedValue = result;
		cachedValueContainer.expireTime = expireTime;
		globalCache.put(key, cachedValueContainer);
		cachedValueContainer.preFetching = false;
		if (LOG.isDebugEnabled()) {
			LOG.debug("CacheMethod取数据:" + key);
		}
		return result;
	}

	public void setAsyncCacheMethod(CacheMethodFacade asyncCacheMethod) {
		this.asyncCacheMethod = asyncCacheMethod;
	}

	public CacheMethodFacade getAsyncCacheMethod() {
		return asyncCacheMethod;
	}
}
