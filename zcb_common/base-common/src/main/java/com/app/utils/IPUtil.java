package com.app.utils;

import java.util.StringTokenizer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.config.ReloadableAppConfig;

public class IPUtil {
	private static final Log LOG = LogFactory.getLog(IPUtil.class);

	public static String getClientIP(ServletRequest request) {
		String ip;
		if (request instanceof HttpServletRequest) {
			ip = getIP((HttpServletRequest) request);
		} else {
			ip = request.getRemoteAddr();
		}
		return CommonUtil.trimString(ip);
	}

	/*
	 * 获取用户IP
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return String ip
	 */
	public static String getIP(HttpServletRequest request) {
		String remoteAddrFirst = CommonUtil
				.trimString(ReloadableAppConfig.appConfig
						.get("IPUtil.remoteAddrFirst"));
		if (remoteAddrFirst != null
				&& (remoteAddrFirst.equals("1") || remoteAddrFirst
						.equalsIgnoreCase("true"))) {
			// 为了符合财付通一般逻辑，先取直接IP
			String remoteAddr = CommonUtil.trimString(request.getRemoteAddr());
			if (remoteAddr != null && isIPValid(remoteAddr)) {
				LOG.info("ReturnIP:" + remoteAddr);
				return remoteAddr;
			}
		}
		String ipChain = request.getHeader("X-Forwarded-For");
		String ip = getValidIP(ipChain);
		if (ip == null) {
			if (ipChain != null && ipChain.length() > 0)
				LOG.info("There is no valid IP in X-Forwarded-For: " + ipChain);
			ipChain = request.getHeader("Proxy-Client-IP");
			ip = getValidIP(ipChain);
		}
		if (ip == null) {
			ipChain = request.getHeader("WL-Proxy-Client-IP");
			ip = getValidIP(ipChain);
		}
		if (ip == null) {
			ipChain = request.getHeader("HTTP_CLIENT_IP");
			ip = getValidIP(ipChain);
		}
		if (ip == null) {
			ipChain = request.getHeader("HTTP_X_FORWARDED_FOR");
			ip = getValidIP(ipChain);
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		LOG.info(String.format("ipChain: %s\tReturnIP: %s", ipChain, ip));
		return ip.trim();
	}

	public static String getValidIP(String ips) {
		if (ips == null || ips.length() == 0) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(ips, ",");
		while (st.hasMoreElements()) {
			String ip = st.nextToken().trim();
			if (isIPValid(ip)) {
				return ip;
			}
		}
		return null;
	}

	public static boolean isIPValid(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		if (st.countTokens() == 4) {
			try {
				// 判断私有IP
				int a = Integer.parseInt(st.nextToken());
				int b;
				switch (a) {
				case 10:
				case 127:
					return false;
				case 172:
					b = Integer.parseInt(st.nextToken());
					if (b >= 16 && b <= 31) {
						return false;
					}
					break;
				case 192:
					b = Integer.parseInt(st.nextToken());
					if (b == 168) {
						return false;
					}
					break;
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
}
