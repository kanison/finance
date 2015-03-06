/**
 * 
 */
package com.tenpay.sm.lang.extend;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

import com.tenpay.sm.lang.util.ExceptionUtil;

/**
 * ��Bean������ֵ������չ
 * ֧��Map��Collection��Array���͵����Խ�����չ
 * ���Ա�����get��set����
 * @author li.hongtl
 *
 */
public class ValueExtension extends Extension {
	private static final Log LOG = LogFactory.getLog(ValueExtension.class);
	private static final long serialVersionUID = -7929873978263279808L;
	
	/**
	 * ��չ���������������չ��bean����������
	 * ֧��PropertyUtils.getNestedProperty�ĸ�ʽ������xx, xx.xxx��xx[3]��xx(yyy)�ĸ�ʽ
	 */
	protected String extensionPointProperty;
	/**
	 * �������չ������(��չ��)��Map���͵ģ�������չ�����key
	 */
	private String extensionKey;
	/**
	 * ��չ��ֵ
	 */
	private Object extensionValue = this;
	
	
	@Override
	public void registExtensionToExtensionpPoint(Object theExtensionpPointBean) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("registExtensionToExtensionpPoint, theExtensionPointBean: " + theExtensionpPointBean);
		}
		if(theExtensionpPointBean==null || extensionPointProperty==null || extensionValue==null) {
			return;
		}
		try {
			Object extensionpPoint = PropertyUtils.getNestedProperty(
			//Object extensionpPoint = PropertyUtils.getProperty(
					theExtensionpPointBean, extensionPointProperty);
			Class propertyType = null;
			if(extensionpPoint==null) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("extensionpPointֵΪ��, theExtensionPointBean: " + theExtensionpPointBean);
				}
				//init the Proprety��Σ���𣬺�����?
				PropertyDescriptor propertyDescriptor = PropertyUtils
					.getPropertyDescriptor(theExtensionpPointBean, extensionPointProperty);
				if(propertyDescriptor.getReadMethod()==null || propertyDescriptor.getWriteMethod()==null) {
					return;
				}
				propertyType = propertyDescriptor.getReadMethod().getReturnType();
				if(propertyType.isArray()) {
					extensionpPoint = Array.newInstance(propertyType.getComponentType(),0);
				}
				else if(propertyType.isAssignableFrom(HashMap.class)) {
					extensionpPoint =  new HashMap();
				}
				else if(propertyType.isAssignableFrom(Hashtable.class)) {
					extensionpPoint =  new Hashtable();
				}
				else if(propertyType.isAssignableFrom(ArrayList.class)) {
					extensionpPoint =  new ArrayList();
				}
				else if(propertyType.isAssignableFrom(HashSet.class)) {
					extensionpPoint =  new HashSet();
				}
				else if(propertyType.isAssignableFrom(TreeSet.class)) {
					extensionpPoint =  new TreeSet();
				}
				else if(propertyType.isAssignableFrom(Vector.class)) {
					extensionpPoint =  new Vector();
				}
				if(extensionpPoint==null) {
	//				TODO log warn
					LOG.warn("extensionpPointֵΪ�գ����ܳ�ʼ����������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
					return;
				}
				else {
					propertyType = extensionpPoint.getClass();
					if(!propertyType.isArray()) {
						if(LOG.isDebugEnabled()) {
							LOG.debug("��ʼ���ü�������bean������,��������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
						}
						PropertyUtils.setNestedProperty(theExtensionpPointBean, extensionPointProperty,extensionpPoint);
					}
				}
			}
				
			Object newArrayValue = null;
			propertyType = extensionpPoint.getClass();
			if (propertyType.isArray()) {
				newArrayValue = Array.newInstance(extensionpPoint.getClass().getComponentType(),
						Array.getLength(extensionpPoint) + 1);
				for (int index = 0; index < Array.getLength(extensionpPoint); index++) {
					Array.set(newArrayValue, index, Array.get(extensionpPoint,index));
				}
				Array.set(newArrayValue, Array.getLength(newArrayValue) - 1,extensionValue);
				if(LOG.isDebugEnabled()) {
					LOG.debug("��ʼ�����������飬����bean������,��������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
				}
				PropertyUtils.setNestedProperty(theExtensionpPointBean,extensionPointProperty, newArrayValue);
			} else if (extensionpPoint instanceof Map && extensionKey != null) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("Map��������������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
				}
				((Map) extensionpPoint).put(extensionKey, extensionValue);
			} else if (extensionpPoint instanceof Dictionary && extensionKey != null) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("Dictionary��������������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
				}				
				((Dictionary) extensionpPoint).put(extensionKey, extensionValue);
			} else if (extensionpPoint instanceof Collection) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("Collection��������������:" + extensionPointProperty + " , theExtensionPointBean: " + theExtensionpPointBean);
				}				
				((Collection) extensionpPoint).add(extensionValue);
			}
			//			
			// if(newArrayValue!=null) {
			// PropertyUtils.setNestedProperty(theExtensionpPointBean,
			// extensionPointProperty,newArrayValue);
			// }
			// else if (extensionpPoint!=null) {
			// PropertyUtils.setNestedProperty(theExtensionpPointBean,
			// extensionPointProperty,extensionpPoint);
			// }
			// else {
			//				//TODO log warn
			//				return;
			//			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapException(e);
		} 
		
	}
	
	/**
	 * @return the extensionKey
	 */
	public String getExtensionKey() {
		return extensionKey;
	}
	/**
	 * @param extensionKey the extensionKey to set
	 */
	public void setExtensionKey(String key) {
		this.extensionKey = key;
	}
	/**
	 * @return the extensionValue
	 */
	public Object getExtensionValue() {
		return extensionValue;
	}
	/**
	 * @param extensionValue the extensionValue to set
	 */
	public void setExtensionValue(Object value) {
		this.extensionValue = value;
	}
	
	public String getExtensionPointProperty() {
		return extensionPointProperty;
	}

	public void setExtensionPointProperty(String extensionPointProperty) {
		this.extensionPointProperty = extensionPointProperty;
	}
	
}
