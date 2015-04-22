package com.zhaocb.common.aop.aspect;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.app.utils.CommonUtil;
import com.zhaocb.common.aop.annotation.SetHeaderCacheControl;
import com.zhaocb.common.aop.annotation.SetHeaderCacheControl.CacheTime;
import com.zhaocb.common.aop.aspect.cachemethod.CacheMethod;

@Aspect
public class SetHeaderCacheControlAspect {
	private static final Log LOG = LogFactory.getLog(CacheMethod.class);
	
	/***
	 * 切点 　　
	 * @throws Exception 
	 */
	@Pointcut("@annotation(com.tenpay.common.aop.annotation.SetHeaderCacheControl)")
	public void allAddMethod() {
	};
	
	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 * 
	 */
	@Around("allAddMethod()")
	public Object setHeaderCacheControl(ProceedingJoinPoint joinPoint)
			throws Throwable {
		Object retObj = joinPoint.proceed();
		try {
			// 获取注解的值
			SetHeaderCacheControl setHeaderCacheControl = ((MethodSignature) joinPoint
					.getSignature()).getMethod().getAnnotation(
					SetHeaderCacheControl.class);
			Long seconds = getCacheAge(setHeaderCacheControl.value());
			if (seconds != null) {
				HttpServletResponse response = CommonUtil
						.getHttpServletResponse();
				response.setHeader("Cache-Control",
						"max-age=" + String.valueOf(seconds));
			}
		} catch (Throwable t) {
			// 缓存不成功关系不大
			LOG.info("获取注解的值出错：" + t);
		}
		return retObj;
	}

	/**
	 * 根据注解的值获取缓存时间
	 * 
	 * @param value
	 * @return
	 */
	private Long getCacheAge(CacheTime value) {
		Long seconds = null;
		switch (value) {
			case TENSEC:
				seconds = 10l;
				break;
			case THIRTYSEC:
				seconds = 30l;
				break;
			case ONEMIN:
				seconds = 60l;
				break;
			case FIVEMIN:
				seconds = 300l;
				break;
			case TENMIN:
				seconds = 600l;
				break;
			case ONEHOUR:
				seconds = 3600l;
				break;
		}
		return seconds;
	}
}
