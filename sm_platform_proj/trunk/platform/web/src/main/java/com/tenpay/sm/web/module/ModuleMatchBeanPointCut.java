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
 * 检查请求和当前的模块是否匹配，是否是发到当前模块的请求 的一个切入点pointCut
 * TODO http get请求一律匹配？post可不可以有例外？用一个新的annotation?
 */
public class ModuleMatchBeanPointCut implements Pointcut {
	//TODO ClassFilter 用 RequestMapping 这个annotation 合适吗?
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
