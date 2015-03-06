package com.tenpay.sm.lang.lb;

import org.springframework.beans.factory.FactoryBean;


/**
 * Ĭ��lb���Թ���
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
