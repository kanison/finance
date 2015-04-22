package com.zhaocb.common.aop.aspect.cachemethod;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

@Aspect
public class CacheObjMethod600SAspect implements Ordered {
	private CacheMethod cacheMethod;

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.CacheObjMethod600S)")
	public void allAddMethod() {
	};

	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 * 
	 */
	@Around("allAddMethod()")
	public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		return cacheMethod.cacheMethod(joinPoint, 600 * 1000, 1);
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
