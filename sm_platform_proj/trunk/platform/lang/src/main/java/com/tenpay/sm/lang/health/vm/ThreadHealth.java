/**
 * 
 */
package com.tenpay.sm.lang.health.vm;

import com.tenpay.sm.lang.health.Health;

/**
 * @author torryhong
 *
 */
public class ThreadHealth implements Health {

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.health.Health#check()
	 */
	public Object check() {
		ThreadStatus threadStatus = new ThreadStatus();
		threadStatus.setActiveCountOfCurrentGroup(Thread.activeCount());
		return threadStatus;
	}

}
