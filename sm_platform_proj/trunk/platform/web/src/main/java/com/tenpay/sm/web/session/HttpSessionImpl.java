/**
 * 
 */
package com.tenpay.sm.web.session;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.tenpay.sm.cache.Cache;

/**
 * @author li.hongtl
 * 
 */
public class HttpSessionImpl implements HttpSession, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8806088746973509723L;
	private static final String SESSION_NAME = "HttpSessionImpl.session_name";

	public HttpSessionImpl(Cache cache) {
		this.cache = cache;
		model = (HttpSessionModel) cache.get(SESSION_NAME);
		if (model == null) {
			model = new HttpSessionModel();
			this.setNew(true);
		}
	}

	HttpSessionModel model;
	private boolean isNew = false;
	private Cache cache;
	boolean stored;

	public void storeToCache() {
		//只允许保存一次
		if (stored||model == null)
			return;
		model.setLastAccessedTime(System.currentTimeMillis());
		cache.put(SESSION_NAME, model);
		stored=true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return this.model.getAttributes().get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		return this.model.getAttributes().keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public long getCreationTime() {
		return this.model.getCreationTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		return this.model.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		return this.model.getLastAccessedTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		return this.model.getMaxInactiveInterval();
	}

	public void setMaxInactiveInterval(int interval) {
		this.model.setMaxInactiveInterval(interval);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		return HttpSessionContextImpl.INSTANCE.getServletContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		return HttpSessionContextImpl.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String name) {
		return this.getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		String[] array = new String[this.model.getAttributes().size()];
		return this.model.getAttributes().keySet().toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		this.model.invalidate();
		this.isNew=true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		return this.isNew;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String,
	 * java.lang.Object)
	 */
	public void putValue(String name, Object value) {
		this.setAttribute(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		this.model.getAttributes().remove(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String name) {
		this.removeAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		if (value == null) {
			this.removeAttribute(name);
			return;
		}
		if (value instanceof java.io.Serializable) {
			this.model.getAttributes().put(name, (java.io.Serializable) value);
		} else {
			throw new java.lang.UnsupportedOperationException(
					"Non Serializable Class of " + value.getClass()
							+ " In session.");
		}
	}

	/**
	 * @return the model
	 */
	public HttpSessionModel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(HttpSessionModel model) {
		this.model = model;
	}

	/**
	 * @param isNew
	 *            the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
