package com.tenpay.sm.lang.util;

/**
 * 替换html中 < > & '" \ (char)133 这些字符的工具，避免xss等
 * @author li.hongtl
 *
 */
public final class HtmlEncoder {

    /**
     * Translates a string into a HTML safe format.
     *
     * @param s the string to encode
     * @return the encoded string
     */
    public static String encode(String s) {
        char [] htmlChars = s.toCharArray();
        StringBuffer encodedHtml = new StringBuffer();
        for (int i=0; i<htmlChars.length; i++) {
            switch(htmlChars[i]) {
            case '<':
                encodedHtml.append("&lt;");
                break;
            case '>':
                encodedHtml.append("&gt;");
                break;
            case '&':
                encodedHtml.append("&amp;");
                break;
            case '\'':
                encodedHtml.append("&#39;");
                break;
            case '"':
                encodedHtml.append("&quot;");
                break;
            case '\\':
                encodedHtml.append("&#92;");
                break;
            case (char)133:
                encodedHtml.append("&#133;");
                break;
            default:
                encodedHtml.append(htmlChars[i]);
                break;
            }
        }
        return encodedHtml.toString();
    }
}
