package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权
 * 主站登陆(弱类型，不检查登录方式，允许QQ登录，初级登录，基金账户登录等)
 * 
 * @author eniacli
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthTenpayWeak {

}
