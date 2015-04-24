package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置http返回头的缓存控制时间
 * 
 * @author fortime
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetHeaderCacheControl {
	public enum CacheTime {
		NONSENSE, TENSEC, THIRTYSEC, ONEMIN, FIVEMIN, TENMIN, ONEHOUR
	}
	CacheTime value() default CacheTime.NONSENSE;
}
