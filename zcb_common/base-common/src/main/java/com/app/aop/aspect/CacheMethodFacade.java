package com.app.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import com.app.utils.CachedValueContainer;

public interface CacheMethodFacade {
	public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable;

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs)
			throws Throwable;

	public Object updateCache(ProceedingJoinPoint joinPoint, long cacheMs,
			String key, CachedValueContainer cachedValueContainer)
			throws Throwable;

	public Object cacheMethod(ProceedingJoinPoint joinPoint, long cacheMs,
			int classOrObj) throws Throwable;

	public void cleanCacheValue();
}
