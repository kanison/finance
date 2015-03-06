/**
 * 
 */
package com.tenpay.sm.test.xo;

import java.io.IOException;

import com.tenpay.sm.lang.error.ErrorCode;
import com.tenpay.sm.lang.xo.XMLSerializer;
import com.tenpay.sm.test.domain.PayOrder;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestXMLSerializer extends TestCase {
	public void testXO() throws IOException {
		PayOrder obj = PayOrder.getDefaultDemoPayOrder();
		ErrorCode errorCode = ErrorCode.code("i18npay", "PARAM_CHECK_ERROR", new String[]{"order_date","order_no"});
		XMLSerializer.serialize(obj, new java.io.FileOutputStream("d:/1.xml"), "theOrder", true,"UTF-8");
		XMLSerializer.serialize(obj, new java.io.FileOutputStream("d:/2.xml"), "theOrder", false,"UTF-8");
		XMLSerializer.serialize(errorCode, new java.io.FileOutputStream("d:/3.xml"), "errorCode", false,"UTF-8");
		XMLSerializer.serialize(ErrorCode.success(), new java.io.FileOutputStream("d:/4.xml"), "errorCode", false,"UTF-8");
	}
}
