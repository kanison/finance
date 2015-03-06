/**
 * 
 */
package com.tenpay.sm.web.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import com.tenpay.sm.web.tag.ModuleExecutor;
/**
 * 支持多个扩展名的BeanNameUrlHandlerMapping
 * 多个扩展名的url映射到同一个bean做handler
 * @author li.hongtl
 *
 */
public class MultiExtBeanNameUrlHandlerMapping extends BeanNameUrlHandlerMapping  {
	private static Logger logger = Logger.getLogger(MultiExtBeanNameUrlHandlerMapping.class);
	/**
	 * 扩展名列表
	 */
	List<String> suffixes = new ArrayList<String>();
	
	private List extendInterceptors;
	
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		Object handler = request.getAttribute(ModuleExecutor.REQUEST_MODULE_HANDLER_ATTRIBUTE);
		if(handler!=null) {
			return handler;
		}
		return super.getHandlerInternal(request);
		
	}
	
	/**
	 * Check name and aliases of the given bean for URLs,
	 * detected by starting with "/".
	 * @param beanName the name of the candidate bean
	 * @return the URLs determined for the bean, or an empty array if none
	 */
	@Override
	protected String[] determineUrlsForHandler(String beanName) {
		List<String> urls = new ArrayList<String>();
		if (beanName.startsWith("/")) {
			//urls.add(beanName);
			urls.addAll(urlToMultiExtName(beanName));
			if(logger.isInfoEnabled()) {
				logger.info("urlHandler, beanName:" + beanName);
			}
		}
		String[] aliases = getApplicationContext().getAliases(beanName);
		for (int j = 0; j < aliases.length; j++) {
			if (aliases[j].startsWith("/")) {
				//urls.add(aliases[j]);
				urls.addAll(urlToMultiExtName(aliases[j]));
				if(logger.isInfoEnabled()) {
					logger.info("urlHandler, bean alias name:" + aliases[j]);
				}
			}
		}
		return StringUtils.toStringArray(urls);
	}
	
	private Collection<String> urlToMultiExtName(String beanName) {
		//Object bean = this.getWebApplicationContext().getBean(beanName);
		Set<String> urls = new HashSet<String>(suffixes.size()+1);
		//if(bean instanceof Controller) {
			urls.add(beanName);
			for(String suffix : suffixes) {
				if(!beanName.endsWith(suffix)) {
					urls.add(beanName + suffix);
				}
			}
		//}
		return urls;
	}

	/**
	 * @return the suffixes
	 */
	public List<String> getSuffixes() {
		return suffixes;
	}

	/**
	 * @param suffixes the suffixes to set
	 */
	public void setSuffixes(List<String> postfixes) {
		this.suffixes = postfixes;
	}
	
	/**
	 * Extension hook that subclasses can override to register additional interceptors,
	 * given the configured interceptors (see {@link #setInterceptors}).
	 * <p>Will be invoked before {@link #initInterceptors()} adapts the specified
	 * interceptors into {@link HandlerInterceptor} instances.
	 * <p>The default implementation is empty.
	 * @param interceptors the configured interceptor List (never <code>null</code>),
	 * allowing to add further interceptors before as well as after the existing
	 * interceptors
	 */
	protected void extendInterceptors(List interceptors) {
		if(this.extendInterceptors!=null) {
			interceptors.addAll(this.extendInterceptors);
		}
	}

	public List getExtendInterceptors() {
		return extendInterceptors;
	}

	public void setExtendInterceptors(List extendInterceptors) {
		this.extendInterceptors = extendInterceptors;
	}
	
	
	
}
