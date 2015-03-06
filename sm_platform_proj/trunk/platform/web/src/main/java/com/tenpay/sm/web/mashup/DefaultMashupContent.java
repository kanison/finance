/**
 * 
 */
package com.tenpay.sm.web.mashup;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author li.hongtl
 *
 */
public class DefaultMashupContent implements Map<String,Object> {
	private MashupContentGetFacade mashupServerFacadeImpl;
	

	/**
	 * @param mashupServerFacadeImpl the mashupServerFacadeImpl to set
	 */
	public void setMashupServerFacadeImpl(
			MashupContentGetFacade mashupServerFacadeImpl) {
		this.mashupServerFacadeImpl = mashupServerFacadeImpl;
	}


	public void clear() {
		mashupServerFacadeImpl.getDefaultContent().clear();
	}


	public boolean containsKey(Object key) {
		return mashupServerFacadeImpl.getDefaultContent().containsKey(key);
	}


	public boolean containsValue(Object value) {
		return mashupServerFacadeImpl.getDefaultContent().containsValue(value);
	}


	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return mashupServerFacadeImpl.getDefaultContent().entrySet();
	}


	public Object get(Object key) {
		return mashupServerFacadeImpl.getDefaultContent().get(key);
	}


	public boolean isEmpty() {
		return mashupServerFacadeImpl.getDefaultContent().isEmpty();
	}


	public Set<String> keySet() {
		return mashupServerFacadeImpl.getDefaultContent().keySet();
	}


	public Object put(String key, Object value) {
		return mashupServerFacadeImpl.getDefaultContent().put(key, value);
	}


	public void putAll(Map<? extends String, ? extends Object> t) {
		mashupServerFacadeImpl.getDefaultContent().putAll(t);
		
	}


	public Object remove(Object key) {
		return mashupServerFacadeImpl.getDefaultContent().remove(key);
	}


	public int size() {
		return mashupServerFacadeImpl.getDefaultContent().size();
	}


	public Collection<Object> values() {
		return mashupServerFacadeImpl.getDefaultContent().values();
	}

	
}
