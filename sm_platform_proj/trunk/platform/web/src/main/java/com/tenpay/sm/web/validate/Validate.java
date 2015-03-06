/**
 * 
 */
package com.tenpay.sm.web.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ָʾspring pojo webģ����Ҫ��֤��������ݵ�annotation
 * @author li.hongtl
 *
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
	String[] value() default {};
}
