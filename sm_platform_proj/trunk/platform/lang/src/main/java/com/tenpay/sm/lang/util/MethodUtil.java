package com.tenpay.sm.lang.util;

import java.lang.reflect.*;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: 支持调用bean的实行的方法的方法调用工具
 * 例如可以调用 xx.xxx方法，表示bean的xx属性的xxx方法
 * 支持xx.xxx，xx.xx.xxx，xx.xx[1].xxx，xx.xx(s).xx的格式
 * 使用 PropertyUtils.getNestedProperty 获得属性
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author 洪桃李
 * @version 2.0
 */
public class MethodUtil {
    private static Class[] EMPTY_TYPE = new Class[0];
    private static Class[] STRING_TYPE = new Class[] {String.class};
    private static Object[] EMPTY_VALUE = new Object[0];
//    public static Object invoke(Object target, String methodName) {
//        try {
//            Method method = target.getClass().getMethod(methodName, EMPTY_TYPE);
//            return method.invoke(target, EMPTY_VALUE);
//        } catch (Throwable ex) {
//            throw ExceptionUtil.wrapException(ex);
//        }
//    }
//
//    public static Object invoke(Object target, String methodName,
//                                String parameter) {
//        try {
//            Method method = target.getClass().getMethod(methodName,
//                    STRING_TYPE);
//            return method.invoke(target, new Object[] {parameter});
//        } catch (Throwable ex) {
//            throw ExceptionUtil.wrapException(ex);
//        }
//    }
//
//    public static Object invoke(Object target, String methodName,
//                                Class[] paramClasses, Object[] parameters) {
//        try {
//            Method method = target.getClass().getMethod(methodName, paramClasses);
//            return method.invoke(target, parameters);
//        } catch (Throwable ex) {
//            throw ExceptionUtil.wrapException(ex);
//        }
//    }

    /**
     * 方法调用，没有参数
     */
    public static Object invokeNested(Object target, String methodName) {
         return invokeNested(target,methodName,EMPTY_TYPE,EMPTY_VALUE);
    }

    /**
     * 方法调用，只有一个String类型的参数
     * @param target
     * @param methodName
     * @param parameter
     * @return
     */
    public static Object invokeNested(Object target, String methodName,
                                String parameter) {
       return invokeNested(target,methodName,STRING_TYPE,new Object[]{parameter});
    }

    /**
     * 方法调用
     * @param target
     * @param methodName
     * @param paramClasses
     * @param parameters
     * @return
     */
    public static Object invokeNested(Object target, String methodName,
                                Class[] paramClasses, Object[] parameters) {
        try {
            Method method = null;
            int lastIndex = methodName.lastIndexOf('.');
            if (lastIndex < 0) {
                method = target.getClass().getMethod(methodName, paramClasses);
            } else {
                target = PropertyUtils.getNestedProperty(target,
                        methodName.substring(0, lastIndex));
                method = target.getClass().getMethod(
                        methodName.substring(lastIndex+1), paramClasses);
            }
            return method.invoke(target, parameters);
        } catch (Throwable ex) {
            throw ExceptionUtil.wrapException(ex);
        }
    }
}
