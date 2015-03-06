/**
 * 
 */
package com.tenpay.sm.web.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author li.hongtl
 * 
 */
@SuppressWarnings("unchecked")
public class MultipartHttpServletRequestImpl implements
		MultipartHttpServletRequest, java.io.Serializable {
	public static final String INPUT_CHARSET_PARAM_KEY = "input_charset";
	private String defaultCharset = "GBK";

	private static final Logger LOG = Logger
			.getLogger(MultipartHttpServletRequestImpl.class);
	private static final long serialVersionUID = 3147479045673521394L;
	private MultipartHttpServletRequest request;
	private HttpSession httpSession;
	private Map<String, String[]> params;
	private Cookie[] cookies = null;

	public MultipartHttpServletRequestImpl(MultipartHttpServletRequest request,
			HttpSession httpSession, String defaultCharset) {
		if (defaultCharset != null) {
			this.defaultCharset = defaultCharset;
		}
		this.request = request;
		this.httpSession = httpSession;
		this.params = this.request.getParameterMap();
		// 参数太多，不转换
		if (this.params.size() > 10000)
			return;
		// 取输入的字符集
		String input_charset = ((MultipartHttpServletRequest) request)
				.getParameter(INPUT_CHARSET_PARAM_KEY);
		String charset = null;
		if (input_charset != null && !"".equals(input_charset)) {
			charset = input_charset;
		} else {
			charset = this.defaultCharset;
		}

		String originalRequestCharset = request.getCharacterEncoding();
		if (originalRequestCharset == null) {
			originalRequestCharset = "ISO-8859-1";
		} else if (originalRequestCharset.equalsIgnoreCase("gb2312")) {
			originalRequestCharset = "GBK";
		}
		// 取query参数
		Set<String> queryKeys = new HashSet<String>();
		String queryString = this.request.getQueryString();
		// 有queryString, GET的参数需要做编程式的转换
		if (queryString != null) {
			queryString = queryString.trim();
			if (queryString.length() > 0) {
				StringTokenizer st = new StringTokenizer(queryString, "&");
				// 取得一个个get参数的key value
				while (st.hasMoreElements()) {
					String keyvalue = st.nextToken();
					int equalIndex = keyvalue.indexOf('=');
					if (equalIndex > 0) {
						String key = keyvalue.substring(0, equalIndex);
						queryKeys.add(key);
					}
				}
			}
		}
		// 参数需要做编程式的转换
		if (this.params != null) {
			boolean postParamsNeedTrans = !originalRequestCharset
					.equalsIgnoreCase(charset);
			try {
				Iterator<String> keyIt = this.params.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					String[] values = this.params.get(key);
					if (values != null && values.length > 0) {
						if (queryKeys.contains(key)) {
							// 处理query参数
							for (int i = 0; i < values.length; i++) {
								if (values[i] != null) {
									values[i] = new String(values[i]
											.getBytes("ISO-8859-1"), charset);
								}
							}
						} else if (postParamsNeedTrans) {
							// 处理post参数
							for (int i = 0; i < values.length; i++) {
								String tmpString = values[i];
								if (tmpString != null
										&& tmpString.length() <= 1073741824) {
									values[i] = new String(tmpString
											.getBytes(originalRequestCharset),
											charset);
								}
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				LOG.error("字符集转换出错，不应该发生", e);
			}
		}
		try {
			request.setCharacterEncoding(charset);
		} catch (UnsupportedEncodingException e1) {
			LOG.error("字符集转换出错，不应该发生", e1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		return this.request.getAuthType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		return this.request.getContextPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		if (cookies == null)
			return this.request.getCookies();
		else
			return cookies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String name) {
		return this.request.getDateHeader(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String name) {
		return this.request.getHeader(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() {
		return this.request.getHeaderNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String name) {
		return this.request.getHeaders(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String name) {
		return this.request.getIntHeader(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return this.request.getMethod();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		return this.request.getPathInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		return this.request.getPathTranslated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		return this.request.getQueryString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		return this.request.getRemoteUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		return this.request.getRequestURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		return this.request.getRequestURL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		// return this.request.getRequestedSessionId();
		// TODO 这里返回什么，再考虑一下
		return this.httpSession.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		return this.request.getServletPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		// return this.request.getSession();
		return getSession(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean create) {
		if (this.httpSession != null) {
			// 判断现有session是否有效
			long lastAccessedTime = this.httpSession.getLastAccessedTime();
			long diff = System.currentTimeMillis() - lastAccessedTime;
			long maxInactiveInterval = ((long) this.httpSession
					.getMaxInactiveInterval()) * 1000;
			if (LOG.isDebugEnabled()) {
				LOG
						.debug(String
								.format(
										"lastAccessedTime: %d, maxInactiveInterval: %d, diff: %d",
										lastAccessedTime, maxInactiveInterval,
										diff));
			}
			if (maxInactiveInterval > 0
					&& (diff > maxInactiveInterval || diff < 0)) {
				this.httpSession.invalidate();
				if (create)
					return this.httpSession;
				else
					return null;
			} else
				return this.httpSession;
		} else {
			return this.request.getSession(create);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		return this.request.getUserPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		return this.request.isRequestedSessionIdFromCookie();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		return this.request.isRequestedSessionIdFromURL();
	}

	/**
	 * @deprecated As of Version 2.1 of the Java Servlet API, use
	 *             {@link #isRequestedSessionIdFromURL} instead.
	 */
	public boolean isRequestedSessionIdFromUrl() {
		return this.request.isRequestedSessionIdFromUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		return this.request.isRequestedSessionIdValid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String role) {
		return this.request.isUserInRole(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return this.request.getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		return this.request.getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return this.request.getCharacterEncoding();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		return this.request.getContentLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		return this.request.getContentType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		return this.request.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		return this.request.getLocalAddr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		return this.request.getLocalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		return this.request.getLocalPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		return this.request.getLocale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		return this.request.getLocales();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String name) {
		String[] values = this.params.get(name);
		if (values != null && values.length > 0) {
			return values[0];
		} else {
			return null;
		}
		// return this.request.getParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
		return this.params;
		// return this.request.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		return this.request.getParameterNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String name) {
		return this.params.get(name);
		// return this.request.getParameterValues(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		return this.request.getProtocol();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		return this.request.getReader();
	}

	/**
	 * @deprecated As of Version 2.1 of the Java Servlet API, use
	 *             {@link ServletContext#getRealPath} instead.
	 */
	public String getRealPath(String path) {
		return this.request.getRealPath(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		return this.request.getRemoteAddr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		return this.request.getRemoteHost();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
		return this.request.getRemotePort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String path) {
		return this.request.getRequestDispatcher(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		return this.request.getScheme();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		return this.request.getServerName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		return this.request.getServerPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		return this.request.isSecure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		this.request.removeAttribute(name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object o) {
		this.request.setAttribute(name, o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		this.request.setCharacterEncoding(env);
	}

	public MultipartFile getFile(String name) {
		// TODO Auto-generated method stub
		return this.request.getFile(name);
	}

	public Map getFileMap() {
		// TODO Auto-generated method stub
		return this.request.getFileMap();
	}

	public Iterator getFileNames() {
		// TODO Auto-generated method stub
		return this.request.getFileNames();
	}

	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}

}
