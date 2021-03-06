package com.zhaocb.common.aop.aspect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import com.app.utils.CommonUtil;
import com.app.utils.IPUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.common.aop.aspect.accessstatecache.AccessStateCache;
import com.zhaocb.common.aop.exception.IpLimitException;

/**
 * IP访问频率限制切面
 * 
 * @author aixxia
 * 
 */
@Aspect
public class LimitIPAccessFrequency implements Ordered {

	private static final Log log = LogFactory
			.getLog(LimitIPAccessFrequency.class);
	private AccessStateCache accessStateCache;
	private ConcurrentMap<String, String> whiteListMap = null;
	private ConcurrentMap<String, String> blackListMap = null;

	public LimitIPAccessFrequency() {
		String cacheSizeStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("limit_access_ip_cachesize"));
		int cacheSize = 2000;
		if (cacheSizeStr != null) {
			cacheSize = Integer.valueOf(cacheSizeStr);
		} else {
			log.warn("not set limit_access_ip_cachesize,use default "
					+ cacheSize);
		}

		String auditIntervalStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("limit_access_ip_interval"));
		long auditInterval = 60000;
		if (auditIntervalStr != null) {
			auditInterval = Integer.valueOf(auditIntervalStr);
		} else {
			log.warn("not set limit_access_ip_interval,use default "
					+ auditInterval);
		}

		String maxAccessTimesStr = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("limit_access_ip_times"));
		long maxAccessTimes = 600;
		if (maxAccessTimesStr != null) {
			maxAccessTimes = Integer.valueOf(maxAccessTimesStr);
		} else {
			log.warn("not set limit_access_ip_times,use default "
					+ maxAccessTimes);
		}

		accessStateCache = new AccessStateCache(cacheSize, auditInterval,
				maxAccessTimes);

		String whiteList = CommonUtil.trimString(ReloadableAppConfig.appConfig
				.get("limit_access_ip_white_list"));
		if (whiteList != null) {
			whiteListMap = new ConcurrentHashMap<String, String>();
			whiteList = whiteList.replace(',', ';');
			String[] whiteIPs = whiteList.split(";");
			for (String whiteIp : whiteIPs) {
				whiteIp = CommonUtil.trimString(whiteIp);
				whiteListMap.put(whiteIp, whiteIp);
			}
		}
		String blackList = CommonUtil.trimString(ReloadableAppConfig.appConfig
				.get("limit_access_ip_black_list"));
		if (blackList != null) {
			blackListMap = new ConcurrentHashMap<String, String>();
			blackList = blackList.replace(',', ';');
			String[] blackIPs = blackList.split(";");
			for (String blackIp : blackIPs) {
				blackIp = CommonUtil.trimString(blackIp);
				blackListMap.put(blackIp, blackIp);
			}
		}
	}

	/***
	 * 　　 * 切点 　　
	 */
	@Pointcut("@annotation(com.zhaocb.common.aop.annotation.LimitIPAccessFrequency)")
	public void allAddMethod() {
	};

	/***
	 * 执行方法
	 * 
	 * @throws Throwable
	 * 
	 * @throws Throwable
	 */
	@Around("allAddMethod()")
	public Object ipLimit(ProceedingJoinPoint joinPoint) throws Throwable {
		String ip = CommonUtil.getClientIP();
		if (!checkIpLimit(ip)) {
			throw new IpLimitException("您的IP[" + ip + "]访问频率过高，请稍后再试!");
		} else {
			return joinPoint.proceed();
		}
	}

	public boolean checkIpLimit(String ip) {
		if (whiteListMap != null && whiteListMap.get(ip) != null)
			return true;
		if (blackListMap != null && blackListMap.get(ip) != null)
			return accessStateCache.recodeAccess(ip);
		if (IPUtil.isIPValid(ip))
			return accessStateCache.recodeAccess(ip);
		else
			return true;
	}

	public int getOrder() {
		return 0;
	}
}
