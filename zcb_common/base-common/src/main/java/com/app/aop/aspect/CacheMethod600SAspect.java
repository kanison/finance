package com.app.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

@Aspect
public class CacheMethod600SAspect implements Ordered {
	private CacheMethod cacheMethod;

	/***
	 * ���� * �е� ����
	 */
	@Pointcut("@annotation(com.app.aop.annotation.CacheMethod600S)")
	public void allAddMethod() {
	};

	/***
	 * ִ�з���
	 * 
	 * @throws Throwable
	 * 
	 */
	@Around("allAddMethod()")
	public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		return cacheMethod.cacheMethod(joinPoint, 600 * 1000);
	}

	public int getOrder() {
		return -1;
	}

	public void setCacheMethod(CacheMethod cacheMethod) {
		this.cacheMethod = cacheMethod;
	}

	public CacheMethod getCacheMethod() {
		return cacheMethod;
	}
}
