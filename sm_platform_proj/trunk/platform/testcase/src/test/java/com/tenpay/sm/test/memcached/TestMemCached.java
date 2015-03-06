/**
 * 
 */
package com.tenpay.sm.test.memcached;

import com.tenpay.sm.cache.Cache;
import com.tenpay.sm.test.base.ContextSupportTestCase;

import junit.framework.TestCase;

/**
 * @author Administrator
 * 
 */
public class TestMemCached extends ContextSupportTestCase {
	private Cache javaMemCachedWrapper;
	private Cache spyMemCachedWrapper;

	public void testSpyMemCachedWrapper() {
		this.doPutAndGet(this.spyMemCachedWrapper);
	}
	
	public void testJavaMemCachedWrapper() {
		this.doPutAndGet(this.javaMemCachedWrapper);
	}



	public void doPutAndGet(Cache cache) {
		long s1 = System.currentTimeMillis();
		for(int i=0;i<10000;i++) {
			cache.put(String.valueOf(i), String.valueOf(i));
		}
		long s2 = System.currentTimeMillis();
		System.out.println(s2-s1);
		for(int i=2000;i<4000;i++) {
			cache.put(String.valueOf(i), String.valueOf(i));
		}
		long s3 = System.currentTimeMillis();
		System.out.println(s3-s2);
		for(int i=0;i<10000;i++) {
			String b = (String)cache.get(String.valueOf(i));
			org.junit.Assert.assertEquals(String.valueOf(i), b);
		}
		long s4 = System.currentTimeMillis();
		System.out.println(s4-s3);
	}

	/**
	 * @return the javaMemCachedWrapper
	 */
	public Cache getJavaMemCachedWrapper() {
		return javaMemCachedWrapper;
	}

	/**
	 * @param javaMemCachedWrapper
	 *            the javaMemCachedWrapper to set
	 */
	public void setJavaMemCachedWrapper(Cache javaMemCachedWrapper) {
		this.javaMemCachedWrapper = javaMemCachedWrapper;
	}

	/**
	 * @return the spyMemCachedWrapper
	 */
	public Cache getSpyMemCachedWrapper() {
		return spyMemCachedWrapper;
	}

	/**
	 * @param spyMemCachedWrapper
	 *            the spyMemCachedWrapper to set
	 */
	public void setSpyMemCachedWrapper(Cache spyMemCachedWrapper) {
		this.spyMemCachedWrapper = spyMemCachedWrapper;
	}

}
