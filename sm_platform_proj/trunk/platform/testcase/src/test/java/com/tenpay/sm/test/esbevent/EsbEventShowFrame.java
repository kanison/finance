/**
 * 
 */
package com.tenpay.sm.test.esbevent;

import java.awt.Frame;
import java.awt.event.WindowEvent;

import com.tenpay.sm.event.EsbEvent;
import com.tenpay.sm.event.listener.EsbEventListener;

/**
 * @author Administrator
 *
 */

public class EsbEventShowFrame extends Frame implements EsbEventListener {
	private java.awt.List list = new java.awt.List();
	public EsbEventShowFrame() {
		this.add(list);
		this.setSize(400, 300);
		this.setTitle("hello");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				EsbEventShowFrame.this.dispose();
			}
		});
	}
	
	public void onEvent(EsbEvent esbEvent) {
		this.list.add(esbEvent.getEventId() + ":" + esbEvent.getEventType() + ":" + esbEvent.getEventData());
		System.out.print(esbEvent);
	}
}
