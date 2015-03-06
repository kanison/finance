/**
 * 
 */
package com.tenpay.sm.test.esbevent;

import org.junit.Test;

import com.tenpay.sm.test.base.ContextSupportTestCase;

/**
 * @author Administrator
 *
 */

public class TestEventShow extends ContextSupportTestCase {
//	private EsbEventShowFrame esbEventShowFrame1;
//	private EsbEventShowFrame esbEventShowFrame2;
	private EsbEventShowFrame esbEventShowFrame3;
	
	//@org.junit.Test
	public void testShowFrame() throws InterruptedException {
//		if(esbEventShowFrame1!=null) {
//			esbEventShowFrame1.setVisible(true);
//		}
//		if(esbEventShowFrame2!=null) {
//			esbEventShowFrame2.setVisible(true);
//		}
		if(esbEventShowFrame3!=null) {
			esbEventShowFrame3.setVisible(true);
		}
		synchronized(this) {
			this.wait();
		}
	}
	
//	/**
//	 * @return the esbEventShowFrame1
//	 */
//	public EsbEventShowFrame getEsbEventShowFrame1() {
//		return esbEventShowFrame1;
//	}
//	/**
//	 * @param esbEventShowFrame1 the esbEventShowFrame1 to set
//	 */
//	public void setEsbEventShowFrame1(EsbEventShowFrame esbEventShowFrame1) {
//		this.esbEventShowFrame1 = esbEventShowFrame1;
//	}
//	/**
//	 * @return the esbEventShowFrame2
//	 */
//	public EsbEventShowFrame getEsbEventShowFrame2() {
//		return esbEventShowFrame2;
//	}
//	/**
//	 * @param esbEventShowFrame2 the esbEventShowFrame2 to set
//	 */
//	public void setEsbEventShowFrame2(EsbEventShowFrame esbEventShowFrame2) {
//		this.esbEventShowFrame2 = esbEventShowFrame2;
//	}

	public EsbEventShowFrame getEsbEventShowFrame3() {
		return esbEventShowFrame3;
	}

	public void setEsbEventShowFrame3(EsbEventShowFrame esbEventShowFrame3) {
		this.esbEventShowFrame3 = esbEventShowFrame3;
	}
	
	
}
