/**
 * 
 */
package com.tenpay.sm.web.session;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author torryhong
 *
 */
abstract public class SessionScope implements Map<String, Object> {

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		throw new java.lang.UnsupportedOperationException("SessionScope.clear");
	}


	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object arg0) {
		throw new java.lang.UnsupportedOperationException("SessionScope.containsValue");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new java.lang.UnsupportedOperationException("SessionScope.entrySet");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		throw new java.lang.UnsupportedOperationException("SessionScope.isEmpty");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet() {
		throw new java.lang.UnsupportedOperationException("SessionScope.keySet");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(String arg0, Object arg1) {
		throw new java.lang.UnsupportedOperationException("SessionScope.put");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String, ? extends Object> arg0) {
		throw new java.lang.UnsupportedOperationException("SessionScope.putAll");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object arg0) {
		throw new java.lang.UnsupportedOperationException("SessionScope.remove");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	public int size() {
		throw new java.lang.UnsupportedOperationException("SessionScope.size");
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values() {
		throw new java.lang.UnsupportedOperationException("SessionScope.values");
	}

}
