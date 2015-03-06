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
 * 放了验证错误的Map
 * 从Map中获取错误的时候，如果没有Errors，自动创建一个
 * 如果没有指定 objectName 使用 Convention 命名objectName
 * @author li.hongtl
 *
 */
public class ErrorsMap extends HashMap<String,Errors> {
	private static final long serialVersionUID = -4421894521160530054L;
	
	/**
	 * 从Map中获取错误的时候，如果没有Errors，自动创建一个
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
	 * 从Map中获取错误的时候，如果没有Errors，自动创建一个
	 * 如果没有指定 objectName 使用 Convention 命名objectName
	 * @param object
	 * @return
	 */
	public Errors getErrors(Object object) {
		String objectName = Conventions.getVariableName(object);
		return this.getErrors(object, objectName);
	}
}
