package com.zhaocb.common.aop.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.error.ErrorCode;

@Aspect
public class DisplayError implements Ordered {

	private static final Log LOG = LogFactory.getLog(DisplayError.class);

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.DisplayError)")
	public void allAddMethod() {
	};

	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 * 
	 */
	@Around("allAddMethod()")
	public Object display(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			return joinPoint.proceed();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			CommonUtil.displayError(ErrorCode.code(e.getMessage()));
			return null;
		}
	}

	public int getOrder() {
		return -1;
	}
}
