package com.zhaocb.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.zhaocb.common.aop.exception.AuthException;
import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;

/**
 * 验证签名，并对返回结果签名， 
 * 注意：
 *   1 使用xml格式返回的数据 （调用接口后缀为.xml格式） 
 *   2 如果验证签名key与返回结果签名key不同，需要在返回的output中指定partner
 *   
 *   
 * @author kansonzhang
 * 
 */
@Aspect
public class AuthApiWithSignResultAspect implements Ordered {
	private AuthDispatcherFacade authDispatcher;

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.AuthApiWithSignResult)")
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
			// 对返回结果签名 
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
