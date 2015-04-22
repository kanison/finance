package com.zhaocb.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;
import com.zhaocb.common.aop.exception.AuthException;

/**
 * ����¼̬����
 * 
 * @author eniacli
 * 
 */
@Aspect
public class AuthApiWithCertAspect implements Ordered {
	private AuthDispatcherFacade authDispatcher;

	/***
	 * ���� * �е� ����
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.AuthApiWithCert)")
	public void allAddMethod() {
	};

	/***
	 * ִ�з���
	 * 
	 * @throws Throwable
	 */
	@Around("allAddMethod()")
	public Object auth(ProceedingJoinPoint joinPoint) throws Throwable {
		Object args[] = joinPoint.getArgs();
		int authType = authDispatcher.commonAuth(AuthDispatcherFacade.API_MASK,
				AuthDispatcherFacade.API_MASK, args);
		if (authType > 0) {
			Object retObj = joinPoint.proceed();
			if ((authType & AuthDispatcherFacade.API_MASK) > 0)
				authDispatcher.signRetObj(retObj);
			return retObj;
		} else {
			throw new AuthException("��Ȩʧ��");
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
