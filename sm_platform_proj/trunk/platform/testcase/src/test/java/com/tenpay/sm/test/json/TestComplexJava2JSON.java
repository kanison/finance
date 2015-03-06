/**
 * 
 */
package com.tenpay.sm.test.json;

import java.math.BigDecimal;


import com.tenpay.sm.lang.json.JSONException;
import com.tenpay.sm.lang.json.JSONObject;
import com.tenpay.sm.test.domain.PayOrder;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestComplexJava2JSON extends TestCase {
	public void testJava2JSON() {
        PayOrder po = PayOrder.getDefaultDemoPayOrder();
        JSONObject js = new JSONObject(po);
        String str = js.toString();
        System.out.println(str);
	}
	
	public void testJSON2JSON() {
        PayOrder po = PayOrder.getDefaultDemoPayOrder();
        JSONObject js = new JSONObject(po);
        js = new JSONObject(js);
        String str = js.toString();
        System.out.println(str);
	}
	
	public void testStatic() throws SecurityException, NoSuchMethodException {
		java.lang.reflect.Method method = PayOrder.class.getMethod("getDefaultDemoPayOrder", (Class[])null);
		System.out.println(method);
		JSONObject js = new JSONObject(method);
        String str = js.toString();
        System.out.println(str);
	}
	
	public void testStandardProperty() throws JSONException {
		JSONObject js = new JSONObject((Object)"this is \\a string.");
        String str = js.toString();
        System.out.println(str);
        System.out.println(new JSONObject((Object)new BigDecimal("3.14")).toString());
	}
}
