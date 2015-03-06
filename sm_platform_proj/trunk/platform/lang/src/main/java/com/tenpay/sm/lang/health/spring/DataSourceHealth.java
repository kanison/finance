/**
 * 
 */
package com.tenpay.sm.lang.health.spring;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class DataSourceHealth implements Health,ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(DataSourceHealth.class);
	private ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.health.Health#check()
	 */
	public Object check() {
		Map map = new TreeMap();
		String[] beanNames = this.applicationContext.getBeanNamesForType(javax.sql.DataSource.class, false, false);
		if(beanNames!=null) {
			for(String beanName : beanNames) {
				javax.sql.DataSource ds = (javax.sql.DataSource)this.applicationContext.getBean(beanName, javax.sql.DataSource.class);
				Map status = new TreeMap();
				try {
					status.put("loginTimeout", ds.getLoginTimeout());
				} catch (SQLException e) {
					logger.error("getLoginTimeout error",e);
				}
				//TODO 获取dataSource的情况, dbcp, c3p0, 不能用DriverManagerDataSource！！！
				map.put(beanName.replace("#", ".sharp."), status);
			}
		}
		
		return map;
	}

}
