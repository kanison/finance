/**
 * 
 */
package com.tenpay.sm.web.module;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;

/**
 * @author li.hongtl
 * 检查请求和当前的模块是否匹配，是否是发到当前模块的请求
 * TODO http get请求一律匹配？post可不可以有例外？用一个新的annotation?
 */
public class ModuleMatchBeanMethodInterceptor implements
		MethodInterceptor {

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Context ctx = ContextUtil.getContext();
		if(!(ctx instanceof WebModuleContext)) {
			/**
			 * TODO review 是否安全
			 */
			return invocation.proceed();
		}
		
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		//对匹配的请求 或 http get方法 都调用方法？
		if(wc.isMatchRequest()) {
			return invocation.proceed();
		}
		else {
			return null;
		}
	}

}
