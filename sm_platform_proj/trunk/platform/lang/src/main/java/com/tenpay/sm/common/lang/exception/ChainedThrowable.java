package com.tenpay.sm.common.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * ʵ�ִ˽ӿڵ��쳣, ������һ���쳣�����.
 *
 * @author Michael Zhou
 * @version $Id: ChainedThrowable.java 509 2004-02-16 05:42:07Z baobao $
 */
public interface ChainedThrowable extends Serializable {
    /**
     * ȡ���쳣������.
     *
     * @return �쳣������.
     */
    Throwable getCause();

    /**
     * ��ӡ����ջ����׼����.
     */
    void printStackTrace();

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param stream ����ֽ���.
     */
    void printStackTrace(PrintStream stream);

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param writer ����ַ���.
     */
    void printStackTrace(PrintWriter writer);

    /**
     * ��ӡ�쳣�ĵ���ջ, �����������쳣����Ϣ.
     *
     * @param writer ��ӡ�������
     */
    void printCurrentStackTrace(PrintWriter writer);
}
