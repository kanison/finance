/**
 * 
 */
package com.tenpay.sm.test.xo;

import java.util.HashMap;
import java.util.Map;

import com.tenpay.sm.test.domain.PayOrder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestXStream extends TestCase {
	public void testXO() {
		PayOrder obj = PayOrder.getDefaultDemoPayOrder();
		//XStream xstream = new XStream();
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		String xml = xstream.toXML(obj);
		System.out.println(xml);
		
		Map m = new HashMap();
		m.put("x", "xxxxxxxx");
		m.put("y", 3);
		xml = xstream.toXML(m);
		System.out.println(xml);
	}
}
