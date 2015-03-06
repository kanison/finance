/**
 * 
 */
package com.tenpay.sm.lang.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author torryhong
 * 
 */
public class ReloadableAppConfig implements AppConfig {
	private static final Log LOG = LogFactory.getLog(ReloadableAppConfig.class);
	public static final ReloadableAppConfig appConfig = new ReloadableAppConfig();
	static {
		try {
			appConfig.setResourceBaseName("classpath:app-config");

			// 设置JVM变量
			String cacheTtl = appConfig.get("networkaddress.cache.ttl");
			if (cacheTtl == null || cacheTtl.trim().length() == 0) {
				cacheTtl = "120";
			}
			java.security.Security.setProperty("networkaddress.cache.ttl",
					cacheTtl);
			java.security.Security
					.setProperty("sun.net.inetaddr.ttl", cacheTtl);

			String cacheNegativeTtl = appConfig
					.get("networkaddress.cache.negative.ttl");
			if (cacheNegativeTtl == null
					|| cacheNegativeTtl.trim().length() == 0) {
				cacheNegativeTtl = "120";
			}
			java.security.Security.setProperty(
					"networkaddress.cache.negative.ttl", cacheNegativeTtl);
			java.security.Security.setProperty("sun.net.inetaddr.negative.ttl",
					cacheNegativeTtl);
			String connectionTimeout = appConfig
					.get("sun.net.client.defaultConnectTimeout");
			if (connectionTimeout == null
					|| connectionTimeout.trim().length() == 0) {
				connectionTimeout = "60000";
			}
			String readTimeout = appConfig
					.get("sun.net.client.defaultReadTimeout");
			if (readTimeout == null || readTimeout.trim().length() == 0) {
				readTimeout = "60000";
			}
			System.setProperty("sun.net.client.defaultConnectTimeout",
					connectionTimeout);
			System
					.setProperty("sun.net.client.defaultReadTimeout",
							readTimeout);

			String proxyHost = appConfig.get("http.proxyHost");
			if (proxyHost != null && proxyHost.trim().length() > 0)
				System.getProperties().setProperty("http.proxyHost", proxyHost);
			String proxyPort = appConfig.get("http.proxyPort");
			if (proxyPort != null && proxyPort.trim().length() > 0)
				System.getProperties().setProperty("http.proxyPort", proxyPort);
			String nonProxyHosts = appConfig.get("http.nonProxyHosts");
			if (nonProxyHosts != null && nonProxyHosts.trim().length() > 0)
				System.getProperties().setProperty("http.nonProxyHosts",
						nonProxyHosts);

		} catch (IOException e) {
			LOG.warn("初始化加载classpath:app-config.propertie出错", e);
		}
	}

	public ReloadableAppConfig() {
		try {
			setResourceBaseName("classpath:app-config");
		} catch (IOException e) {
			LOG.warn("初始化加载classpath:app-config.propertie出错", e);
		}
	}

	private ReloadableResourceBundleMessageSource resource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tenpay.sm.lang.config.AppConfig#get(java.lang.String)
	 */
	public String get(String key) {
		try {
			return this.resource.getMessage(key, null, Locale.getDefault());
		} catch (Exception ex) {
			return null;
		}
	}

	private void setResourceBaseName(String resource) throws IOException {
		this.resource = new ReloadableResourceBundleMessageSource();
		this.resource.setBasename(resource);
		this.resource.setDefaultEncoding("utf-8");
		this.resource.setCacheSeconds(10);
	}

	public void setResource(ReloadableResourceBundleMessageSource resource)
			throws IOException {
		this.resource = resource;
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		// TODO Auto-generated method stub
		return new HashSet<java.util.Map.Entry<String, String>>();
	}

	public String get(Object key) {
		return this.get(key.toString());
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return new HashSet<String>();
	}

	public String put(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(Map<? extends String, ? extends String> t) {
		// TODO Auto-generated method stub

	}

	public String remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Collection<String> values() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

}
