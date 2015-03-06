/**
 * 
 */
package com.tenpay.sm.test.property;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;

import com.tenpay.sm.lang.util.NullablePropertyUtils;
import com.tenpay.sm.test.domain.IfbItem;
import com.tenpay.sm.test.domain.PayOrder;

import junit.framework.TestCase;

/**
 * @author torryhong
 *
 */
public class TestPropertiesNestedUtil extends TestCase {
	public void ntestApachePropertiesUtil() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PayOrder payOrder = new PayOrder();
		PropertyUtils.setNestedProperty(payOrder, "payCustomer.lastName", "jack");
		junit.framework.Assert.assertEquals("jack", payOrder.getPayCustomer().getLastName());
	}
	
	public void testPropertiesUtil() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		PayOrder payOrder = new PayOrder();
		NullablePropertyUtils.setNestedProperty(payOrder, "payCustomer.lastName", "jack");
		junit.framework.Assert.assertEquals("jack", payOrder.getPayCustomer().getLastName());
		
		NullablePropertyUtils.setNestedProperty(payOrder, "payCustomer.lastPurchaseItem.quantity", "3");
		junit.framework.Assert.assertEquals(3, payOrder.getPayCustomer().getLastPurchaseItem().getQuantity());
		
		Map properties = NullablePropertyUtils.getNestedProperties(payOrder);
		System.out.println(properties);
	}
	
	public void testMultiBeanPropertiesUtil() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		Map<String,Object> sourceValues = new TreeMap<String,Object>();
		sourceValues.put("payOrder.payCustomer.age", "28");
		sourceValues.put("payOrder.payCustomer.lastName", "jack");
		sourceValues.put("payOrder.payCustomer.lastPurchaseItem.quantity", "3");
		sourceValues.put("ifbItem.itemNo", "123");
		sourceValues.put("ifbItem.itemName", "abc");
		System.out.println(sourceValues);
		
		Map<String,Object> beans = new TreeMap<String,Object>();
		beans.put("payOrder", new PayOrder());
		beans.put("ifbItem", new IfbItem());
		
		NullablePropertyUtils.setNestedProperties(beans, sourceValues);
		
		Map<String,Object> returnValues = NullablePropertyUtils.getNestedProperties(beans);
		System.out.println(returnValues);
		
		junit.framework.Assert.assertEquals(sourceValues.toString(), returnValues.toString());
	}
	
}
