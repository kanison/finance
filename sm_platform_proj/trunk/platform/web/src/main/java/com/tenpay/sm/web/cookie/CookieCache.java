/**
 * 
 */
package com.tenpay.sm.web.cookie;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.tenpay.sm.cache.Cache;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * @author torryhong 用Cookie实现Session中的数据Cache
 */
public class CookieCache implements Cache {
	private static Logger logger = Logger.getLogger(CookieCache.class);
	AppConfig appConfig = ReloadableAppConfig.appConfig;
	public static final String COOKIE_NAME = "__cache";
	public static final String CURRENT_NAME = "__cookie_cache";

	private String cookieDomain = appConfig.get("global_domain");
	private String cookiePath = "/";

	private final static String DEFAULT_KEY = "6edd29b2e478ec28a43bc6bd20fb5b724b0aed4207c47fa8";

	private Cipher cipherEncrypt;
	private Cipher cipherDecrypt;

	private void initCrypto() {
		if (cipherEncrypt == null || cipherDecrypt == null) {
			// TODO 随机生成，多个自动更换
			String key = appConfig.get("cookie_session_cache_key");
			if (key == null) {// 没有配置则才有默认值
				key = DEFAULT_KEY;
			}

			try {
				byte keybyte[] = Hex.decodeHex(key.toCharArray()); // 按照16进制转换
				SecretKey secretKey = new SecretKeySpec(keybyte, "DESede");

				cipherEncrypt = Cipher.getInstance("DESede");
				cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);

				cipherDecrypt = Cipher.getInstance("DESede");
				cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);
			} catch (Exception ex) {
				throw new RuntimeException("CookieCache初始化加解密错误", ex);
			}
		}
	}

	private Map getCacheMap() {
		WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
		Map cache = (Map) ctx.getCurrentAttribute(CURRENT_NAME);
		if (cache == null) {
			String cookie_value = CookieUtil.getValue(ctx.getRequest(),
					COOKIE_NAME);
			if (cookie_value != null && !"".equals(cookie_value)) {
				byte[] bytes = null;
				this.initCrypto();
				try {
					// bytes = Base64.fromBase64Bytes(cookie_value);
					bytes = Hex.decodeHex(cookie_value.toCharArray());
					bytes = this.cipherDecrypt.doFinal(bytes);
				} catch (Exception e) {
					logger.error("解密cookie cache出错", e);
					return null;
				}
				java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
						bytes);
				try {
					java.io.ObjectInputStream ois = new java.io.ObjectInputStream(
							bais);
					cache = (Map) ois.readObject();
				} catch (Exception ex) {
					// TODO 故意忽略，待重新评估，需要日志
					// LOG.error("初始化httpSessionModel,出错，忽略.",ex2);
				}
			}
			if (cache != null) {
				ctx.setCurrentAttribute(CURRENT_NAME, cache);
			}
		}
		return cache;
	}

	private void storeSessionToCookie(Map cache) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		try {
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(
					baos);
			oos.writeObject(cache);
		} catch (Exception ex1) {
			// TODO 故意忽略，待重新评估，需要日志
			// LOG.error("初始化httpSessionModel,出错，忽略.",ex1);
		}
		byte[] bytes = baos.toByteArray();
		this.initCrypto();
		try {
			bytes = this.cipherEncrypt.doFinal(bytes);
		} catch (Exception e) {
			logger.error("加密cookie cache出错", e);
			return;
		}
		// String cookie_value = Base64.toBase64(bytes);
		String cookie_value = new String(Hex.encodeHex(bytes));
		if (logger.isDebugEnabled()) {
			logger.debug("cookie_value length: " + cookie_value.length());
		}
		if (cookie_value != null && cookie_value.length() > 4096) {
			logger.warn("cookiecache_value too long!");
		}

		WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
		Cookie cookie = new Cookie(COOKIE_NAME, cookie_value);
		if (this.cookieDomain != null) {
			if (cookieDomain != null && !cookieDomain.startsWith(".")) {
				cookieDomain = "." + cookieDomain;
			}
			cookie.setDomain(cookieDomain);
		}
		if (this.cookiePath != null) {
			cookie.setPath(cookiePath);
		}
		ctx.getResponse().addCookie(cookie);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.cache.Cache#get(java.lang.String)
	 */
	public Object get(String key) {
		Map cache = getCacheMap();
		if (cache != null) {
			return cache.get(key);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.cache.Cache#isExist(java.lang.String)
	 */
	public boolean isExist(String key) {
		return get(key) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.cache.Cache#put(java.lang.String, java.lang.Object)
	 */
	public Object put(String key, Object value) {
		Map cache = getCacheMap();
		if (cache == null) {
			cache = new HashMap();
			WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
			ctx.setCurrentAttribute(CURRENT_NAME, cache);
		}
		Object returnValue = cache.put(key, (java.io.Serializable) value);
		// TODO 每次都store合适吗
		storeSessionToCookie(cache);
		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.cache.Cache#remove(java.lang.String)
	 */
	public Object remove(String key) {
		Map cache = getCacheMap();
		if (cache != null) {
			// TODO 每次都store合适吗
			storeSessionToCookie(cache);
			return cache.remove(key);
		}
		return null;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

}
