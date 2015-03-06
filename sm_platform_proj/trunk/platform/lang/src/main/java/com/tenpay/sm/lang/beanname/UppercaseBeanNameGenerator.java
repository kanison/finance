/**
 * 
 */
package com.tenpay.sm.lang.beanname;

import java.io.IOException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;

/**
 * ×ÖÄ¸´óÐ´µÄBeanNameGenerato
 * @author li.hongtl
 *
 */
public class UppercaseBeanNameGenerator extends DefaultFixBeanNameGenerator {


	public UppercaseBeanNameGenerator() throws IOException {
		super();
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.lang.beanname.AbstractFixBeanNameGenerator#internalGenerateBeanName(org.springframework.beans.factory.config.BeanDefinition, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public String internalGenerateBeanName(BeanDefinition definition,
			BeanDefinitionRegistry registry) {
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return shortClassName.toUpperCase();
	}

}
