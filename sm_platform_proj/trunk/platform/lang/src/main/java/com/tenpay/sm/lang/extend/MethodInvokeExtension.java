/**
 * 
 */
package com.tenpay.sm.lang.extend;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.binding.method.MethodSignature;
import org.springframework.binding.method.TextToMethodSignature;
import org.springframework.util.StringUtils;

import com.tenpay.sm.lang.util.ExceptionUtil;

/**
 * 通过对bean的方法调用，对bean进行扩充
 * 可以有两种方式使用：
 * 1、指定 methodSignature ，例如 xxxTheMethodName(java.lang.String who, int a) --必须要写参数行，可以任意命名，没有意义
 * 2、指定 methodName(例如:xxxTheMethodName)和parameterTypes(例如java.lang.String, int)不用写参数行，类型之间用逗号隔开
 * 如果有指定第1中方式优先
 * @author li.hongtl
 *
 */
public class MethodInvokeExtension extends Extension {
	private static final Log LOG = LogFactory.getLog(MethodInvokeExtension.class);
	private static final long serialVersionUID = -8614145339731410303L;
	private String methodSignature;
	private String methodName;
	private String parameterTypes;
	private Object[] arguments;
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.extend.Extension#registExtensionToExtensionpPoint()
	 */
	@Override
	public void registExtensionToExtensionpPoint(Object theExtensionpPointBean) {		
		try {
			Method method = this.getMethod(theExtensionpPointBean);
			method.invoke(theExtensionpPointBean, this.arguments);
		} catch(Exception ex) {
			LOG.error("注册方法扩展出错,theExtensionPointBean: " + theExtensionpPointBean,ex);
			throw ExceptionUtil.wrapException(ex);
		}
	}
	
	protected Method getMethodFromMethodSignature(final Object theExtensionpPointBean) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("getMethodFromMethodSignature: " + methodSignature + ", theExtensionPointBean: " + theExtensionpPointBean);
		}
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
		
		try {
			MethodSignature ms = (MethodSignature)ttms.convert(methodSignature,
					theExtensionpPointBean.getClass(), null);
			Method method = theExtensionpPointBean.getClass().getMethod(
					ms.getMethodName(),ms.getParameters().getTypesArray());
			return method;
		} catch(Exception ex) {
			throw ExceptionUtil.wrapException(ex);
		}
	}
	
	protected Method getMethodFromMethodNameAndParameterTypes(final Object theExtensionpPointBean) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("getMethodFromMethodNameAndParameterTypes: " + methodSignature + " ,theExtensionPointBean: " + theExtensionpPointBean);
		}
		try {
			String[] paramTypeArray = StringUtils.split(this.parameterTypes, ",");
			Class[] types = new Class[paramTypeArray.length];
			for(int index=0; index<paramTypeArray.length; index++) {
				Class clazz = Class.forName(paramTypeArray[index].trim());
				types[index] = clazz;
			}
			Method method = theExtensionpPointBean.getClass().getMethod(
					this.methodName,types);
			return method;
		} catch(Exception ex) {
			throw ExceptionUtil.wrapException(ex);
		}			
	}
	
	protected Method getMethod(final Object theExtensionpPointBean) {
		if(StringUtils.hasText(this.methodSignature)) {
			return this.getMethodFromMethodSignature(theExtensionpPointBean);
		}
		else {
			return this.getMethodFromMethodNameAndParameterTypes(theExtensionpPointBean);
		}
	}
	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String functionsignature) {
		this.methodSignature = functionsignature;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(String parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	
	
}
