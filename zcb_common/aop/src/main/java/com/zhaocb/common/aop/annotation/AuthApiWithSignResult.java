package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��Ȩ APIǩ��(����֤��,�Է��ؽ��ǩ��,���ؽ�����в�������ǩ������ʱû���Ƿ�����������List�����)
 * 
 * @author kansonzhang
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthApiWithSignResult {

}