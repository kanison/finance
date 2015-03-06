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
 * �������͵�ǰ��ģ���Ƿ�ƥ�䣬�Ƿ��Ƿ�����ǰģ�������
 * TODO http get����һ��ƥ�䣿post�ɲ����������⣿��һ���µ�annotation?
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
			 * TODO review �Ƿ�ȫ
			 */
			return invocation.proceed();
		}
		
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		//��ƥ������� �� http get���� �����÷�����
		if(wc.isMatchRequest()) {
			return invocation.proceed();
		}
		else {
			return null;
		}
	}

}
