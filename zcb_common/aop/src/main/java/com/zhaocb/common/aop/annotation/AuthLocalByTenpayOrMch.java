package com.zhaocb.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 鉴权
 * 使用本地session登陆态，验证主站登录，失败时验证商户系统登录。初始验证来自Tenpay 或  Mch Session
 * 
 * @author carrickma
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthLocalByTenpayOrMch {

}
