package com.zhaocb.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.zhaocb.common.aop.exception.AuthException;
import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;

/**
 * 检查登录态切面
 * 
 * @author eniacli
 * 
 */
@Aspect
public class AuthApiWithoutCertAspect implements Ordered {
	private AuthDispatcherFacade authDispatcher;
	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.zhaocb.common.aop.annotation.AuthApiWithoutCert)")
	public void allAddMethod() {
	};

	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 */
	@Around("allAddMethod()")
	public Object auth(ProceedingJoinPoint joinPoint) throws Throwable {
		Object args[] = joinPoint.getArgs();
		int authType = authDispatcher.commonAuth(AuthDispatcherFacade.API_MASK,
				0, args);
		if (authType > 0) {
			Object retObj = joinPoint.proceed();
			if ((authType & AuthDispatcherFacade.API_MASK) > 0)
				authDispatcher.signRetObj(retObj);
			return retObj;
		} else {
			throw new AuthException("鉴权失败");
		}
	}

	public int getOrder() {
		return 1;
	}

	public AuthDispatcherFacade getAuthDispatcher() {
		return authDispatcher;
	}

	public void setAuthDispatcher(AuthDispatcherFacade authDispatcher) {
		this.authDispatcher = authDispatcher;
	}

}
