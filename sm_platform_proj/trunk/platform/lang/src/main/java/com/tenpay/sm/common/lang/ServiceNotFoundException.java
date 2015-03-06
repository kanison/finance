package com.tenpay.sm.common.lang;

import com.tenpay.sm.common.lang.exception.ChainedThrowable;
import com.tenpay.sm.common.lang.exception.ChainedThrowableDelegate;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * ����<code>META-INF/services/</code>�е��ļ�δ�ҵ�����ļ�ʧ�ܵ��쳣��
 *
 * @author Michael Zhou
 * @version $Id: ServiceNotFoundException.java 1291 2005-03-04 03:23:30Z baobao $
 */
public class ServiceNotFoundException extends ClassNotFoundException
        implements ChainedThrowable {
    private static final long      serialVersionUID = 3258126964232566584L;
    private final ChainedThrowable delegate = new ChainedThrowableDelegate(this);
    private Throwable              cause;

    /**
     * ����һ���յ��쳣.
     */
    public ServiceNotFoundException() {
        super();
    }

    /**
     * ����һ���쳣, ָ���쳣����ϸ��Ϣ.
     *
     * @param message ��ϸ��Ϣ
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param cause �쳣������
     */
    public ServiceNotFoundException(Throwable cause) {
        super((cause == null) ? null
                              : cause.getMessage());
        this.cause = cause;
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param message ��ϸ��Ϣ
     * @param cause �쳣������
     */
    public ServiceNotFoundException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * ȡ����������쳣������.
     *
     * @return �쳣������.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * ��ӡ����ջ����׼����.
     */
    public void printStackTrace() {
        delegate.printStackTrace();
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param stream ����ֽ���.
     */
    public void printStackTrace(PrintStream stream) {
        delegate.printStackTrace(stream);
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param writer ����ַ���.
     */
    public void printStackTrace(PrintWriter writer) {
        delegate.printStackTrace(writer);
    }

    /**
     * ��ӡ�쳣�ĵ���ջ, �����������쳣����Ϣ.
     *
     * @param writer ��ӡ�������
     */
    public void printCurrentStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
    }
}
