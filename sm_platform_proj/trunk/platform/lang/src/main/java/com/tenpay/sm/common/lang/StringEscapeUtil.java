package com.tenpay.sm.common.lang;

import com.tenpay.sm.common.lang.i18n.LocaleUtil;
import com.tenpay.sm.common.lang.internal.Entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.util.BitSet;

/**
 * �ַ���ת�幤���࣬�ܽ��ַ���ת������Ӧ Java��Java Script��HTML��XML����SQL������ʽ��
 *
 * @author Michael Zhou
 * @version $Id: StringEscapeUtil.java 2474 2006-09-20 07:11:47Z baobao $
 */
public class StringEscapeUtil {
    /* ============================================================================ */
    /*  Java��JavaScript��                                                          */
    /* ============================================================================ */

    /**
     * ��Java�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn't say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     *
     * @return ת�����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }

    /**
     * ��Java�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn't say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @return ת�����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeJava(String str, boolean strict) {
        return escapeJavaStyleString(str, false, strict);
    }

    /**
     * ��Java�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn't say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeJava(String str, Writer out) throws IOException {
        escapeJavaStyleString(str, false, out, false);
    }

    /**
     * ��Java�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn't say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeJava(String str, Writer out, boolean strict)
            throws IOException {
        escapeJavaStyleString(str, false, out, strict);
    }

    /**
     * ��JavaScript�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���š������źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn\'t say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     *
     * @return ת�����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true, false);
    }

    /**
     * ��JavaScript�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���š������źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn\'t say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @return ת�����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeJavaScript(String str, boolean strict) {
        return escapeJavaStyleString(str, true, strict);
    }

    /**
     * ��JavaScript�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���š������źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn\'t say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeJavaScript(String str, Writer out)
            throws IOException {
        escapeJavaStyleString(str, true, out, false);
    }

    /**
     * ��JavaScript�Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ��˫���š������źͿ����ַ�ת����<code>'\\'</code>��ͷ����ʽ������tab�Ʊ������ת����<code>\t</code>��
     * </p>
     * 
     * <p>
     * Java��JavaScript�ַ�����Ψһ����ǣ�JavaScript����Ե����Ž���ת�壬��Java����Ҫ��
     * </p>
     * 
     * <p>
     * ���磺�ַ�����<code>He didn't say, "Stop!"</code>��ת����<code>He didn\'t say, \"Stop!\"</code>
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeJavaScript(String str, Writer out, boolean strict)
            throws IOException {
        escapeJavaStyleString(str, true, out, strict);
    }

    /**
     * ��Java��JavaScript�Ĺ�����ַ�������ת�塣
     *
     * @param str Ҫת����ַ���
     * @param escapeSingleQuotes �Ƿ�Ե����Ž���ת��
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @return ת�����ַ���
     */
    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes,
        boolean strict) {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length() * 2);

            if (escapeJavaStyleString(str, escapeSingleQuotes, out, strict)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣 
        }
    }

    /**
     * ��Java��JavaScript�Ĺ�����ַ�������ת�塣
     *
     * @param str Ҫת����ַ���
     * @param escapeSingleQuote �Ƿ�Ե����Ž���ת��
     * @param out �����
     * @param strict �Ƿ����ϸ�ķ�ʽ�����ַ���
     *
     * @return ����ַ���û�б仯���򷵻�<code>false</code>
     */
    private static boolean escapeJavaStyleString(String str, boolean escapeSingleQuote, Writer out,
        boolean strict) throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);

            if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;

                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;

                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;

                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;

                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;

                    default:

                        if (ch > 0xf) {
                            out.write("\\u00" + Integer.toHexString(ch).toUpperCase());
                        } else {
                            out.write("\\u000" + Integer.toHexString(ch).toUpperCase());
                        }

                        break;
                }

                // ���øı��־
                needToChange = true;
            } else if (strict && (ch > 0xff)) {
                if (ch > 0xfff) {
                    out.write("\\u" + Integer.toHexString(ch).toUpperCase());
                } else {
                    out.write("\\u0" + Integer.toHexString(ch).toUpperCase());
                }

                // ���øı��־
                needToChange = true;
            } else {
                switch (ch) {
                    case '\'':

                        if (escapeSingleQuote) {
                            out.write('\\');

                            // ���øı��־
                            needToChange = true;
                        }

                        out.write('\'');

                        break;

                    case '"':
                        out.write('\\');
                        out.write('"');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case '\\':
                        out.write('\\');
                        out.write('\\');

                        // ���øı��־
                        needToChange = true;
                        break;

                    default:
                        out.write(ch);
                        break;
                }
            }
        }

        return needToChange;
    }

    /**
     * ��Java�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ������ת���ַ����ַ���
     *
     * @return �ָ���δת����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String unescapeJava(String str) {
        return unescapeJavaStyleString(str);
    }

    /**
     * ��Java�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void unescapeJava(String str, Writer out)
            throws IOException {
        unescapeJavaStyleString(str, out);
    }

    /**
     * ��JavaScript�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     *
     * @return �ָ���δת����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String unescapeJavaScript(String str) {
        return unescapeJavaStyleString(str);
    }

    /**
     * ��Java�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void unescapeJavaScript(String str, Writer out)
            throws IOException {
        unescapeJavaStyleString(str, out);
    }

    /**
     * ��Java�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     *
     * @return ������ת���ַ����ַ���
     */
    private static String unescapeJavaStyleString(String str) {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length());

            if (unescapeJavaStyleString(str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣
        }
    }

    /**
     * ��Java�Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * <code>'\\'</code>��ͷ����ʽת������Ӧ���ַ�������<code>\t</code>����ת����tab�Ʊ��
     * </p>
     * 
     * <p>
     * ���ת������ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     * @param out �����
     *
     * @return ����ַ���û�б仯���򷵻�<code>false</code>
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    private static boolean unescapeJavaStyleString(String str, Writer out)
            throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        int          length    = str.length();
        StringBuffer unicode   = new StringBuffer(4);
        boolean      hadSlash  = false;
        boolean      inUnicode = false;

        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);

            if (inUnicode) {
                unicode.append(ch);

                if (unicode.length() == 4) {
                    String unicodeStr = unicode.toString();

                    try {
                        int value = Integer.parseInt(unicodeStr, 16);

                        out.write((char) value);
                        unicode.setLength(0);
                        inUnicode     = false;
                        hadSlash      = false;

                        // ���øı��־
                        needToChange = true;
                    } catch (NumberFormatException e) {
                        out.write("\\u" + unicodeStr);
                    }
                }

                continue;
            }

            if (hadSlash) {
                hadSlash = false;

                switch (ch) {
                    case '\\':
                        out.write('\\');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case '\'':
                        out.write('\'');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case '\"':
                        out.write('"');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 'r':
                        out.write('\r');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 'f':
                        out.write('\f');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 't':
                        out.write('\t');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 'n':
                        out.write('\n');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 'b':
                        out.write('\b');

                        // ���øı��־
                        needToChange = true;
                        break;

                    case 'u': {
                        inUnicode = true;
                        break;
                    }

                    default:
                        out.write(ch);
                        break;
                }

                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }

            out.write(ch);
        }

        if (hadSlash) {
            out.write('\\');
        }

        return needToChange;
    }

    /* ============================================================================ */
    /*  HTML��XML��                                                                 */
    /* ============================================================================ */

    /**
     * ����HTML�Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     * 
     * <p>
     * ���磺<code>"bread" & "butter"</code>����ת����<tt>&amp;quot;bread&amp;quot; &amp;amp;
     * &amp;quot;butter&amp;quot;</tt>.
     * </p>
     * 
     * <p>
     * ֧������HTML 4.0 entities��
     * </p>
     *
     * @param str Ҫת����ַ���
     *
     * @return ��ʵ�����ת����ַ��������ԭ�ִ�Ϊ<code>null</code>���򷵻�<code>null</code>
     *
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO
     *      Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO
     *      Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity
     *      references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character
     *      References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code
     *      positions</a>
     */
    public static String escapeHtml(String str) {
        return escapeEntities(Entities.HTML40, str);
    }

    /**
     * ����HTML�Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     * 
     * <p>
     * ���磺<code>"bread" & "butter"</code>����ת����<tt>&amp;quot;bread&amp;quot; &amp;amp;
     * &amp;quot;butter&amp;quot;</tt>.
     * </p>
     * 
     * <p>
     * ֧������HTML 4.0 entities��
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     *
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO
     *      Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO
     *      Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity
     *      references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character
     *      References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code
     *      positions</a>
     */
    public static void escapeHtml(String str, Writer out) throws IOException {
        escapeEntities(Entities.HTML40, str, out);
    }

    /**
     * ����XML�Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     * 
     * <p>
     * ���磺<code>"bread" & "butter"</code>����ת����<tt>&amp;quot;bread&amp;quot; &amp;amp;
     * &amp;quot;butter&amp;quot;</tt>.
     * </p>
     * 
     * <p>
     * ֻת��4�ֻ�����XMLʵ�壺<code>gt</code>��<code>lt</code>��<code>quot</code>��<code>amp</code>��
     * ��֧��DTD���ⲿʵ�塣
     * </p>
     *
     * @param str Ҫת����ַ���
     *
     * @return ��ʵ�����ת����ַ��������ԭ�ִ�Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeXml(String str) {
        return escapeEntities(Entities.XML, str);
    }

    /**
     * ����XML�Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     * 
     * <p>
     * ���磺<code>"bread" & "butter"</code>����ת����<tt>&amp;quot;bread&amp;quot; &amp;amp;
     * &amp;quot;butter&amp;quot;</tt>.
     * </p>
     * 
     * <p>
     * ֻת��4�ֻ�����XMLʵ�壺<code>gt</code>��<code>lt</code>��<code>quot</code>��<code>amp</code>��
     * ��֧��DTD���ⲿʵ�塣
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeXml(String str, Writer out) throws IOException {
        escapeEntities(Entities.XML, str, out);
    }

    /**
     * ����ָ���Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     *
     * @param entities ʵ�弯��
     * @param str Ҫת����ַ���
     *
     * @return ��ʵ�����ת����ַ��������ԭ�ִ�Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String escapeEntities(Entities entities, String str) {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length());

            if (escapeEntitiesInternal(entities, str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣
        }
    }

    /**
     * ����ָ���Ĺ��򣬽��ַ����еĲ����ַ�ת����ʵ����롣
     *
     * @param entities ʵ�弯��
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void escapeEntities(Entities entities, String str, Writer out)
            throws IOException {
        escapeEntitiesInternal(entities, str, out);
    }

    /**
     * ��HTML�Ĺ�����ַ������з���ת�壬֧��HTML 4.0�е�����ʵ�壬�Լ�unicodeʵ����<code>&amp;#12345;</code>��
     * 
     * <p>
     * ���磺"&amp;lt;Fran&amp;ccedil;ais&amp;gt;"����ת����"&lt;Fran&ccedil;ais&gt;"
     * </p>
     * 
     * <p>
     * ���ʵ�岻�ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ������ת���ַ����ַ���
     *
     * @return �ָ���δת����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String unescapeHtml(String str) {
        return unescapeEntities(Entities.HTML40, str);
    }

    /**
     * ��HTML�Ĺ�����ַ������з���ת�壬֧��HTML 4.0�е�����ʵ�壬�Լ�unicodeʵ����<code>&amp;#12345;</code>��
     * 
     * <p>
     * ���磺"&amp;lt;Fran&amp;ccedil;ais&amp;gt;"����ת����"&lt;Fran&ccedil;ais&gt;"
     * </p>
     * 
     * <p>
     * ���ʵ�岻�ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ����ת���ַ����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void unescapeHtml(String str, Writer out)
            throws IOException {
        unescapeEntities(Entities.HTML40, str, out);
    }

    /**
     * ��XML�Ĺ�����ַ������з���ת�壬֧��unicodeʵ����<code>&amp;#12345;</code>��
     * 
     * <p>
     * ���磺"&amp;lt;Fran&amp;ccedil;ais&amp;gt;"����ת����"&lt;Fran&ccedil;ais&gt;"
     * </p>
     * 
     * <p>
     * ���ʵ�岻�ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ������ת���ַ����ַ���
     *
     * @return �ָ���δת����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String unescapeXml(String str) {
        return unescapeEntities(Entities.XML, str);
    }

    /**
     * ��XML�Ĺ�����ַ������з���ת�壬֧��unicodeʵ����<code>&amp;#12345;</code>��
     * 
     * <p>
     * ���磺"&amp;lt;Fran&amp;ccedil;ais&amp;gt;"����ת����"&lt;Fran&ccedil;ais&gt;"
     * </p>
     * 
     * <p>
     * ���ʵ�岻�ܱ�ʶ���������������䡣
     * </p>
     *
     * @param str ������ת���ַ����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void unescapeXml(String str, Writer out) throws IOException {
        unescapeEntities(Entities.XML, str, out);
    }

    /**
     * ��ָ���Ĺ�����ַ������з���ת�塣
     *
     * @param entities ʵ�弯��
     * @param str ������ת���ַ����ַ���
     *
     * @return �ָ���δת����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     */
    public static String unescapeEntities(Entities entities, String str) {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length());

            if (unescapeEntitiesInternal(entities, str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣
        }
    }

    /**
     * ��ָ���Ĺ�����ַ������з���ת�塣
     * 
     * <p>
     * ���ʵ�岻�ܱ�ʶ���������������䡣
     * </p>
     *
     * @param entities ʵ�弯��
     * @param str ������ת���ַ����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    public static void unescapeEntities(Entities entities, String str, Writer out)
            throws IOException {
        unescapeEntitiesInternal(entities, str, out);
    }

    /**
     * ���ַ����еĲ����ַ�ת����ʵ����롣
     *
     * @param entities ʵ�弯��
     * @param str Ҫת����ַ���
     * @param out �ַ������������Ϊ<code>null</code>
     *
     * @return ����ַ���û�б仯���򷵻�<code>false</code>
     *
     * @throws IllegalArgumentException ���<code>entities</code>�������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    private static boolean escapeEntitiesInternal(Entities entities, String str, Writer out)
            throws IOException {
        boolean needToChange = false;

        if (entities == null) {
            throw new IllegalArgumentException("The Entities must not be null");
        }

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        for (int i = 0; i < str.length(); ++i) {
            char   ch         = str.charAt(i);
            String entityName = entities.getEntityName(ch);

            if (entityName == null) {
                out.write(ch);
            } else {
                out.write('&');
                out.write(entityName);
                out.write(';');

                // ���øı��־
                needToChange = true;
            }
        }

        return needToChange;
    }

    /**
     * ���ַ����е��Ѷ���ʵ���unicodeʵ����<code>&amp;#12345;</code>ת������Ӧ��unicode�ַ���
     * 
     * <p>
     * δ�����ʵ�彫�������䡣
     * </p>
     *
     * @param entities ʵ�弯�ϣ����Ϊ<code>null</code>����ֻת��<code>&amp;#number</code>ʵ�塣
     * @param str ����ת���ַ����ַ���
     * @param out �ַ������������Ϊ<code>null</code>
     *
     * @return ����ַ���û�б仯���򷵻�<code>false</code>
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     */
    private static boolean unescapeEntitiesInternal(Entities entities, String str, Writer out)
            throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (ch == '&') {
                // ����&xxxx;
                int semi = str.indexOf(';', i + 1);

                if ((semi == -1) || ((i + 1) >= (semi - 1))) {
                    out.write(ch);
                    continue;
                }

                // �����&#xxxxx;
                if (str.charAt(i + 1) == '#') {
                    if ((i + 2) >= (semi - 1)) {
                        out.write(ch);
                        out.write('#');
                        i++;
                        continue;
                    }

                    try {
                        int entityValue = Integer.parseInt(str.substring(i + 2, semi));

                        out.write(entityValue);

                        // ���øı��־
                        needToChange = true;
                    } catch (NumberFormatException e) {
                        out.write(ch);
                        out.write('#');
                        i++;
                        continue;
                    }
                } else {
                    String entityName  = str.substring(i + 1, semi);
                    int    entityValue = -1;

                    if (entities != null) {
                        entityValue = entities.getEntityValue(entityName);
                    }

                    if (entityValue == -1) {
                        out.write('&');
                        out.write(entityName);
                        out.write(';');
                    } else {
                        out.write(entityValue);

                        // ���øı��־
                        needToChange = true;
                    }
                }

                i = semi;
            } else {
                out.write(ch);
            }
        }

        return needToChange;
    }

    /* ============================================================================ */
    /*  SQL��䡣                                                                   */
    /* ============================================================================ */

    /**
     * ��SQL���Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ���磺
     * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" + 
     *   StringEscapeUtil.escapeSql("McHale's Navy") + 
     *   "'");</pre>
     * </p>
     * 
     * <p>
     * Ŀǰ���˷���ֻ��������ת�������������ţ�<code>"McHale's Navy"</code>ת����<code>"McHale''s
     * Navy"</code>���������ַ����а�����<code>%</code>��<code>_</code>�ַ���
     * </p>
     *
     * @param str Ҫת����ַ���
     *
     * @return ת�����ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
     *
     * @see <a href="http://www.jguru.com/faq/view.jsp?EID=8881">faq</a>
     */
    public static String escapeSql(String str) {
        return StringUtil.replace(str, "'", "''");
    }

    /**
     * ��SQL���Ĺ�����ַ�������ת�塣
     * 
     * <p>
     * ���磺
     * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" + 
     *   StringEscapeUtil.escapeSql("McHale's Navy") + 
     *   "'");</pre>
     * </p>
     * 
     * <p>
     * Ŀǰ���˷���ֻ��������ת�������������ţ�<code>"McHale's Navy"</code>ת����<code>"McHale''s
     * Navy"</code>���������ַ����а�����<code>%</code>��<code>_</code>�ַ���
     * </p>
     *
     * @param str Ҫת����ַ���
     * @param out �����
     *
     * @throws IllegalArgumentException ��������Ϊ<code>null</code>
     * @throws IOException ������ʧ��
     *
     * @see <a href="http://www.jguru.com/faq/view.jsp?EID=8881">faq</a>
     */
    public static void escapeSql(String str, Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        String result = StringUtil.replace(str, "'", "''");

        if (result != null) {
            out.write(result);
        }
    }

    /* ============================================================================ */
    /*  URL/URI encoding/decoding��                                                 */
    /*                                                                              */
    /*  ����RFC2396��http://www.ietf.org/rfc/rfc2396.txt                            */
    /* ============================================================================ */

    /** "Alpha" characters from RFC 2396. */
    private static final BitSet ALPHA = new BitSet(256);

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            ALPHA.set(i);
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            ALPHA.set(i);
        }
    }

    /** "Alphanum" characters from RFC 2396. */
    private static final BitSet ALPHANUM = new BitSet(256);

    static {
        ALPHANUM.or(ALPHA);

        for (int i = '0'; i <= '9'; i++) {
            ALPHANUM.set(i);
        }
    }

    /** "Mark" characters from RFC 2396. */
    private static final BitSet MARK = new BitSet(256);

    static {
        MARK.set('-');
        MARK.set('_');
        MARK.set('.');
        MARK.set('!');
        MARK.set('~');
        MARK.set('*');
        MARK.set('\'');
        MARK.set('(');
        MARK.set(')');
    }

    /** "Reserved" characters from RFC 2396. */
    private static final BitSet RESERVED = new BitSet(256);

    static {
        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
    }

    /** "Unreserved" characters from RFC 2396. */
    private static final BitSet UNRESERVED = new BitSet(256);

    static {
        UNRESERVED.or(ALPHANUM);
        UNRESERVED.or(MARK);
    }

    /** ��һ������ת����16���Ƶ�ת���� */
    private static int[] HEXADECIMAL = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     * 
     * <p>
     * ����RFC2396�е�<code>unreserved</code>�ַ�֮��������ַ���������ת����URL����<code>%xx</code>��
     * ����RFC2396��<code>unreserved</code>�Ķ������£�
     * <pre><![CDATA
     *  unreserved  = alphanum | mark
     *  alphanum    = ��СдӢ����ĸ | ����
     *  mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
     * ]]></pre>
     * </p>
     * 
     * <p>
     * ���棺�÷���ʹ�õ�ǰ�߳�Ĭ�ϵ��ַ�����������URL����˸÷����ڲ�ͬ���������п��ܻ������ͬ�Ľ����
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     *
     * @return URL�������ַ���
     */
    public static String escapeURL(String str) {
        try {
            return escapeURLInternal(str, null, true);
        } catch (UnsupportedEncodingException e) {
            return str; // �����ܷ�������쳣
        }
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     * 
     * <p>
     * ����RFC2396�е�<code>unreserved</code>�ַ�֮��������ַ���������ת����URL����<code>%xx</code>��
     * ����RFC2396��<code>unreserved</code>�Ķ������£�
     * <pre><![CDATA
     *  unreserved  = alphanum | mark
     *  alphanum    = ��СдӢ����ĸ | ����
     *  mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
     * ]]></pre>
     * </p>
     * 
     * <p>
     * �÷���ʹ��ָ�����ַ�����������URL��
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     *
     * @return URL�������ַ���
     *
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     */
    public static String escapeURL(String str, String encoding)
            throws UnsupportedEncodingException {
        return escapeURLInternal(str, encoding, true);
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     * 
     * <p>
     * ���ָ������<code>strict</code>Ϊ<code>true</code>�����ϸ�ķ�ʽ����URL��
     * ����RFC2396�е�<code>unreserved</code>�ַ�֮��������ַ���������ת����URL����<code>%xx</code>��
     * ����RFC2396��<code>unreserved</code>�Ķ������£�
     * <pre><![CDATA
     *  unreserved  = alphanum | mark
     *  alphanum    = ��СдӢ����ĸ | ����
     *  mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
     * ]]></pre>
     * </p>
     * 
     * <p>
     * ���ָ������<code>strict</code>Ϊ<code>false</code>����ʹ�ÿ��ɵķ�ʽ����URL��
     * ���˿����ַ����հ��ַ��Լ�RFC2396�е�<code>reserved</code>�ַ�֮��������ַ����������������䡣
     * ����RFC2396��ֻ�п����ַ����հ��ַ��Լ���������<code>reserved</code>������ַ��ű�ת����<code>%xx</code>��ʽ��
     * <pre><![CDATA
     *  reserved      = ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" | "$" | ","
     * ]]></pre>
     * </p>
     * 
     * <p>
     * �÷���ʹ��ָ�����ַ�����������URL��
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ�õ�ǰ�߳�Ĭ�ϵı���
     * @param strict �Ƿ����ϸ�ķ�ʽ����URL
     *
     * @return URL�������ַ���
     *
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     */
    public static String escapeURL(String str, String encoding, boolean strict)
            throws UnsupportedEncodingException {
        return escapeURLInternal(str, encoding, strict);
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     * 
     * <p>
     * ����RFC2396�е�<code>unreserved</code>�ַ�֮��������ַ���������ת����URL����<code>%xx</code>��
     * ����RFC2396��<code>unreserved</code>�Ķ������£�
     * <pre><![CDATA
     *  unreserved  = alphanum | mark
     *  alphanum    = ��СдӢ����ĸ | ����
     *  mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
     * ]]></pre>
     * </p>
     * 
     * <p>
     * �÷���ʹ��ָ�����ַ�����������URL��
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param out �����ָ���ַ���
     *
     * @throws IOException ��������<code>out</code>ʧ��
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     * @throws IllegalArgumentException <code>out</code>Ϊ<code>null</code>
     */
    public static void escapeURL(String str, String encoding, Writer out)
            throws IOException {
        escapeURLInternal(str, encoding, out, true);
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     * 
     * <p>
     * ���ָ������<code>strict</code>Ϊ<code>true</code>�����ϸ�ķ�ʽ����URL��
     * ����RFC2396�е�<code>unreserved</code>�ַ�֮��������ַ���������ת����URL����<code>%xx</code>��
     * ����RFC2396��<code>unreserved</code>�Ķ������£�
     * <pre><![CDATA
     *  unreserved  = alphanum | mark
     *  alphanum    = ��СдӢ����ĸ | ����
     *  mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
     * ]]></pre>
     * </p>
     * 
     * <p>
     * ���ָ������<code>strict</code>Ϊ<code>false</code>����ʹ�ÿ��ɵķ�ʽ����URL��
     * ���˿����ַ����հ��ַ��Լ�RFC2396�е�<code>reserved</code>�ַ�֮��������ַ����������������䡣
     * ����RFC2396��ֻ�п����ַ����հ��ַ��Լ���������<code>reserved</code>������ַ��ű�ת����<code>%xx</code>��ʽ��
     * <pre><![CDATA
     *  reserved      = ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" | "$" | ","
     * ]]></pre>
     * </p>
     * 
     * <p>
     * �÷���ʹ��ָ�����ַ�����������URL��
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param out �����ָ���ַ���
     * @param strict �Ƿ����ϸ�ķ�ʽ����URL
     *
     * @throws IOException ��������<code>out</code>ʧ��
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     * @throws IllegalArgumentException <code>out</code>Ϊ<code>null</code>
     */
    public static void escapeURL(String str, String encoding, Writer out, boolean strict)
            throws IOException {
        escapeURLInternal(str, encoding, out, strict);
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param strict �Ƿ����ϸ�ķ�ʽ����URL
     *
     * @return URL�������ַ���
     *
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     */
    private static String escapeURLInternal(String str, String encoding, boolean strict)
            throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length());

            if (escapeURLInternal(str, encoding, out, strict)) {
                return out.toString();
            }

            return str;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣
        }
    }

    /**
     * ��ָ���ַ��������<code>application/x-www-form-urlencoded</code>��ʽ��
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param strict �Ƿ����ϸ�ķ�ʽ����URL
     * @param out �����
     *
     * @return ����ַ������ı䣬�򷵻�<code>true</code>
     *
     * @throws IOException ��������<code>out</code>ʧ��
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     * @throws IllegalArgumentException <code>out</code>Ϊ<code>null</code>
     */
    private static boolean escapeURLInternal(String str, String encoding, Writer out, boolean strict)
            throws IOException {
        if (encoding == null) {
            encoding = LocaleUtil.getContext().getCharset();
        }

        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        // �������ַ�=>�ֽڵ���ʱ�ռ䣬����Ϊ10���ӡ�
        ByteArrayOutputStream baos   = new ByteArrayOutputStream(10);
        OutputStreamWriter    writer = new OutputStreamWriter(baos, encoding);

        for (int i = 0; i < str.length(); i++) {
            int ch = str.charAt(i);

            if (isSafeCharacter(ch, strict)) {
                // ����ȫ�����ַ���ֱ�����
                out.write(ch);
            } else if (ch == ' ') {
                // ����������ո�0x20��ת����'+'
                out.write('+');

                // ���øı��־
                needToChange = true;
            } else {
                // ��ch����URL���롣
                // ���Ȱ�ָ��encodingȡ�ø��ַ����ֽ��롣
                try {
                    writer.write(ch);
                    writer.flush();
                } catch (IOException e) {
                    baos.reset();
                    continue;
                }

                byte[] bytes = baos.toByteArray();

                for (int j = 0; j < bytes.length; j++) {
                    byte toEscape = bytes[j];

                    out.write('%');

                    int low  = (toEscape & 0x0F);
                    int high = (toEscape & 0xF0) >> 4;

                    out.write(HEXADECIMAL[high]);
                    out.write(HEXADECIMAL[low]);
                }

                baos.reset();

                // ���øı��־
                needToChange = true;
            }
        }

        return needToChange;
    }

    /**
     * �ж�ָ���ַ��Ƿ��ǡ���ȫ���ģ�����ַ�������ת����URL���롣
     *
     * @param ch Ҫ�жϵ��ַ�
     * @param strict �Ƿ����ϸ�ķ�ʽ����
     *
     * @return ����ǡ���ȫ���ģ��򷵻�<code>true</code>
     */
    private static boolean isSafeCharacter(int ch, boolean strict) {
        if (strict) {
            return UNRESERVED.get(ch);
        } else {
            return (ch > ' ') && !RESERVED.get(ch) && !Character.isWhitespace((char) ch);
        }
    }

    /**
     * ����<code>application/x-www-form-urlencoded</code>��ʽ���ַ�����
     * 
     * <p>
     * ���棺�÷���ʹ��ϵͳ�ַ�����������URL����˸÷����ڲ�ͬ��ϵͳ�п��ܻ������ͬ�Ľ����
     * </p>
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     *
     * @return URL�������ַ���
     */
    public static String unescapeURL(String str) {
        try {
            return unescapeURLInternal(str, null);
        } catch (UnsupportedEncodingException e) {
            return str; // �����ܷ�������쳣
        }
    }

    /**
     * ����<code>application/x-www-form-urlencoded</code>��ʽ���ַ�����
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     *
     * @return URL�������ַ���
     *
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     */
    public static String unescapeURL(String str, String encoding)
            throws UnsupportedEncodingException {
        return unescapeURLInternal(str, encoding);
    }

    /**
     * ����<code>application/x-www-form-urlencoded</code>��ʽ���ַ�����
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param out �����
     *
     * @throws IOException ��������<code>out</code>ʧ��
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     * @throws IllegalArgumentException <code>out</code>Ϊ<code>null</code>
     */
    public static void unescapeURL(String str, String encoding, Writer out)
            throws IOException {
        unescapeURLInternal(str, encoding, out);
    }

    /**
     * ����<code>application/x-www-form-urlencoded</code>��ʽ���ַ�����
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     *
     * @return URL�������ַ���
     *
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     */
    private static String unescapeURLInternal(String str, String encoding)
            throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }

        try {
            StringWriter out = new StringWriter(str.length());

            if (unescapeURLInternal(str, encoding, out)) {
                return out.toString();
            }

            return str;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            return str; // StringWriter�����ܷ�������쳣
        }
    }

    /**
     * ����<code>application/x-www-form-urlencoded</code>��ʽ���ַ�����
     *
     * @param str Ҫ������ַ�����������<code>null</code>
     * @param encoding ����ַ����룬���Ϊ<code>null</code>����ʹ��ϵͳĬ�ϱ���
     * @param out �����
     *
     * @return ����ַ������ı䣬�򷵻�<code>true</code>
     *
     * @throws IOException ��������<code>out</code>ʧ��
     * @throws UnsupportedEncodingException ���ָ����<code>encoding</code>Ϊ�Ƿ���
     * @throws IllegalArgumentException <code>out</code>Ϊ<code>null</code>
     */
    private static boolean unescapeURLInternal(String str, String encoding, Writer out)
            throws IOException {
        if (encoding == null) {
            encoding = LocaleUtil.getContext().getCharset();
        }

        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        int    length = str.length();
        byte[] buffer = null;
        int    pos    = 0;

        for (int i = 0; i < length; i++) {
            int ch = str.charAt(i);

            if (ch < 256) {
                // ��ȡ�������ֽڣ���������ָ������ת�����ַ���
                if (buffer == null) {
                    buffer = new byte[length - i]; // �ֻ��Ҫlength - i
                }

                switch (ch) {
                    case '+':

                        // ��'+'ת����' '
                        buffer[pos++] = ' ';

                        // ���øı��־
                        needToChange = true;
                        break;

                    case '%':

                        if ((i + 2) < length) {
                            try {
                                buffer[pos] = (byte) Integer.parseInt(str.substring(i + 1, i + 3),
                                        16);
                                pos++;
                                i += 2;

                                // ���øı��־
                                needToChange = true;
                            } catch (NumberFormatException e) {
                                // ���%xx���ǺϷ���16����������ԭ�����
                                buffer[pos++] = (byte) ch;
                            }
                        } else {
                            buffer[pos++] = (byte) ch;
                        }

                        break;

                    default:

                        // д��bytes�У���ʱһ�������
                        buffer[pos++] = (byte) ch;
                        break;
                }
            } else {
                // �Ƚ�buffer�е��ֽڴ�ת�����ַ�����
                if (pos > 0) {
                    out.write(new String(buffer, 0, pos, encoding));
                    pos = 0;
                }

                // ���ch��ISO-8859-1������ַ���ֱ���������
                out.write(ch);
            }
        }

        // �Ƚ�buffer�е��ֽڴ�ת�����ַ�����
        if (pos > 0) {
            out.write(new String(buffer, 0, pos, encoding));
            pos = 0;
        }

        return needToChange;
    }
}
