/**
 * 
 */
package com.tenpay.sm.web.validate;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.model.AdditionalHandlerInterceptor;

/**
 * 对spring pojo web模块的方法的输入数据进行验证的方法拦截器MethodInterceptor,Interceptor,Advice
 * @author li.hongtl
 *
 */
public class ValidateMethodInterceptor implements MethodInterceptor {
	public static final String CURRENT_VALIDATE_ERRORS_MAP_KEY = ValidateMethodInterceptor.class.getName() + ".VALIDATE_ERRORS_MAP";
	private Map<String,Validator> validators;
	
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 * 拦截方法进行验证，验证不通过不调用方法，返回null
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(validators==null || validators.size()==0) {
			return invocation.proceed();
		}
		if (!invocation.getMethod().isAnnotationPresent(Validate.class)) {
			return invocation.proceed();
		}
		Validate validate = AnnotationUtils.findAnnotation(invocation.getMethod(), Validate.class);
		if(validate.value()==null || validate.value().length==0) {
			return invocation.proceed();
		}
		//TODO to get method parameter annotation
		ErrorsMap errorsMap = (ErrorsMap)
			ContextUtil.getContext().getCurrentAttribute(CURRENT_VALIDATE_ERRORS_MAP_KEY);
		for(Object arg : invocation.getArguments()) {
			if(arg==null) {
				//为空的不能验证，算通过?
				continue;
			}
			else if(arg instanceof Errors) {
				continue;
			}
			
			String objectName = Conventions.getVariableName(arg);
			Errors errors = new BeanPropertyBindingResult(arg,objectName);
			errorsMap.put(objectName,errors);
			for(String validateName : validate.value()) {
				Validator validator = validators.get(validateName);
				if(validator!=null) {
					if (validator.supports(arg.getClass())) {
						validator.validate(arg, errors);
					}
					// TODO 不支持的情况要不要报错或记录日志;
				}
			}
			if(errors.hasErrors()) {
				Map<String,Object> additionalModel = AdditionalHandlerInterceptor.getAdditionalModel();
				additionalModel.put(BindingResult.MODEL_KEY_PREFIX + errors.getObjectName(), errors);
			}
			
		}
		
		for(Errors errors : errorsMap.values()) {
			if(errors.hasErrors()) {
				return null;
			}
		}
		
		return invocation.proceed();
	}

	public Map<String,Validator> getValidators() {
		return validators;
	}

	public void setValidators(Map<String,Validator> validators) {
		this.validators = validators;
	}
}
