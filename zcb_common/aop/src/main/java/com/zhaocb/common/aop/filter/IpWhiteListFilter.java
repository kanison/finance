package com.zhaocb.common.aop.filter;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonUtil;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.common.aop.exception.IpLimitException;

public class IpWhiteListFilter implements Filter {

	private ConcurrentMap<String, Boolean> whiteIpSet = new ConcurrentHashMap<String, Boolean>();
	private volatile long lastConfigUpdateTime = 0;
	private volatile long configUpdateInterval = 60000 * 5;
	private String configKeyName = "ip_white_list";
	private Log LOG = LogFactory.getLog(IpWhiteListFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		updateConfig();
		if (!whiteIpSet.isEmpty()) {
			// 限制直连ip
			String ip = request.getRemoteAddr();
			if (!whiteIpSet.containsKey(ip)) {
				LOG.warn("IP不在白名单内:" + ip);
				if (response instanceof HttpServletResponse) {
					((HttpServletResponse) response).setStatus(404);
					response.setContentType("text/html");
					Writer writer = response.getWriter();
					writer.write(ip + ": \nYour ip is not in white list!");
					writer.close();
					return;
				} else
					throw new IpLimitException(ip
							+ ": \nYour ip is not in white list!");
			}
		}
		chain.doFilter(request, response);
		return;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String config = CommonUtil.trimString(filterConfig
				.getInitParameter("configKeyName"));
		if (config != null)
			configKeyName = config;
	}

	private void updateConfig() {
		long currentTime = System.currentTimeMillis();
		long diff = currentTime - lastConfigUpdateTime;
		if (diff > configUpdateInterval || diff < 0) {
			synchronized (this) {
				currentTime = System.currentTimeMillis();
				diff = currentTime - lastConfigUpdateTime;
				if (diff > configUpdateInterval || diff < 0) {
					try {
						whiteIpSet.clear();
						String ipWhiteList = CommonUtil
								.trimString(ReloadableAppConfig.appConfig
										.get(configKeyName));
						if (ipWhiteList != null) {
							StringTokenizer st = new StringTokenizer(
									ipWhiteList, ";");
							while (st.hasMoreTokens()) {
								whiteIpSet.put(CommonUtil.trimString(st
										.nextToken()), Boolean.TRUE);
							}
						}
					} catch (Throwable t) {
					}
					try {
						String ipWhiteListUpdateInterval = CommonUtil
								.trimString(ReloadableAppConfig.appConfig
										.get("ip_white_list_update_interval"));
						if (ipWhiteListUpdateInterval != null) {
							configUpdateInterval = Long
									.parseLong(ipWhiteListUpdateInterval);
						} else
							configUpdateInterval = 60000 * 5;
					} catch (Throwable t) {
						configUpdateInterval = 60000 * 5;
					}
					lastConfigUpdateTime = currentTime;
				}
			}
		}
	}
}
