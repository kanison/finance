/**
 * 
 */
package com.tenpay.sm.test.base;

import java.beans.PropertyDescriptor;


import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tenpay.sm.client.soagov.RequestHeaderXFireSoaGovInterceptor;
import com.tenpay.sm.common.lang.diagnostic.Profiler;
import com.tenpay.sm.lang.log.Loggers;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class ContextSupportTestCase extends TestCase {
	private ApplicationContext applicationContext;
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		Loggers.PERF.warn("<script>function select_changed(theme) {var divNext = theme.nextSibling;divNext.style.display= divNext.style.display=='none'? '':'none';}</script>");
		Profiler.reset();
		Profiler.start("测试开始");
		
		Profiler.enter("初始化ApplicationContextTestCase");
		setUp(new String[] {
//				"../../demo/war/WebRoot/WEB-INF/DispatcherServlet-servlet.xml",
				"src/test/resources/META-INF/spring/ApplicationContextTestCase.xml",
				//"E:/myworkspace/smpoc-demo-war/WebRoot/WEB-INF/DispatcherServlet-servlet.xml",
//				"META-INF/spring/smpoc-platform-wc.xml",
//				"META-INF/spring/smpoc-platform-mashup.xml",
//				"META-INF/spring/smpoc-demo-service.xml",
//				"META-INF/spring/smpoc-demo-mvc.xml",
//				"META-INF/spring/smpoc-demo-sm.xml",
		});
		Profiler.release();
	}
	
	protected void setUp(String path[]) throws Exception {
		this.applicationContext = new FileSystemXmlApplicationContext(path);
		//this.applicationContext = new ClassPathXmlApplicationContext(path);
		this.initPropertyByName();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		Profiler.release();
		String profilerDump = Profiler.dump("","<br/>").replace("   ", "&nbsp;&nbsp;&nbsp;");
		Loggers.PERF.warn(new java.util.Formatter().format("<br/>测试请求总执行时间: %dms, IGID: %s, Test:%s\n%s",
				Profiler.getDuration(),
				RequestHeaderXFireSoaGovInterceptor.getInvokeGlobalId(),
				this.getName() + ":" +this.getClass().getName(),
				profilerDump).toString());
	}
	
	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	protected void initPropertyByName() {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(this);
		for(PropertyDescriptor pd : pds) {
			if(pd.getWriteMethod()!=null) {
				Object value = null;
				try {
					value = this.getApplicationContext().getBean(pd.getName());
				} catch (Exception e) {
					//TODO info it
				}
				if(value==null) {
					continue;
				}
				try {
					PropertyUtils.setProperty(this, pd.getName(), value);
				} catch (Exception e) {
					//					TODO ERROR it
					
				} 
			}
		}
	}

}
