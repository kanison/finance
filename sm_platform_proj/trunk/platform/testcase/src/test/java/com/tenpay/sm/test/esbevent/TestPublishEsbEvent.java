/**
 * 
 */
package com.tenpay.sm.test.esbevent;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.EsbEventEngine;
import com.tenpay.sm.test.base.ContextSupportTestCase;

/**
 * @author Administrator
 *
 */
public class TestPublishEsbEvent extends ContextSupportTestCase {
	private EsbEventEngine esbEventEngine;
	
	public void testPublish() throws InterruptedException {
		for(int i=0;i<100;i++) {
			EsbEvent event = new EsbEvent();
			event.setEventData("hello" + i);
			event.setEventId("eventId " + i + ":" + System.currentTimeMillis());
			event.setEventType("testevent");
			esbEventEngine.publicEsbEvent(event, null,false);
		}
		synchronized(this) {
			this.wait();
		}
	}
	
	/**
	 * @return the esbEventEngine
	 */
	public EsbEventEngine getEsbEventEngine() {
		return esbEventEngine;
	}
	/**
	 * @param esbEventEngine the esbEventEngine to set
	 */
	public void setEsbEventEngine(EsbEventEngine esbEventEngine) {
		this.esbEventEngine = esbEventEngine;
	}
	
	
}
