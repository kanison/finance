/**
 * 
 */
package com.tenpay.sm.web.view;

/**
 * @author torryhong
 * 
 */
public enum ViewType {
	XML, JSON, PULL, REST, JHTM, VHTM, HTM, API, RAW, CGI, XLS, RELAY, OTHER;

	public boolean isRenderHTML() {
		return JHTM.equals(this) || HTM.equals(this) || VHTM.equals(this);
	}
}
