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
 * ��spring pojo webģ��ķ������������ݽ�����֤�������Pointcut
 * ����Validate���annotation����ͷ����������ص�Pointcut
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
