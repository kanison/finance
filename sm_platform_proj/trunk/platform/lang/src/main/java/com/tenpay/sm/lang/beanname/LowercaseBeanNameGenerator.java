/**
 * 
 */
package com.tenpay.sm.lang.beanname;

import java.io.IOException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;

/**
 * ×ÖÄ¸Ð¡Ð´µÄBeanNameGenerato
 * @author li.hongtl
 *
 */
public class LowercaseBeanNameGenerator extends DefaultFixBeanNameGenerator {

	public LowercaseBeanNameGenerator() throws IOException {
		super();
	}

	public String internalGenerateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return shortClassName.toLowerCase();
	}


}
