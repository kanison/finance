package com.app.aop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.web.view.ViewUtil;

/**
 * ����¼̬����
 * 
 * @author eniacli
 * 
 */
@Aspect
public class AuthMchWithoutCertOrApiWithoutCertAspect implements Ordered {


	/***
	 * ���� * �е� ����
	 */
	@Pointcut("@annotation(om.app.aop.annotation.AuthMchWithoutCertOrApiWithoutCert)")
	public void allAddMethod() {
	};

	/***
	 * ִ�з���
	 * 
	 * @throws Throwable
	 */
	@Around("allAddMethod()")
	public Object auth(ProceedingJoinPoint joinPoint) throws Throwable {
		/*Object args[] = joinPoint.getArgs();
		int authType = authDispatcher.commonAuth(AuthDispatcherFacade.API_MASK
				| AuthDispatcherFacade.MCH_MASK, 0, args);
		if (authType > 0) {
			Object retObj = joinPoint.proceed();
			if ((authType & AuthDispatcherFacade.API_MASK) > 0)
				authDispatcher.signRetObj(retObj);
			return retObj;
		} else {
			if (ViewUtil.getCurrentRequestViewType().isRenderHTML()) {
				String redirectUrl = ReloadableAppConfig.appConfig
						.get("mch_login_url");
				RedirectToLogin.redirectToLogin(redirectUrl);
				return null;
			} else
				throw new AuthException("��Ȩʧ��");
		}*/
		
		
		return null;
	}

	public int getOrder() {
		return 1;
	}



}
