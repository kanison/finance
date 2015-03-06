/**
 * 
 */
package com.tenpay.sm.web.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import com.tenpay.sm.cache.Cache;
import com.tenpay.sm.client.soagov.RequestHeaderXFireSoaGovInterceptor;
import com.tenpay.sm.common.lang.diagnostic.Profiler;
import com.tenpay.sm.context.Context;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.lang.config.AppConfig;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.log.Loggers;
import com.tenpay.sm.lang.util.Base64;
import com.tenpay.sm.web.session.HttpServletRequestImpl;
import com.tenpay.sm.web.session.HttpSessionContextImpl;
import com.tenpay.sm.web.session.MultipartHttpServletRequestImpl;

/**
 * 一个过滤器，过滤url，初始化WebContext 注意！服务器内部分发和包含其它url也要经过这个Filter!
 * 原始的*.jsp不要经过这个filter
 * 
 * @author li.hongtl
 * 
 */
public class WebModuleContextFilter implements Filter {
	AppConfig appConfig = ReloadableAppConfig.appConfig;
	private static final long serialVersionUID = 3452288969652311124L;
	private static final Logger LOG = Logger
			.getLogger(WebModuleContextFilter.class);
	private static final String CHARSET_CONFIG_KEY = "CHARSET";
	private static final String SESSION_TYPE_CONFIG_KEY = "SESSION_TYPE";
	private String charset = "GBK";
	public static final String SESSION_TYPE_NONE = "NONE";
	public static final String SESSION_TYPE_WRAP = "WRAP";
	public static final String SESSION_TYPE_ORIGINAL = "ORIGINAL";
	public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

