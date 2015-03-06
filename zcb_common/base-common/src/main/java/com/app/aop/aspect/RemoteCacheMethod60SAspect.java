package com.app.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

@Aspect
public class RemoteCacheMethod60SAspect implements Ordered {
	private RemoteCacheMethod remoteCacheMethod;

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.app.aop.annotation.RemoteCacheMethod60S)")
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
		return remoteCacheMethod.cacheMethod(joinPoint, 60 * 1000);
	}

	public int getOrder() {
		return -1;
	}

	public RemoteCacheMethod getRemoteCacheMethod() {
		return remoteCacheMethod;
	}

	public void setRemoteCacheMethod(RemoteCacheMethod remoteCacheMethod) {
		this.remoteCacheMethod = remoteCacheMethod;
	}

}
