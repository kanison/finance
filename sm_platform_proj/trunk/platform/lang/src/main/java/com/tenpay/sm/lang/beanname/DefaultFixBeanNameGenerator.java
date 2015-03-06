/**
 * 
 */
package com.tenpay.sm.lang.beanname;

import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

/**
 * 根据包名配置前后缀的Spring beanName生成组件
 * 用户自动配置一个包的bean
 * 默认使用属性命名的格式，首字母小写
 * @author li.hongtl
 *
 */
public class DefaultFixBeanNameGenerator implements BeanNameGenerator {
	private static final Log LOG = LogFactory.getLog(DefaultFixBeanNameGenerator.class);
	java.util.Properties  prefixAndSuffix = new java.util.Properties ();
	
	public DefaultFixBeanNameGenerator() throws IOException {
		//TODO 很丑陋，很危险的做法
//		ClassPathResource resource = new ClassPathResource("package-beanname-config.properties", DefaultFixBeanNameGenerator.class);
//		prefixAndSuffix = PropertiesLoaderUtils.loadProperties(resource);
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		InputStream is = resourceLoader.getResource("package-beanname-config.properties").getInputStream();
		prefixAndSuffix.load(is);
		if(LOG.isDebugEnabled()) {
			LOG.debug("加载资源成功：package-beanname-config.properties");
		}
	}
	
	private String prefix;
	private String suffix;
	
	protected String internalGenerateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return Introspector.decapitalize(shortClassName);
	}
	
	/**
	 * 实现BeanNameGenerator接口的方法，生成bean的名字
	 */
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String name = internalGenerateBeanName(definition,registry);
        if(LOG.isDebugEnabled()) {
        	LOG.debug("内部生产beanName:" + name);
        }
        
        if(prefix!=null && !"".equals(prefix)) {
        	name = prefix + name;
        }
        else {
        	String className = definition.getBeanClassName();
        	int lastDotIndex = className.lastIndexOf('.');
        	String packageName = (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
        	String configPrefix = prefixAndSuffix.getProperty(packageName + ".prefix");
        	if(configPrefix!=null && !"".equals(configPrefix)) {
        		return configPrefix + name;
        	}
        }
        if(suffix!=null && !"".equals(suffix)) {
        	name = name + suffix;
        }
        else {
        	String className = definition.getBeanClassName();
        	int lastDotIndex = className.lastIndexOf('.');
        	String packageName = (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
        	String configSuffix = prefixAndSuffix.getProperty(packageName + ".suffix");
        	if(configSuffix!=null && !"".equals(configSuffix)) {
        		return name + configSuffix;
        	}
        }
        if(LOG.isDebugEnabled()) {
        	LOG.debug("生产beanName:" + name);
        }
        return name;
    }
    
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