	private FilterConfig filterConfig;
	private MultipartResolver multipartResolver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		ServletContext sc = this.filterConfig.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sc);

		Cache cache = null;
		try {
			cache = (Cache) webApplicationContext.getBean("httpSessionCache",
					Cache.class);
		} catch (Exception ex) {
			LOG.warn("没有配置httpSessionCache，默认使用本地cache作session,"
					+ ex.getMessage());
		}
		HttpSessionContextImpl.INSTANCE = new HttpSessionContextImpl(cache);
		HttpSessionContextImpl.INSTANCE.setServletContext(sc);

		String configCharset = this.filterConfig
				.getInitParameter(CHARSET_CONFIG_KEY);
		if (configCharset != null && configCharset.length() > 0) {
			charset = configCharset;
		}
		if (webApplicationContext.containsBean(MULTIPART_RESOLVER_BEAN_NAME))
			multipartResolver = (MultipartResolver) webApplicationContext
					.getBean(MULTIPART_RESOLVER_BEAN_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		Context oldContext = null;
		WebModuleContext wc = new WebModuleContext();
		WebModuleContext parent = null;
		MockHttpServletResponse mockResponse = null;
		// 尝试探测慢连接
		long beginTime = System.currentTimeMillis();
		try {
			if (ContextUtil.hasContext()) {
				oldContext = ContextUtil.getContext();
				if (oldContext != null
						&& oldContext instanceof WebModuleContext) {
					// TODO 怎么检查是用一个请求？是否需要检查
					parent = (WebModuleContext) oldContext;
				} else {
					LOG.warn("parent context not WebModuleContext!"
							+ oldContext);
				}
			}
			ContextUtil.setContext(wc);

			wc.setRequest((HttpServletRequest) request);
			wc.setResponse((HttpServletResponse) response);
			wc.setServletContext(this.filterConfig.getServletContext());
			if (parent == null) {
				Profiler.reset();
				Profiler.start("Http请求开始处理开始"
						+ ((HttpServletRequest) request).getRequestURI());

				if (((HttpServletRequest) request).getServletPath().startsWith(
						"/services")) {
					mockResponse = new MockHttpServletResponse();
					wc.setResponse(mockResponse);
				}

				String globalId = ((HttpServletRequest) request)
						.getHeader(RequestHeaderXFireSoaGovInterceptor.KEY_SOAGOV_INVOKE_GLOBALID);
				RequestHeaderXFireSoaGovInterceptor.reset(globalId);

				String sessionType = this.filterConfig
						.getInitParameter(SESSION_TYPE_CONFIG_KEY);
				if (sessionType != null
						&& SESSION_TYPE_WRAP.equalsIgnoreCase(sessionType)) {
					Profiler.enter("获取wrapHttpSession");
					try {
						this.wrapHttpSession(wc);
					} finally {
						Profiler.release();
					}
				}
			} else {
				wc.setHttpSession(parent.getHttpSession());
			}

			if (multipartResolver != null
					&& multipartResolver
							.isMultipart((HttpServletRequest) request)) {
				// 特殊处理Multipart
				MultipartHttpServletRequest multipartHttpServletRequest = multipartResolver
						.resolveMultipart((HttpServletRequest) request);
				request = multipartHttpServletRequest;
				wc.setRequest(new MultipartHttpServletRequestImpl(
						multipartHttpServletRequest, wc.getHttpSession(),
						charset));
			} else {

				wc.setRequest(new HttpServletRequestImpl(
						(HttpServletRequest) request, wc.getHttpSession(),
						charset));
			}
			wc.init(parent);
			long midTime = System.currentTimeMillis();
			long duration = midTime - beginTime;
			if (duration > 30000)
				LOG.warn(request.getRemoteAddr() + "慢连接,耗时(ms):" + duration);

			Profiler.enter("处理webModule的http请求");
			try {
				filterChain.doFilter(wc.getRequest(),
						mockResponse != null ? mockResponse : response);
			} finally {
				Profiler.release();
			}
			duration = System.currentTimeMillis() - midTime;
			if (duration > 10000)
				LOG.warn(request.getRemoteAddr()
						+ wc.getRequest().getRequestURI() + "处理缓慢,耗时(ms):"
						+ duration);
		} catch (IOException ex) {
			long duration = System.currentTimeMillis() - beginTime;
			LOG.warn("WebModuleContextFilter IOException,耗时(ms):" + duration
					+ ex);
			throw ex;
		} catch (ServletException ex) {
			long duration = System.currentTimeMillis() - beginTime;
			LOG.warn("WebModuleContextFilter ServletException,耗时(ms):"
					+ duration + ex);
			throw ex;
		} finally {
			if (oldContext != null) {
				ContextUtil.setContext(oldContext);
				wc.destroy();
			} else if (oldContext == null) {
				// TODO only store Session if the WebModuleContext is Top
				// WebModuleContext
				Profiler.enter("存储HttpSession");
				try {
					HttpSessionContextImpl.INSTANCE.storeSession(wc
							.getHttpSession());
				} catch (Exception ex) {
					LOG.error("storeSession出错", ex);
				} finally {
					ContextUtil.setContext(oldContext);
					wc.destroy();
					Profiler.release();
				}

				Profiler.release();

				String localhostName = "UnknowHost";
				try {
					localhostName = java.net.InetAddress.getLocalHost()
							.getHostName();
				} catch (java.net.UnknownHostException ex) {
				}

				String profilerDump = null;
				if (Profiler.getDuration() >= 600) {
					profilerDump = new java.util.Formatter().format(
							"\n请求执行时间: %dms, IGID: %s, host:%s, URI: %s\n%s",
							Profiler.getDuration(),
							RequestHeaderXFireSoaGovInterceptor
									.getInvokeGlobalId(), localhostName,
							((HttpServletRequest) request).getRequestURI(),
							Profiler.dump()).toString();
					Loggers.PERF.warn(profilerDump);
				}

				/**
				 * xfire response header，mockresponse
				 */
				if (mockResponse != null) {
					if (((HttpServletRequest) request).getServletPath()
							.startsWith("/services")) {
						if (profilerDump == null) {
							profilerDump = new java.util.Formatter()
									.format(
											"\n请求执行时间: %dms, IGID: %s, host:%s, URI: %s\n%s",
											Profiler.getDuration(),
											RequestHeaderXFireSoaGovInterceptor
													.getInvokeGlobalId(),
											localhostName,
											((HttpServletRequest) request)
													.getRequestURI(),
											Profiler.dump()).toString();
						}
						((HttpServletResponse) response)
								.addHeader(
										RequestHeaderXFireSoaGovInterceptor.KEY_SOAGOV_INVOKE_RESPONSEHEADER,
										Base64.toBase64(profilerDump));
					}

					for (Object name : mockResponse.getHeaderNames()) {
						Object value = mockResponse.getHeader((String) name);
						((HttpServletResponse) response).addHeader(
								(String) name, value != null ? value.toString()
										: null);
					}
					((HttpServletResponse) response)
							.setContentType(mockResponse.getContentType());

					if (mockResponse.getCookies() != null) {
						for (Cookie cookie : mockResponse.getCookies()) {
							((HttpServletResponse) response).addCookie(cookie);
						}
					}

					((HttpServletResponse) response).getOutputStream().write(
							mockResponse.getContentAsByteArray());
					((HttpServletResponse) response).flushBuffer();
				}
			}
		}
	}

	private void wrapHttpSession(WebModuleContext wc) {

		wc.setHttpSession(HttpSessionContextImpl.INSTANCE.getSession(null));

	}

	public void setMultipartResolver(MultipartResolver multipartResolver) {
		this.multipartResolver = multipartResolver;
	}

	public MultipartResolver getMultipartResolver() {
		return multipartResolver;
	}

}
