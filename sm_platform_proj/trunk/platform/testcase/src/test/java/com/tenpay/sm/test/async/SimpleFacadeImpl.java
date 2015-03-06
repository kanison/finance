/**
 * 
 */
package com.tenpay.sm.test.async;

/**
 * @author Administrator
 *
 */
public class SimpleFacadeImpl implements SimpleFacade {

	public void doSomething(String str) {
		System.out.println(str + "--SimpleFacadeImpl¡£¡£±»µ÷ÓÃ,thread:" + Thread.currentThread().getId() + "," + Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
