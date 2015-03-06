/**
 * 
 */
package com.tenpay.sm.web.validate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Conventions;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.tenpay.sm.web.model.AdditionalHandlerInterceptor;

/**
 * ������֤�����Map
 * ��Map�л�ȡ�����ʱ�����û��Errors���Զ�����һ��
 * ���û��ָ�� objectName ʹ�� Convention ����objectName
 * @author li.hongtl
 *
 */
public class ErrorsMap extends HashMap<String,Errors> {
	private static final long serialVersionUID = -4421894521160530054L;
	
	/**
	 * ��Map�л�ȡ�����ʱ�����û��Errors���Զ�����һ��
	 * @param object
	 * @param objectName
	 * @return
	 */
	public Errors getErrors(Object object,String objectName) {
		Errors errors = this.get(objectName);
		if(errors==null) {
			errors = new BeanPropertyBindingResult(object,objectName);
			this.put(objectName, errors);
			Map<String,Object> additionalModel = AdditionalHandlerInterceptor.getAdditionalModel();
			additionalModel.put(BindingResult.MODEL_KEY_PREFIX + errors.getObjectName(), errors);
		}
		return errors;
	}

	/**
	 * ��Map�л�ȡ�����ʱ�����û��Errors���Զ�����һ��
	 * ���û��ָ�� objectName ʹ�� Convention ����objectName
	 * @param object
	 * @return
	 */
	public Errors getErrors(Object object) {
		String objectName = Conventions.getVariableName(object);
		return this.getErrors(object, objectName);
	}
}
