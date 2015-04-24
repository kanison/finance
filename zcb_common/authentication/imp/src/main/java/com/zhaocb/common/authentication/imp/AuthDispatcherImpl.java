package com.zhaocb.common.authentication.imp;

import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.common.authentication.facade.AuthDispatcherFacade;
import com.zhaocb.common.authentication.facade.DetailAuthFacade;

public class AuthDispatcherImpl implements AuthDispatcherFacade {
	private DetailAuthFacade apiAuth;
	private DetailAuthFacade mchAuth;
	private DetailAuthFacade tenpayAuth;
	private DetailAuthFacade tenpayWeakAuth;
	private DetailAuthFacade localByTenpayAuth;
	private DetailAuthFacade localByMchAuth;
	private DetailAuthFacade weixinAuth;
	
	private ApiSignRetObj apiSignRetObj;
	private boolean debugIgnoreCert = false;
	private boolean debugIgnoreApi = false;
	private boolean debugIgnoreTenpay = false;
	private boolean debugIgnoreMch = false;

	public AuthDispatcherImpl() {
		super();
		String ignoreCert = ReloadableAppConfig.appConfig
				.get("debug.ignoreCert");
		if (ignoreCert != null
				&& (ignoreCert.equals("1") || ignoreCert
						.equalsIgnoreCase("true"))) {
			debugIgnoreCert = true;
		}
		String ignoreApi = ReloadableAppConfig.appConfig.get("debug.ignoreApi");
		if (ignoreApi != null
				&& (ignoreApi.equals("1") || ignoreApi.equalsIgnoreCase("true"))) {
			debugIgnoreApi = true;
		}
		String ignoreTenpay = ReloadableAppConfig.appConfig
				.get("debug.ignoreTenpay");
		if (ignoreTenpay != null
				&& (ignoreTenpay.equals("1") || ignoreTenpay
						.equalsIgnoreCase("true"))) {
			debugIgnoreTenpay = true;
		}
		String ignoreMch = ReloadableAppConfig.appConfig.get("debug.ignoreMch");
		if (ignoreMch != null
				&& (ignoreMch.equals("1") || ignoreMch.equalsIgnoreCase("true"))) {
			debugIgnoreMch = true;
		}
	}

	public int commonAuth(int authChannel, int needCert, Object[] args) {

		boolean result;

		// debug模式关闭证书检查
		if (debugIgnoreCert)
			needCert = 0;

		if ((authChannel & AuthDispatcherFacade.MCH_MASK) > 0) {
			// debug模式关闭MCH登录检查
			if (debugIgnoreMch)
				return AuthDispatcherFacade.MCH_MASK;
			result = mchAuth.auth(
					(needCert & AuthDispatcherFacade.MCH_MASK) > 0, args);
			if (result == true)
				return AuthDispatcherFacade.MCH_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.TENPAY_WEAK_MASK) > 0) {
			// debug模式关闭Tenpay登录检查
			if (debugIgnoreTenpay)
				return AuthDispatcherFacade.TENPAY_WEAK_MASK;
			result = tenpayWeakAuth.auth(
					(needCert & AuthDispatcherFacade.TENPAY_WEAK_MASK) > 0,
					args);
			if (result == true)
				return AuthDispatcherFacade.TENPAY_WEAK_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.TENPAY_MASK) > 0) {
			// debug模式关闭Tenpay登录检查
			if (debugIgnoreTenpay)
				return AuthDispatcherFacade.TENPAY_MASK;
			result = tenpayAuth.auth(
					(needCert & AuthDispatcherFacade.TENPAY_MASK) > 0, args);
			if (result == true)
				return AuthDispatcherFacade.TENPAY_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.API_MASK) > 0) {

			// debug模式关闭Api检查
			if (debugIgnoreApi)
				return AuthDispatcherFacade.API_MASK;

			result = apiAuth.auth(
					(needCert & AuthDispatcherFacade.API_MASK) > 0, args);
			if (result == true)
				return AuthDispatcherFacade.API_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.LOCAL_BY_TENPAY_MASK) > 0) {
			// debug模式关闭Tenpay登录检查
			if (debugIgnoreTenpay)
				return AuthDispatcherFacade.LOCAL_BY_TENPAY_MASK;
			result = localByTenpayAuth.auth(
					(needCert & AuthDispatcherFacade.LOCAL_BY_TENPAY_MASK) > 0,
					args);
			if (result == true)
				return AuthDispatcherFacade.LOCAL_BY_TENPAY_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.LOCAL_BY_MCH_MASK) > 0) {
			// debug模式关闭MCH登录检查
			if (debugIgnoreMch)
				return AuthDispatcherFacade.LOCAL_BY_MCH_MASK;
			result = localByMchAuth.auth(
					(needCert & AuthDispatcherFacade.LOCAL_BY_MCH_MASK) > 0,
					args);
			if (result == true)
				return AuthDispatcherFacade.LOCAL_BY_MCH_MASK;
		}
		if ((authChannel & AuthDispatcherFacade.WEIXIN_MASK) > 0) {
			// debug模式关闭MCH登录检查
			if (debugIgnoreMch)
				return AuthDispatcherFacade.WEIXIN_MASK;
			result = weixinAuth.auth(
					(needCert & AuthDispatcherFacade.WEIXIN_MASK) > 0,
					args);
			if (result == true)
				return AuthDispatcherFacade.WEIXIN_MASK;
		}
		return 0;
	}

	public void signRetObj(Object obj) {
		apiSignRetObj.signRetObj(obj);
	}

	public DetailAuthFacade getApiAuth() {
		return apiAuth;
	}

	public void setApiAuth(DetailAuthFacade apiAuth) {
		this.apiAuth = apiAuth;
	}

	public DetailAuthFacade getMchAuth() {
		return mchAuth;
	}

	public void setMchAuth(DetailAuthFacade mchAuth) {
		this.mchAuth = mchAuth;
	}

	public DetailAuthFacade getTenpayAuth() {
		return tenpayAuth;
	}

	public void setTenpayAuth(DetailAuthFacade tenpayAuth) {
		this.tenpayAuth = tenpayAuth;
	}

	public ApiSignRetObj getApiSignRetObj() {
		return apiSignRetObj;
	}

	public void setApiSignRetObj(ApiSignRetObj apiSignRetObj) {
		this.apiSignRetObj = apiSignRetObj;
	}

	public DetailAuthFacade getTenpayWeakAuth() {
		return tenpayWeakAuth;
	}

	public void setTenpayWeakAuth(DetailAuthFacade tenpayWeakAuth) {
		this.tenpayWeakAuth = tenpayWeakAuth;
	}

	public DetailAuthFacade getLocalByTenpayAuth() {
		return localByTenpayAuth;
	}

	public void setLocalByTenpayAuth(DetailAuthFacade localByTenpayAuth) {
		this.localByTenpayAuth = localByTenpayAuth;
	}

	public DetailAuthFacade getLocalByMchAuth() {
		return localByMchAuth;
	}

	public void setLocalByMchAuth(DetailAuthFacade localByMchAuth) {
		this.localByMchAuth = localByMchAuth;
	}

	public DetailAuthFacade getWeixinAuth() {
		return weixinAuth;
	}

	public void setWeixinAuth(DetailAuthFacade weixinAuth) {
		this.weixinAuth = weixinAuth;
	}

}
