package com.tenpay.sm.common.lang.enumeration.internal;

/**
 * �������������ֽӿ�.
 *
 * @author Michael Zhou
 * @version $Id: NumberType.java 1196 2004-11-24 01:03:13Z baobao $
 */
public interface NumberType {
    /** ʮ�����ƵĻ�����16�� */
    int RADIX_HEX = 16;

    /** �˽��ƵĻ�����8�� */
    int RADIX_OCT = 8;

    /** �����ƵĻ�����2�� */
    int RADIX_BIN = 2;

    /**
     * ȡ������ֵ
     *
     * @return ����ֵ
     */
    int intValue();

    /**
     * ȡ�ó�����ֵ
     *
     * @return ������ֵ
     */
    long longValue();

    /**
     * ȡ�ø���ֵ
     *
     * @return ����ֵ
     */
    float floatValue();

    /**
     * ȡ��ȡ��doubleֵ
     *
     * @return doubleֵ
     */
    double doubleValue();

    /**
     * ȡ��byteֵ
     *
     * @return byteֵ
     */
    byte byteValue();

    /**
     * ȡ��shortֵ
     *
     * @return shortֵ
     */
    short shortValue();

    /**
     * ת����ʮ�����������ַ���.
     *
     * @return ʮ�����������ַ���
     */
    String toHexString();

    /**
     * ת���ɰ˽��������ַ���.
     *
     * @return �˽��������ַ���
     */
    String toOctalString();

    /**
     * ת���ɶ����������ַ���.
     *
     * @return �����������ַ���
     */
    String toBinaryString();
}
