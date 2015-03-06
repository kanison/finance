/**
 * 
 */
package com.tenpay.sm.lang.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;


/**
 * @author li.hongtl
 * @deprecated
 */
public class PropertiesBean extends HashMap<String,String> implements AppConfig {
	private static final long serialVersionUID = -8331186918071219255L;
	private static final Log LOG = LogFactory.getLog(PropertiesBean.class);
	private List<String> resources = new ArrayList<String>();
	
	private static final PropertiesBean appConfig = new PropertiesBean();
	static {
		try {
			appConfig.setResource("classpath:app-config.properties");
		} catch (IOException e) {
			LOG.warn("初始化加载classpath:app-config.propertie出错",e);
		}
	}
	
	public void setResource(String resource) throws IOException {
		try {
			java.util.Properties resourceProperties = new java.util.Properties();
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			InputStream is = resourceLoader.getResource(resource).getInputStream();
			resourceProperties.load(is);
			
			java.util.Enumeration en = resourceProperties.keys();
			while(en.hasMoreElements()) {
				String key = (String)en.nextElement();
				Object oldValue = this.put(key, resourceProperties.getProperty(key));
				if(oldValue!=null) {
					if(LOG.isInfoEnabled()) {
						LOG.info("PropertiesBean中覆盖项：" + key);
					}
				}
			}
			this.resources.add(resource);
		} catch (IOException e) {
			LOG.error("加载资源失败" + resource,e);
			throw e;
		}		
	}

	public String get(String key) {
		return super.get(key);
	}
}
