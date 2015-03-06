package com.tenpay.sm.web.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tenpay.sm.lang.health.Health;


/**
 * @author torryhong
 * 响应心跳检查
 */
@RequestMapping
public class HeartBeat {
	private static final Logger LOG = Logger.getLogger(HeartBeat.class);
	private Map<String,Health> healthItems = new LinkedHashMap<String,Health>();
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String,Object> handleHeartBeatRequest() {
		Map<String,Object> results = new LinkedHashMap<String,Object>();
		for(String name : healthItems.keySet()) {
			Health health = healthItems.get(name);
			try {
				Object result = health.check();
				results.put(name.replace("#", ".sharp."), result);
			} catch(Exception ex) {
				LOG.error("健康检查项出错,忽略"+name,ex);
			}
		}
		return results;
	}


	public Map<String, Health> getHealthItems() {
		return healthItems;
	}


	public void setHealthItems(Map<String, Health> healthItems) {
		this.healthItems = healthItems;
	}


}
