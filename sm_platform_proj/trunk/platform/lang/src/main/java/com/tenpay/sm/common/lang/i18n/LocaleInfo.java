package com.tenpay.sm.common.lang.i18n;

import com.tenpay.sm.common.lang.ObjectUtil;
import com.tenpay.sm.common.lang.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import java.util.Locale;

/**
 * ����һ��locale��Ϣ��
 *
 * @author Michael Zhou
 * @version $Id: LocaleInfo.java 1291 2005-03-04 03:23:30Z baobao $
 */
public final class LocaleInfo
        implements Cloneable, Serializable {
    private static final long       serialVersionUID = 3257847675461251635L;
    private static final CharsetMap CHARSET_MAP = new CharsetMap();
    private Locale                  locale;
    private String                  charset;

    /**
     * ����ϵͳlocale��Ϣ��
     */
    LocaleInfo() {
        this.locale  = Locale.getDefault();
        this.charset = LocaleUtil.getCanonicalCharset(new OutputStreamWriter(new ByteArrayOutputStream())
                                                      .getEncoding(), "ISO-8859-1");
    }

    /**
     * ����locale��Ϣ��
     *
     * @param locale ������Ϣ
     */
    public LocaleInfo(Locale locale) {
        this(locale, null);
    }

    /**
     * ����locale��Ϣ��
     *
     * @param locale ������Ϣ
     * @param charset �����ַ���
     */
    public LocaleInfo(Locale locale, String charset) {
        this(locale, charset, LocaleUtil.getDefault());
    }

    /**
     * ����locale��Ϣ��
     *
     * @param locale ������Ϣ
     * @param charset �����ַ���
     * @param defaultLocaleInfo Ĭ�ϵ�locale��Ϣ
     */
    LocaleInfo(Locale locale, String charset, LocaleInfo defaultLocaleInfo) {
        if (locale == null) {
            locale = defaultLocaleInfo.getLocale();

            if (StringUtil.isEmpty(charset)) {
                charset = defaultLocaleInfo.getCharset();
            }
        }

        if (StringUtil.isEmpty(charset)) {
            charset = CHARSET_MAP.getCharSet(locale);
        }

        this.locale  = locale;
        this.charset = LocaleUtil.getCanonicalCharset(charset, defaultLocaleInfo.getCharset());
    }

    /**
     * ȡ������
     *
     * @return ����
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * ȡ�ñ����ַ�����
     *
     * @return �����ַ���
     */
    public String getCharset() {
        return charset;
    }

    /**
     * �Ƚ϶���
     *
     * @param o ���ȽϵĶ���
     *
     * @return ��������Ч���򷵻�<code>true</code>
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof LocaleInfo)) {
            return false;
        }

        LocaleInfo otherLocaleInfo = (LocaleInfo) o;

        return ObjectUtil.equals(locale, otherLocaleInfo.locale)
               && ObjectUtil.equals(charset, otherLocaleInfo.charset);
    }

    /**
     * ȡ��locale��Ϣ��hashֵ��
     *
     * @return hashֵ
     */
    public int hashCode() {
        return ObjectUtil.hashCode(locale) ^ ObjectUtil.hashCode(charset);
    }

    /**
     * ���ƶ���
     *
     * @return ����Ʒ
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * ȡ���ַ�����ʾ��
     *
     * @return �ַ�����ʾ
     */
    public String toString() {
        return locale + ":" + charset;
    }
}
