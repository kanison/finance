/**
 * 
 */
package com.tenpay.sm.web.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.web.servlet.view.AbstractTemplateView;

import com.tenpay.sm.web.pull.PullServiceHandlerInterceptor;

/**
 * 只是生成数据的view，例如生成固定格式的xml,json等
 * 默认只要当前model数据，不要request,session的数据
 * @author li.hongtl
 *
 */
abstract public class AbstractToDataView extends AbstractTemplateView {
	private PullServiceHandlerInterceptor pullServiceHandlerInterceptor;
	public AbstractToDataView() {
		this.setExposeRequestAttributes(false);
		this.setAllowRequestOverride(false);
		this.setAllowSessionOverride(false);
		this.setExposeSessionAttributes(false);
		this.setExposeSpringMacroHelpers(false);
	}
	
	@Override
	protected boolean isUrlRequired() {
		return false;
	}
	protected Map<Object, Object> toUserDataMap(Map<Object, Object> modelMap) {
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		Iterator<Map.Entry<Object, Object>> iter = modelMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Object, Object> entry = iter.next();
			if(entry.getKey()!=null) {
				//if(!entry.getKey().toString().startsWith("org.springframework.validation.BindingResult")) {
				if(entry.getKey().toString().startsWith("org.springframework")) {
					continue;
				}
				if(pullServiceHandlerInterceptor.getGlobalTool().containsKey(entry.getKey())) {
					continue;
				}
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	public void setPullServiceHandlerInterceptor(
			PullServiceHandlerInterceptor pullServiceHandlerInterceptor) {
		this.pullServiceHandlerInterceptor = pullServiceHandlerInterceptor;
	}

	public PullServiceHandlerInterceptor getPullServiceHandlerInterceptor() {
		return pullServiceHandlerInterceptor;
	}
	
	
}