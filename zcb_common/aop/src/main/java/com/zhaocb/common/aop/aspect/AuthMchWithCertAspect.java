package com.zhaocb.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.web.view.ViewUtil;
import com.zhaocb.common.aop.exception.AuthException;
import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;

/**
 * ����¼̬����
 * 
 * @author eniacli
 * 
 */
@Aspect
public class AuthMchWithCertAspect implements Ordered {
	private AuthDispatcherFacade authDispatcher;

	/***
	 * ���� * �е� ����
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.AuthMchWithCert)")
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
		int authType = authDispatcher.commonAuth(AuthDispatcherFacade.MCH_MASK,
				AuthDispatcherFacade.MCH_MASK, args);
		if (authType > 0) {
			return joinPoint.proceed();
		} else {
			if (ViewUtil.getCurrentRequestViewType().isRenderHTML()) {
				String redirectUrl = ReloadableAppConfig.appConfig
						.get("mch_login_url");
				RedirectToLogin.redirectToLogin(redirectUrl);
				return null;
			} else
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
