package com.tenpay.sm.lang.lb.hessian;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

import com.tenpay.sm.common.lang.StringUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * ʵ�ָ��ؾ����hessian���� ;
 * 
 * @author aixxia
 * 
 */
public class HessianProxyFactoryBeanWithLB extends HessianClientInterceptorLB
		implements FactoryBean {
	private boolean allowNoUrl;
	private Object serviceProxy;

	public HessianProxyFactoryBeanWithLB() {
		super();
	}

	public void afterPropertiesSet() {
		String serviceUrl = this.getServiceUrl();
		if (serviceUrl == null) {
			// ���Ի�ȡĬ��url
			serviceUrl = StringUtil.trimToNull(ReloadableAppConfig.appConfig
					.get("hessian.serviceUrl"));
			this.setServiceUrl(serviceUrl);
		}
		if (serviceUrl == null && allowNoUrl) {
			// û��url, ��������
			return;
		}
		String readTimeoutStr = StringUtil
				.trimToNull(ReloadableAppConfig.appConfig
						.get("hessian.readTimeout"));
		if (readTimeoutStr != null)
			try {
				this.setReadTimeout(Long.parseLong(readTimeoutStr));
			} catch (Throwable t) {
			}
			
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this)
				.getProxy(getBeanClassLoader());
	}

	public Object getObject() {
		return this.serviceProxy;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

	public void setAllowNoUrl(boolean allowNoUrl) {
		this.allowNoUrl = allowNoUrl;
	}

	public boolean isAllowNoUrl() {
		return allowNoUrl;
	}
}