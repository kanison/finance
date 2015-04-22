package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ��Ȩ
 * ʹ�ñ���session��½̬����֤��վ��¼��ʧ��ʱ��֤�̻�ϵͳ��¼����ʼ��֤����Tenpay ��  Mch Session
 * 
 * @author carrickma
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthLocalByTenpayOrMch {

}