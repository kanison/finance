/**
 * 
 */
package com.tenpay.sm.web.module;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * @author li.hongtl
 * �������͵�ǰ��ģ���Ƿ�ƥ�䣬�Ƿ��Ƿ�����ǰģ������� ��һ�������pointCut
 * TODO http get����һ��ƥ�䣿post�ɲ����������⣿��һ���µ�annotation?
 */
public class ModuleMatchBeanPointCut implements Pointcut {
	//TODO ClassFilter �� RequestMapping ���annotation ������?
	protected AnnotationClassFilter annotationClassFilter = new AnnotationClassFilter(RequestMapping.class,true);
	protected AnnotationMethodMatcher annotationMethodMatcher = new AnnotationMethodMatcher(RequestMapping.class);
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
