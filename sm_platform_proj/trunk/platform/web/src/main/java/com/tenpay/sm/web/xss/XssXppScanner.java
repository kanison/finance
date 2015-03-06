/**
 * 
 */
package com.tenpay.sm.web.xss;

import com.tenpay.sm.lang.util.HtmlEncoder;

/**
 * @author torryhong
 *
 */
public class XssXppScanner {

	public String scan(String retValue) {
		return HtmlEncoder.encode(retValue);
	}

}
