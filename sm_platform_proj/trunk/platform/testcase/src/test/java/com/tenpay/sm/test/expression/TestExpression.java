/**
 * 
 */
package com.tenpay.sm.test.expression;

import java.util.HashMap;
import java.util.Map;

import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.binding.expression.support.BeanWrapperExpressionParser;
import org.springframework.binding.expression.support.OgnlExpressionParser;

import com.tenpay.sm.test.domain.PayOrder;
import com.tenpay.sm.web.mashup.el.ElExpressionParser;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 * 
 */
public class TestExpression extends TestCase {
	PayOrder payOrder;
	Map map = new HashMap();
	ExpressionParser ep;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		payOrder = PayOrder.getDefaultDemoPayOrder();
		map.put("po", payOrder);
	}
	
	protected void evalCommon(ExpressionParser ep) {
		SettableExpression ex = null;
		Object result = null;
		
		ex = ep.parseSettableExpression("${payCustomer.firstName}");
		result = ex.evaluate(payOrder, null);
		System.out.println(result);
		


	}
	
	public void testBeanWrap() {
		ExpressionParser ep = new BeanWrapperExpressionParser();
		evalCommon(ep);
	}
	
	public void testOgnl() {
		ExpressionParser ep = new OgnlExpressionParser();
		evalCommon(ep);
		
		SettableExpression ex = null;
		Object result = null;
		
		ex = ep.parseSettableExpression("${po.payCustomer.firstName}");
		result = ex.evaluate(map, null);
		System.out.println(result);
		
		ex = ep.parseSettableExpression("${payCustomer.age>30}");
		result = ex.evaluate(payOrder, null);
		System.out.println(result);
		
		ex = ep.parseSettableExpression("${payCustomer.age<=30}");
		result = ex.evaluate(payOrder, null);
		System.out.println(result);
	}
	
	public void testEl() {
		ExpressionParser ep = new ElExpressionParser();
		evalCommon(ep);
		
		SettableExpression ex = null;
		Object result = null;
		
		ex = ep.parseSettableExpression("${po.payCustomer.firstName}");
		result = ex.evaluate(map, null);
		System.out.println(result);
		
		ex = ep.parseSettableExpression("${payCustomer.age>30}");
		result = ex.evaluate(payOrder, null);
		System.out.println(result);
		
		ex = ep.parseSettableExpression("${payCustomer.age<=30}");
		result = ex.evaluate(payOrder, null);
		System.out.println(result);
	}
	

	public void testEL() {

	}
}
