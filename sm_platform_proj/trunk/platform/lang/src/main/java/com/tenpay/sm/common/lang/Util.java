package com.tenpay.sm.common.lang;

import com.tenpay.sm.common.lang.enumeration.EnumUtil;
import com.tenpay.sm.common.lang.i18n.LocaleUtil;
import com.tenpay.sm.common.lang.io.StreamUtil;

import java.util.Collections;
import java.util.Map;

/**
 * ���ɳ��õĹ����ࡣ
 *
 * @author Michael Zhou
 * @version $Id: Util.java 1196 2004-11-24 01:03:13Z baobao $
 */
public class Util {
    private static final ArrayUtil        ARRAY_UTIL         = new ArrayUtil();
    private static final ClassLoaderUtil  CLASS_LOADER_UTIL  = new ClassLoaderUtil();
    private static final ClassUtil        CLASS_UTIL         = new ClassUtil();
    private static final EnumUtil         ENUM_UTIL          = new EnumUtil();
    private static final ExceptionUtil    EXCEPTION_UTIL     = new ExceptionUtil();
    private static final FileUtil         FILE_UTIL          = new FileUtil();
    private static final LocaleUtil       LOCALE_UTIL        = new LocaleUtil();
    private static final MathUtil         MATH_UTIL          = new MathUtil();
    private static final MessageUtil      MESSAGE_UTIL       = new MessageUtil();
    private static final ObjectUtil       OBJECT_UTIL        = new ObjectUtil();
    private static final StreamUtil       STREAM_UTIL        = new StreamUtil();
    private static final StringEscapeUtil STRING_ESCAPE_UTIL = new StringEscapeUtil();
    private static final StringUtil       STRING_UTIL        = new StringUtil();
    private static final SystemUtil       SYSTEM_UTIL        = new SystemUtil();
    private static final Map              ALL_UTILS          = Collections.unmodifiableMap(ArrayUtil
            .toMap(new Object[][] {
                    { "arrayUtil", ARRAY_UTIL },
                    { "classLoaderUtil", CLASS_LOADER_UTIL },
                    { "classUtil", CLASS_UTIL },
                    { "enumUtil", ENUM_UTIL },
                    { "exceptionUtil", EXCEPTION_UTIL },
                    { "fileUtil", FILE_UTIL },
                    { "localeUtil", LOCALE_UTIL },
                    { "mathUtil", MATH_UTIL },
                    { "messageUtil", MESSAGE_UTIL },
                    { "objectUtil", OBJECT_UTIL },
                    { "streamUtil", STREAM_UTIL },
                    { "stringEscapeUtil", STRING_ESCAPE_UTIL },
                    { "stringUtil", STRING_UTIL },
                    { "systemUtil", SYSTEM_UTIL }
                }));

    /**
     * ȡ��<code>ArrayUtil</code>��
     *
     * @return <code>ArrayUtil</code>ʵ��
     */
    public static ArrayUtil getArrayUtil() {
        return ARRAY_UTIL;
    }

    /**
     * ȡ��<code>ClassLoaderUtil</code>��
     *
     * @return <code>ClassLoaderUtil</code>ʵ��
     */
    public static ClassLoaderUtil getClassLoaderUtil() {
        return CLASS_LOADER_UTIL;
    }

    /**
     * ȡ��<code>ClassUtil</code>��
     *
     * @return <code>ClassUtil</code>ʵ��
     */
    public static ClassUtil getClassUtil() {
        return CLASS_UTIL;
    }

    /**
     * ȡ��<code>EnumUtil</code>��
     *
     * @return <code>EnumUtil</code>ʵ��
     */
    public static EnumUtil getEnumUtil() {
        return ENUM_UTIL;
    }

    /**
     * ȡ��<code>ExceptionUtil</code>��
     *
     * @return <code>ExceptionUtil</code>ʵ��
     */
    public static ExceptionUtil getExceptionUtil() {
        return EXCEPTION_UTIL;
    }

    /**
     * ȡ��<code>FileUtil</code>��
     *
     * @return <code>FileUtil</code>ʵ��
     */
    public static FileUtil getFileUtil() {
        return FILE_UTIL;
    }

    /**
     * ȡ��<code>LocaleUtil</code>��
     *
     * @return <code>LocaleUtil</code>ʵ��
     */
    public static LocaleUtil getLocaleUtil() {
        return LOCALE_UTIL;
    }

    /**
     * ȡ��<code>MathUtil</code>��
     *
     * @return <code>MathUtil</code>ʵ��
     */
    public static MathUtil getMathUtil() {
        return MATH_UTIL;
    }

    /**
     * ȡ��<code>MessageUtil</code>��
     *
     * @return <code>MessageUtil</code>ʵ��
     */
    public static MessageUtil getMessageUtil() {
        return MESSAGE_UTIL;
    }

    /**
     * ȡ��<code>ObjectUtil</code>��
     *
     * @return <code>ObjectUtil</code>ʵ��
     */
    public static ObjectUtil getObjectUtil() {
        return OBJECT_UTIL;
    }

    /**
     * ȡ��<code>StreamUtil</code>��
     *
     * @return <code>StreamUtil</code>ʵ��
     */
    public static StreamUtil getStreamUtil() {
        return STREAM_UTIL;
    }

    /**
     * ȡ��<code>StringEscapeUtil</code>��
     *
     * @return <code>StringEscapeUtil</code>ʵ��
     */
    public static StringEscapeUtil getStringEscapeUtil() {
        return STRING_ESCAPE_UTIL;
    }

    /**
     * ȡ��<code>StringUtil</code>��
     *
     * @return <code>StringUtil</code>ʵ��
     */
    public static StringUtil getStringUtil() {
        return STRING_UTIL;
    }

    /**
     * ȡ��<code>SystemUtil</code>��
     *
     * @return <code>SystemUtil</code>ʵ��
     */
    public static SystemUtil getSystemUtil() {
        return SYSTEM_UTIL;
    }

    /**
     * ȡ�ð�������utils��map
     *
     * @return utils map
     */
    public static Map getUtils() {
        return ALL_UTILS;
    }
}
