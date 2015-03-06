/**
 * 
 */
package com.tenpay.sm.web.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.util.NullablePropertyUtils;


/**
 * @author torryhong
 *
 */
public class ServiceGate implements BeanFactoryAware {
	private BeanFactory beanFactory;
	private Set<String> exportedBeanNames = new HashSet<String>();
	
	@RequestMapping
	public Object execute() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		Context context = ContextUtil.getContext();
		Map<String,String[]> paramArrayMaps = context.getRequestParamsMap();
		
		//希望service参数用get方式提交
		String service = paramArrayMaps.get("service")[0];
		int dotIndex = service.indexOf('.');
		String beanName = service.substring(0,dotIndex);
		//这个bean没有export成rest服务，不支持。
		if(!this.exportedBeanNames.contains(beanName)) {
			return null;
		}
		
		String methodName = service.substring(dotIndex+1);
		
		Object serviceBean = this.beanFactory.getBean(beanName);
		Method method = org.springframework.beans.BeanUtils.findMethodWithMinimalParameters(
				serviceBean.getClass(),methodName);
		
		Object[] args = this.initArgs(method,paramArrayMaps);
		
		Object returnValue = method.invoke(serviceBean, args);
		
		return returnValue;
	}

	private Object[] initArgs(Method method, Map<String, String[]> paramArrayMaps) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<String,Object> paramMaps = new HashMap<String,Object>();
		for(String paramKey : paramArrayMaps.keySet()) {
			if(!"service".equals(paramKey)) {
				String[] values = paramArrayMaps.get(paramKey);
				if(values!=null && values.length>0) {
					paramMaps.put(paramKey, values[0]);
				}
			}
		}
		
		Map<String,Object> destParamBeans = new HashMap<String,Object>(); 
		Object[] args = new Object[method.getParameterTypes().length];
		int index = 0;
		for(Class paramClass : method.getParameterTypes()) {
			Object obj = paramClass.newInstance();
			destParamBeans.put(ClassUtils.getShortNameAsProperty(paramClass), obj);
			args[index++] = obj;
		}
		
		NullablePropertyUtils.setNestedProperties(destParamBeans,paramMaps);
		
		return args;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public Set<String> getExportedBeanNames() {
		return exportedBeanNames;
	}

	public void setExportedBeanNames(Set<String> exportedBeanNames) {
		this.exportedBeanNames = exportedBeanNames;
	}
	
	
}
