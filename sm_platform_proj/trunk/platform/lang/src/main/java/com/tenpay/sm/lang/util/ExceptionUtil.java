package com.tenpay.sm.lang.util;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: �쳣�İ�װ����
 * �Է�RuntimeException��װ��RuntimeException�����⵽��try/catch
 * ��RuntimeException���ظ���װ���Է�����õõ���InvocationTargetException����ȡԭʼ��Exception
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author ������
 * @version 1.0
 */
public class ExceptionUtil {
	/**
	 * ��װ�쳣
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
