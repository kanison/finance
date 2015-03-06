package com.app.aop.aspect;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.tenpay.sm.common.lang.diagnostic.Profiler;

@Aspect
public class LogMethod implements Ordered {

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.app.aop.annotation.LogMethod)")
	public void allAddMethod() {
	};

	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 * 
	 */
	@Around("allAddMethod()")
	public Object logMethodWithEx(ProceedingJoinPoint joinPoint)
			throws Throwable {
		return logMethod(joinPoint, true);
	}

	public static Object logMethod(ProceedingJoinPoint joinPoint, boolean logEx)
			throws Throwable {
		Log LOG = LogFactory.getLog(joinPoint.getTarget().getClass());
		String methodName = joinPoint.getSignature().getName();
		Object args[] = joinPoint.getArgs();
		StringBuffer sb = new StringBuffer();
		sb.append(methodName + " params:");
		if (args.length < 10000) {
			for (int i = 0; i < args.length; i++) {
				sb.append(" 参数");
				sb.append(i);
				sb.append(':');
				Object obj = args[i];
				String printStr = null;
				if (obj != null) {
					if (obj instanceof CharSequence || obj instanceof Number
							|| obj instanceof Character
							|| obj instanceof Boolean
							|| obj instanceof java.util.Date) {
						printStr = obj.toString();
					} else {
						printStr = ToStringBuilder.reflectionToString(obj,
								ToStringStyle.SHORT_PREFIX_STYLE);
					}
					if (printStr != null && printStr.length() > 1073741824)
						printStr = "参数太长，超过1MB";
				}
				sb.append(printStr);
			}
		} else {
			sb.append("参数太多，超过10000");
		}
		LOG.info(sb.toString());
		Object result;
		Profiler.enter(methodName);
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			if (logEx)
				LOG.warn(methodName + " exception: ", t);
			throw (t);
		} finally {
			Profiler.release();
		}
		String printStr = null;
		if (result != null) {
			if (result instanceof CharSequence || result instanceof Number
					|| result instanceof Character || result instanceof Boolean
					|| result instanceof java.util.Date) {
				printStr = result.toString();
			} else {
				printStr = ToStringBuilder.reflectionToString(result,
						ToStringStyle.SHORT_PREFIX_STYLE);
			}
		}
		LOG
				.info(methodName + " result: "
						+ printStr);
		return result;
	}

	public int getOrder() {
		return -1;
	}
}
