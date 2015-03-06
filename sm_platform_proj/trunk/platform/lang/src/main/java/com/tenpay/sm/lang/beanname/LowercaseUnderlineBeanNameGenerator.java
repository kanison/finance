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
 * 
 * @author li.hongtl
 * 
 */
public class LowercaseUnderlineBeanNameGenerator extends
		DefaultFixBeanNameGenerator {

	public LowercaseUnderlineBeanNameGenerator() throws IOException {
		super();
	}

	public String internalGenerateBeanName(BeanDefinition definition,
			BeanDefinitionRegistry registry) {
		String shortClassName = ClassUtils.getShortName(definition
				.getBeanClassName());
		shortClassName = insertUnderlineAndToLowerCase(shortClassName);
		return shortClassName;
	}

	private String insertUnderlineAndToLowerCase(String name) {
		char[] tmpCharArray = new char[name.length() * 2];
		int arrayOffset = 0;
		for (int i = 0; i < name.length(); i++) {
			char tmpChar = name.charAt(i);
			if (tmpChar >= 'A' && tmpChar <= 'Z') {
				if (i > 0) {
					tmpCharArray[arrayOffset] = '_';
					arrayOffset++;
				}
				tmpCharArray[arrayOffset] = (char) (tmpChar - ('A' - 'a'));
				arrayOffset++;
			} else {
				tmpCharArray[arrayOffset] = tmpChar;
				arrayOffset++;
			}
		}
		return String.valueOf(tmpCharArray).trim();
	}

}
