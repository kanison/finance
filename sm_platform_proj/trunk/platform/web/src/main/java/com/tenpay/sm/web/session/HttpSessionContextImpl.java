/**
 * 
 */
package com.tenpay.sm.web.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.tenpay.sm.cache.Cache;
import com.tenpay.sm.cache.local.LocalLRUCacheImpl;
import com.tenpay.sm.web.cookie.CookieCache;

/**
 * @author li.hongtl
 * 
 */
public class HttpSessionContextImpl implements HttpSessionContext {
	public static HttpSessionContextImpl INSTANCE = null;

	private ServletContext servletContext;
	private Cache cache;

	public HttpSessionContextImpl() {
		this(null);
	}

	public HttpSessionContextImpl(Cache cache) {
		this.cache = cache;
		if (this.cache == null) {
			// LocalLRUCacheImpl localLRUCacheImpl = new LocalLRUCacheImpl();
			// localLRUCacheImpl.setMaxSize(10*10000);
			// localLRUCacheImpl.init();
			// this.cache = localLRUCacheImpl;

			CookieCache cookieCache = new CookieCache();
			this.cache = cookieCache;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSessionContext#getIds()
	 */
	public Enumeration getIds() {
		throw new java.lang.UnsupportedOperationException("Unsupported getIds "
				+ this.getClass());
	}

	// /* (non-Javadoc)
	// * @see javax.servlet.http.HttpSessionContext#getSession(java.lang.String)
	// */
	// public HttpSession getSession(String sessionId) {
	// HttpSessionModel model = (HttpSessionModel)cache.get(sessionId);
	// if(model==null) {
	// return null;
	// }
	// model.setLastAccessedTime(System.currentTimeMillis());
	// //TODO 不能返回多个对象
	// HttpSessionImpl httpSessionImpl = new HttpSessionImpl(sessionId,cache);
	// httpSessionImpl.setModel(model);
	// return httpSessionImpl;
	// }
	//	

	// public HttpSession getOrCreateSession(String sessionId) {
	// HttpSessionImpl httpSessionImpl = new HttpSessionImpl(sessionId,cache);
	// return httpSessionImpl;
	// }

	public HttpSession getSession(String sessionId) {
		HttpSessionImpl httpSessionImpl = new HttpSessionImpl(cache);
		return httpSessionImpl;
	}

	public void storeSession(HttpSession httpSession) {
		if (httpSession != null && httpSession instanceof HttpSessionImpl) {
			HttpSessionImpl httpSessionImpl = (HttpSessionImpl) httpSession;
			httpSessionImpl.storeToCache();
		}
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext
	 *            the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
