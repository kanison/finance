package com.tenpay.sm.lang.util;

import java.lang.reflect.*;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: ֧�ֵ���bean��ʵ�еķ����ķ������ù���
 * ������Ե��� xx.xxx��������ʾbean��xx���Ե�xxx����
 * ֧��xx.xxx��xx.xx.xxx��xx.xx[1].xxx��xx.xx(s).xx�ĸ�ʽ
 * ʹ�� PropertyUtils.getNestedProperty �������
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author ������
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
     * �������ã�û�в���
     */
    public static Object invokeNested(Object target, String methodName) {
         return invokeNested(target,methodName,EMPTY_TYPE,EMPTY_VALUE);
    }

    /**
     * �������ã�ֻ��һ��String���͵Ĳ���
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
     * ��������
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
