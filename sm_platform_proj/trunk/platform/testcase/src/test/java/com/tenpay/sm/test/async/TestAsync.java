/**
 * 
 */
package com.tenpay.sm.test.async;

import com.tenpay.sm.test.base.ContextSupportTestCase;

/**
 * @author torryhong
 *
 */
public class TestAsync extends ContextSupportTestCase {
	private SimpleFacade simpleFacade;
	private SimpleFacade asyncSimpleFacade;
	private SimpleFacade esbAsyncSimpleFacade;
	public void testAsync() {
		this.asyncSimpleFacade.doSomething("hello sync");
		this.simpleFacade.doSomething("hello async");
		
		if(this.esbAsyncSimpleFacade!=null) {
			this.esbAsyncSimpleFacade.doSomething("hello esb");
		}
	}
	/**
	 * @return the asyncSimpleFacade
	 */
	public SimpleFacade getAsyncSimpleFacade() {
		return asyncSimpleFacade;
	}
	/**
	 * @param asyncSimpleFacade the asyncSimpleFacade to set
	 */
	public void setAsyncSimpleFacade(SimpleFacade asyncSimpleFacade) {
		this.asyncSimpleFacade = asyncSimpleFacade;
	}
	/**
	 * @return the simpleFacade
	 */
	public SimpleFacade getSimpleFacade() {
		return simpleFacade;
	}
	/**
	 * @param simpleFacade the simpleFacade to set
	 */
	public void setSimpleFacade(SimpleFacade simpleFacade) {
		this.simpleFacade = simpleFacade;
	}
	/**
	 * @return the esbAsyncSimpleFacade
	 */
	public SimpleFacade getEsbAsyncSimpleFacade() {
		return esbAsyncSimpleFacade;
	}
	/**
	 * @param esbAsyncSimpleFacade the esbAsyncSimpleFacade to set
	 */
	public void setEsbAsyncSimpleFacade(SimpleFacade esbAsyncSimpleFacade) {
		this.esbAsyncSimpleFacade = esbAsyncSimpleFacade;
	}
	
	
	
}
