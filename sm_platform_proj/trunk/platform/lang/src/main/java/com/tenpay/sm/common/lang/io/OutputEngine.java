package com.tenpay.sm.common.lang.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * �����ؽ�����Դд�뵽ָ�������������, ��������ֲ��IBM developer works��������, �μ�package�ĵ�.
 *
 * @author Michael Zhou
 * @version $Id: OutputEngine.java 509 2004-02-16 05:42:07Z baobao $
 */
public interface OutputEngine {
    /** Ĭ�ϵ����������, ֱ�ӷ���ָ���������. */
    OutputStreamFactory DEFAULT_OUTPUT_STREAM_FACTORY = new OutputStreamFactory() {
            public OutputStream getOutputStream(OutputStream out) {
                return out;
            }
        };

    /**
     * ��ʼ���������, ͨ��<code>OutputEngine</code>��ʵ�ֻὫһ��<code>FilterOutputStream</code>���ӵ�ָ�����������.
     *
     * @param out �����ָ���������
     *
     * @throws IOException ��������쳣
     */
    void open(OutputStream out) throws IOException;

    /**
     * ִ��һ���������.  �˲�����<code>OutputEngine</code>���������лᱻִ�ж��, ÿ�ζ�����������д�뵽��ʼ��ʱָ���������.
     *
     * @throws IOException ��������쳣
     */
    void execute() throws IOException;

    /**
     * ɨβ����.  �����е����������Ժ�, �˷���������.
     *
     * @throws IOException ��������쳣
     */
    void close() throws IOException;

    /**
     * ����������Ĺ���.
     */
    interface OutputStreamFactory {
        /**
         * ���������, ͨ������һ��<code>FilterOutputStream</code>���ӵ�ָ�����������.
         *
         * @param out �����ָ���������
         *
         * @return �����
         *
         * @throws IOException ��������쳣
         */
        OutputStream getOutputStream(OutputStream out) throws IOException;
    }
}
