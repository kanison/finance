package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���ں����������溯����������Ͻ��������������ݱ仯���ٵĲ�ѯ�ӿ�
 * 
 * @author eniacli
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheMethod {

}
