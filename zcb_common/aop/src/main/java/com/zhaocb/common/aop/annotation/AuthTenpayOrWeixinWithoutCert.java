package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��Ȩ
 * �Ƹ�ͨ��¼��΢�ŵ�½(����֤��)��
 * ����΢�ŵ�¼̬����ͬ�����˺Ų�ͬ��cookie���Ƶķ���չ�����ע���ʵ�ֲ����ݣ����Բ�����ʹ�����ע��
 * 
 * @author wenlonwang
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface AuthTenpayOrWeixinWithoutCert {

}
