package com.tenpay.sm.common.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * �����쳣�Ĺ����ࡣ
 *
 * @author Michael Zhou
 * @version $Id: ExceptionUtil.java 965 2004-04-28 03:20:05Z baobao $
 */
public class ExceptionUtil {
    /**
     * ȡ���쳣��stacktrace�ַ�����
     *
     * @param throwable �쳣
     *
     * @return stacktrace�ַ���
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter buffer = new StringWriter();
        PrintWriter  out = new PrintWriter(buffer);

        throwable.printStackTrace(out);
        out.flush();

        return buffer.toString();
    }
}
