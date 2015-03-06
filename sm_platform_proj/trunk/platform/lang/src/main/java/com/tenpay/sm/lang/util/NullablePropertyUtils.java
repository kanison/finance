/**
 * 
 */
package com.tenpay.sm.lang.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author torryhong
 *
 */
public class NullablePropertyUtils {
	public static void setNestedProperties(Map<String,Object> destBeans,Map<String,Object> sourceValues) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		for(String key : sourceValues.keySet()) {
			Object value = sourceValues.get(key);
			if(value!=null) {
				int firstDotIndex = key.indexOf('.');
				if(firstDotIndex<0) {
					destBeans.put(key, value);
					continue;
				}
				String beanKey = key.substring(0,firstDotIndex);
				Object bean = destBeans.get(beanKey);
				if(bean!=null) {
					String propertyName = key.substring(firstDotIndex+1);
					NullablePropertyUtils.setNestedProperty(bean, propertyName, value);
				}
				
			}
		}
	}
	
	public static Map<String,Object> getNestedProperties(Map<String,Object> sourceBeans) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(sourceBeans==null) {
			return null;
		}
		Map<String,Object> propertyValues = new TreeMap<String,Object>();
		for(String key : sourceBeans.keySet()) {
			Object value = sourceBeans.get(key);
			if(value==null) {
				continue;
			} else if(value.getClass().isArray()
				|| java.util.Collection.class.isAssignableFrom(value.getClass())
				|| java.util.Map.class.isAssignableFrom(value.getClass())) {
				//TODO 暂时不支持
				continue;
			}
			else if(value.getClass().isPrimitive() 
				|| java.lang.Number.class.isAssignableFrom(value.getClass())
				|| java.lang.CharSequence.class.isAssignableFrom(value.getClass())
				|| java.util.Date.class.isAssignableFrom(value.getClass())) {
				propertyValues.put(key, value);
			} 
			else {
				Map<String,Object> nextLevelValues = getNestedProperties(value);
				if(nextLevelValues!=null) {
					for(String keyInner : nextLevelValues.keySet()) {
						propertyValues.put(key + "." + keyInner, nextLevelValues.get(keyInner));
					} 
				}	
			}
		}
		return propertyValues;
	}
	
	public static void setNestedProperty(Object bean, String propertyName, Object value) 
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		int firstDotIndex = propertyName.indexOf('.');
		if(firstDotIndex<0) {
			BeanUtils.setProperty(bean, propertyName, value);
			return;
		} else {
			String firstPropertyName = propertyName.substring(0,firstDotIndex);
			Object firstProperty  = PropertyUtils.getProperty(bean, firstPropertyName);
			if(firstProperty==null) {
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, firstPropertyName);
				firstProperty = pd.getPropertyType().newInstance();
				PropertyUtils.setSimpleProperty(bean, firstPropertyName, firstProperty);
			}
			String nextLevelPropertyName = propertyName.substring(firstDotIndex + 1);
			setNestedProperty(firstProperty,nextLevelPropertyName,value);
		}
	}
	
	public static Map<String,Object> getNestedProperties(Object sourceBean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(sourceBean==null) {
			return null;
		}
		Map<String,Object> propertyValues = new TreeMap<String,Object>();
		
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(sourceBean);
		for(PropertyDescriptor pd : pds) {
			if(pd.getReadMethod()==null) {
				continue;
			}
			else if(pd.getPropertyType().equals(Class.class)) {
				continue;
			} 
			else if(pd.getPropertyType().isArray()
					|| java.util.Collection.class.isAssignableFrom(pd.getPropertyType())
					|| java.util.Map.class.isAssignableFrom(pd.getPropertyType())) {
				//TODO 暂时不支持
				continue;
			}
			else if(pd.getPropertyType().isPrimitive() 
					|| java.lang.Number.class.isAssignableFrom(pd.getPropertyType())
					|| java.lang.CharSequence.class.isAssignableFrom(pd.getPropertyType())
					|| java.util.Date.class.isAssignableFrom(pd.getPropertyType())) {
				Object value = PropertyUtils.getProperty(sourceBean, pd.getName());
				if(value!=null) {
					propertyValues.put(pd.getName(), value);
				}
			}
			else {
				Object nextLevelPropertyValue = PropertyUtils.getProperty(sourceBean, pd.getName());
				if(nextLevelPropertyValue!=null) {
					Map<String,Object> nextLevelPropertyValues = getNestedProperties(nextLevelPropertyValue);
					if(nextLevelPropertyValues!=null) {
						for(String key : nextLevelPropertyValues.keySet()) {
							propertyValues.put(pd.getName() + "." + key, nextLevelPropertyValues.get(key));
						}
					}
				}
			}
		}
		
		return propertyValues;
	}
	
}
