/**
 * 
 */
package com.tenpay.sm.test.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.binding.convert.support.TextToExpression;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.binding.expression.support.BeanWrapperExpressionParser;
import org.springframework.binding.expression.support.OgnlExpressionParser;
import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.binding.method.MethodSignature;
import org.springframework.binding.method.TextToMethodSignature;

import com.tenpay.sm.test.base.ContextSupportTestCase;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestTextToMethodSignature extends TestCase {
	public void testTextToMethodSignature() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		TextToMethodSignature ttms = new TextToMethodSignature();
		DefaultConversionService dcs = new DefaultConversionService();
		dcs.addConverter(new AbstractConverter() {
			@Override
			protected
			Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
				if(source==null) {
					return null;
				}
				if(source instanceof String) {
					return new StaticExpression((String)source);
				}
				return null;
			}

			public Class[] getSourceClasses() {
				return new Class[] { String.class };
			}

			public Class[] getTargetClasses() {
				return new Class[] { Expression.class };
			}
		});
		
		ttms.setConversionService(dcs);
		MethodSignature methodSignature = (MethodSignature)ttms.convert("methodToInvoke()",
				TestTextToMethodSignature.class, null);
		Method method = TestTextToMethodSignature.class.getMethod(
				methodSignature.getMethodName(),methodSignature.getParameters().getTypesArray());
		method.invoke(this, null);
		
		methodSignature = (MethodSignature)ttms.convert("methodToInvoke(java.lang.String who)",
				TestTextToMethodSignature.class, null);
		method = TestTextToMethodSignature.class.getMethod(
				methodSignature.getMethodName(),methodSignature.getParameters().getTypesArray());
		method.invoke(this, new Object[]{"tom"});
	}
	
	public void methodToInvoke() {
		System.out.println("hello, i'm here!");
	}
	
	public void methodToInvoke(String who) {
		System.out.println("hi, " + who + " is here?");
	}
}
