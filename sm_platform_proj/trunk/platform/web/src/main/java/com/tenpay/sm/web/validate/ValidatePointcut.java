/**
 * 
 */
package com.tenpay.sm.web.validate;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

/**
 * 对spring pojo web模块的方法的输入数据进行验证的切入点Pointcut
 * 对有Validate这个annotation的类和方法进行拦截的Pointcut
 * @author li.hongtl
 *
 */
public class ValidatePointcut implements Pointcut {
	protected AnnotationClassFilter annotationClassFilter = new AnnotationClassFilter(Validate.class,true);
	protected AnnotationMethodMatcher annotationMethodMatcher = new AnnotationMethodMatcher(Validate.class);
	/* (non-Javadoc)
	 * @see org.springframework.aop.Pointcut#getClassFilter()
	 */
	public ClassFilter getClassFilter() {
		return annotationClassFilter;
	}

	/* (non-Javadoc)
	 * @see org.springframework.aop.Pointcut#getMethodMatcher()
	 */
	public MethodMatcher getMethodMatcher() {
		return annotationMethodMatcher;
	}
}
