/**
 * 
 */
package com.tenpay.sm.web.mashup.el;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * @author li.hongtl
 *
 */
public class MyVariableResolverImpl implements VariableResolver { 
	private Object target;
	public MyVariableResolverImpl(){
	}
	public MyVariableResolverImpl(Object target){
		this.target = target;
	}
	public Object resolveVariable(String name) throws ELException {
		if(this.target==null) {
			return null;
		}
		else if(this.target instanceof Map) {
			return ((Map)this.target).get(name);
		}
		else {
			try {
				return PropertyUtils.getProperty(this.target, name);
			} catch (Exception e) {
				throw new ELException("获取属性出错",e);
			} 
		}
	}
	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(Object target) {
		this.target = target;
	}
	
}
