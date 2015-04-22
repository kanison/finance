package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权
 * 财付通登录或微信登陆(不带证书)，
 * 由于微信登录态往不同公众账号不同的cookie名称的方向发展，这个注解的实现不兼容，所以不建议使用这个注解
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
