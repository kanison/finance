package com.tenpay.sm.lang.lb;

import org.springframework.beans.factory.FactoryBean;


/**
 * 默认lb策略工厂
 * @author aixxia
 *
 */
public class DefaultLoadbalanceStrategyFactory implements FactoryBean{

	public Object getObject() throws Exception {
		return new DefaultLoadbalanceStrategy();
	}

	public Class<DefaultLoadbalanceStrategy> getObjectType() {
		return DefaultLoadbalanceStrategy.class;
	}

	public boolean isSingleton() {
		return false;
	}
}
