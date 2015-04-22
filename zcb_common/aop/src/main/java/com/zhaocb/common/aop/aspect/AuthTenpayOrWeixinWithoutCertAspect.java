package com.zhaocb.common.aop.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.web.cookie.CookieUtil;
import com.tenpay.sm.web.view.ViewUtil;
import com.zhaocb.common.aop.exception.AuthException;
import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;

/**
 * 检查微信登录态切面
 * 
 * @author wenlonwang
 * 
 */
@Aspect
public class AuthTenpayOrWeixinWithoutCertAspect implements Ordered {
	private AuthDispatcherFacade authDispatcher;

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.AuthTenpayOrWeixinWithoutCert)")
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
		HttpServletRequest request = CommonUtil.getHttpServletRequest();
		String qq_logtype = CommonUtil.trimString(CookieUtil.getValue(request,
				"qq_logtype"));
		int authType = 0;
		if (qq_logtype != null && qq_logtype.equals("16")) {
			// 微信登录类型
			authType = authDispatcher.commonAuth(
					AuthDispatcherFacade.WEIXIN_MASK, 0, args);
		} else {
			authType = authDispatcher.commonAuth(
					AuthDispatcherFacade.TENPAY_MASK, 0, args);
		}
		if (authType > 0) {
			return joinPoint.proceed();
		} else {
			if (ViewUtil.getCurrentRequestViewType().isRenderHTML()
					&& (qq_logtype == null || !qq_logtype.equals("16"))) {
				String redirectUrl = ReloadableAppConfig.appConfig
						.get("login_url");
				RedirectToLogin.redirectToLogin(redirectUrl);
				return null;
			} else
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
