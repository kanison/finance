/**
 * 
 */
package com.tenpay.sm.test.config;

import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.test.base.ContextSupportTestCase;


/**
 * @author torryhong
 *
 */
public class TestAppConfig extends ContextSupportTestCase {
	AppConfig appConfig;
	
	public void testLoad() throws InterruptedException {
		System.out.println(this.appConfig.get("web_trace_mode"));
		Thread.sleep(25000);
		System.out.println(this.appConfig.get("web_trace_mode"));
	}

	/**
	 * @return the appConfig
	 */
	public AppConfig getAppConfig() {
		return appConfig;
	}

	/**
	 * @param appConfig the appConfig to set
	 */
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	
	
}
