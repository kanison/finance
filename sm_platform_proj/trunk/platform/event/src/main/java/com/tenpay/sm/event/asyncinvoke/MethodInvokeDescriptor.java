/**
 * 
 */
package com.tenpay.sm.event.asyncinvoke;

/**
 * @author Administrator
 *
 */
public class MethodInvokeDescriptor implements java.io.Serializable {
	private static final long serialVersionUID = 2030619539735799094L;
	
	private String beanName;
	private String methodName;
	private Class[] types;
	private Object[] arguments;

	/**
	 * @return the arguments
	 */
	public Object[] getArguments() {
		return arguments;
	}
	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}
	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the types
	 */
	public Class[] getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(Class[] types) {
		this.types = types;
	}
	
	
}
