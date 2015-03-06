package com.tenpay.sm.lang.util;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: 异常的包装工具
 * 对非RuntimeException包装成RuntimeException，避免到处try/catch
 * 对RuntimeException不重复包装，对反射调用得到的InvocationTargetException，获取原始的Exception
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author 洪桃李
 * @version 1.0
 */
public class ExceptionUtil {
	/**
	 * 包装异常
	 * @param ex
	 * @return
	 */
    public static RuntimeException wrapException(Throwable ex) {
        if(ex instanceof RuntimeException) {
            return (RuntimeException)ex;
        }
        else if(ex instanceof InvocationTargetException){
            if(((InvocationTargetException)ex).getTargetException()!=null) {
                return wrapException((
                        (InvocationTargetException) ex).getTargetException());
            }else {
                return new RuntimeException(ex);
            }
        }
        else {
            return new RuntimeException(ex);
        }
    }
}
