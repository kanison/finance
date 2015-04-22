package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权 API签名(不带证书,对返回结果签名,返回结果所有参数参与签名，暂时没考虑返回数据中有List的情况)
 * 
 * @author kansonzhang
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthApiWithSignResult {

}
