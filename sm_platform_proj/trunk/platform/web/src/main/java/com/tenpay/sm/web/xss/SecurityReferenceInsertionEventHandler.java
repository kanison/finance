/**
 * 
 */
package com.tenpay.sm.web.xss;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

//import com.alibaba.china.fasttext.security.xss.Policy;
//import com.alibaba.china.fasttext.security.xss.PolicyException;
//import com.alibaba.china.fasttext.security.xss.XssXppScanner;

/**
 * <pre>
 * 1.html输出, 什么都不执行， 按原始格式输出。 他并不是真正的不执行任何变
 * 化， 因为他会执行xss的过滤动作。一个非常复杂的安全处理过程， 如果不是输
 * 出HTML， 请勿使用。 这个表示将会消耗大量的CPU处理。
 * #ZHTML($html)
 * 2.xml编码输出， 将会执行 xml encode输出
 * #ZXML($xml)
 * 3.js编码输出 , 将会执行javascript encode输出
 * #ZJS($js)
 * 4.纯文本编码输出, 将会执行html encode
 * $pureText
 * 5. 已经被业务代码escaped过的输出
 * 这个目前没有什么好的办法， 只能修改业务代码， 去除这些escaped的地方。
 * 使用find . |grep java$ |xargs grep -i StringEscapeUtil.escapeHtml 来寻找
 * 程序中出现的， 我找了下exodus里的代码， 有21个地方是这么做的。 我想这个
 * 修改起来的难度是比较低的。
 * 6. 使用$stringEscapeUtil.escapeHtml($text)输出的地方
 * 由于代码修改起来比较麻烦， 我们使用一个宏来处理这个问题, 这个使用一个统
 * 一替换工作。把$stringEscapeUtil.escapeHtml 替换成
 * #ZPURETEXT($text)
 * 7. URL的输出, 也许我们就将来要做URL的安全检查， 因此， 请勿使用在非URL出
 * 现的地方 虽然他什么转义都不做。
 * 一般情况下， 我们的URL输出使用URIBoker对象， 但是， 我注意到了， 有部分
 * URL使用了URIBoker.render() 或者， URIBoker.toString() 这么输出， 这个会
 * 造成URL被html encode. 这个是很无奈的。
 * 如果发生这个情况， 请加上
 * #ZURL($url), 这个宏指示了不要使用html encode.
 * 8. 为了避免发生歧义， #ZHTML虽然是不转义， 但是我们明确说的是HTML， 但
 * 是， 如果真有不需要转义的情况出现， 我们可以使用
 * #ZLITERAL($text)
 * tips: literal - 照字面的;原义的.
 * 
 * 加一个Z是为了尽量避免和应用定义的宏出现冲突， 没有别的含义. 但是我们仍然
 * 有几个地方需要我们注意的地方：
 * 
 * 1．扩展实现的是编码输出String对象， 如果你输出的不是String对象， 这些宏
 * 操作将会失效， 这个是为了提高系统的编码性能，采取的态度。 如果一个对象没
 * 有重新实现toString()的， 这个对象的输出是不可能有html编码的问题的。因为
 * Object.toString实现的是对象地址， 另外实现了toString的方法， 我们姑且认
 * 为是安全， 这个虽然严格意义上是不正确的，但是， 我们的系统的确基本是这样
 * 工作的。
 * 2．如何避免一个数据被多次encode。
 * 首先我们假设 $text=&quot;s&amp;&quot;
 * a.$stringUtil.getXXX($text)
 * 这个形式输出是 s&amp;
 * b.$stringUtil.getXXX(&quot;$text&quot;)
 * 这个形式输出是 s&amp;amp;
 * 为什么会这样呢，&quot;&quot;是告诉velocity, 先输出， 后把参数送给
 * #stringUtil.getXXX. 这是一个非常需要注意的地方，这个地方导致了大量的地方
 * 需要修改的地方。 也是目前最无奈的地方。这个
 * 这个会造成一些变形的特殊情况， 比如
 * $stringUtil.equals(&quot;$text&quot;， &quot;&amp;&quot;)
 * 这样情况就是， &quot;$text&quot;, 这个已经发生htlml encode, 然而 &quot;&amp;&quot;是常量， 常量
 * 是无法被转义的，因此这个就是发生严重的bug.
 * 解决的办法是这么写：
 * $stringUtil.equals($text， &quot;&amp;&quot;)
 * c.$stringUtil.getXXX(&quot;the pro: $text&quot;)
 * 这个情况， 目前我们的系统也是比较多， 常量与输出的混合. 解决这个问题的办
 * 法是：HTML_XSS_FILTER
 * $stringUtil.getXXX(&quot;the pro: #ZLITERAL($text)&quot;)
 * 
 * d.$control.setTemplate($text)
 * 这个系统会自动处理
 * e.$stringUtil.escapeHtml($text)
 * 这个上面的第6条作出了解释
 * f.$screen_placeholder
 * 这个系统会自动处理
 * 
 * </pre>
 * 
 * @author leon
 */
public class SecurityReferenceInsertionEventHandler extends RootReferenceFilter implements ReferenceInsertionEventHandler {

    private static final Log logger = LogFactory.getLog(SecurityReferenceInsertionEventHandler.class);
    private XssXppScanner    scanner;

    public SecurityReferenceInsertionEventHandler(){
//        try {
//            scanner = new XssXppScanner(Policy.getLoosePolicyInstance());
//        } catch (PolicyException e) {
//            throw new RuntimeException(e);
//        }
    	scanner = new XssXppScanner();
    }

    /**
     * @see ReferenceAction
     */
    public Object referenceInsert(String reference, Object value) {
        if (value instanceof String) {
            ReferenceAction action = getReferenceAction(reference);
            String retValue = (String) value;
            switch (action) {
                case HTML_XSS_FILTER:
                    retValue = scanner.scan(retValue);
                    break;

                case JAVASCRIPT_ENCODE:
                    FastEntities.escapeJavaScript(retValue);
                    break;
                case XML_ENCODE:
                    retValue = XML.escape(retValue);
                    break;
                case LITERAL:
                case URL_FILTER:// do nothing
                    break;
                case HTML_ENCODE:
                    retValue = HTML40.escape(retValue);
                    break;
                case PURE_TEXT_HTML_ENCODE:
                    retValue = HTML40.escape(retValue);
                    break;
                case NOT_DEFINED:
                default:
                    // 在默认的情况下，　日志记录，　可能是html的输出
                    if (logger.isDebugEnabled()) {
                        if (retValue.indexOf('<') > -1 && retValue.indexOf('>') > -1) {
                            logger.debug("reference=" + reference + " maybe is a html content");
                        }
                    }
                    retValue = HTML40.escape(retValue);
                    break;
            }
            return retValue;
        }
        return value;
    }
}

/**
 * <pre>
 * HTML_XSS_FILTER: 使用xss过滤工具输出
 * HTML_ENCODE: 按html encode输出
 * JAVASCRIPT_ENCODE: 按js encode 输出
 * XML_ENCODE: 按xml encode输出
 * LITERAL: 按原来的输出
 * URL_FILTER: 暂时没有做任何编码行为 
 * PURE_TEXT_HTML_ENCODE: 兼容原来的系统使用， 为了避免发生二次编码
 * NOT_DEFINED: 未定义的， 使用html encode
 * </pre>
 */
enum ReferenceAction {
    HTML_XSS_FILTER, HTML_ENCODE, JAVASCRIPT_ENCODE, XML_ENCODE, LITERAL, URL_FILTER, PURE_TEXT_HTML_ENCODE,
    NOT_DEFINED
}

/**
 * <pre>
 * #########html xss filter#############
 * #macro(SHTML $SHTML_)$SHTML_#end
 * #########xml encode #############
 * #macro(SXML $SXML_)$SXML_#end
 * #########javascript encode #############
 * #macro(SJS $SJS_)$SJS_#end
 * #########pure text html encode, it is a compatibility purpose, shun double escaped#############
 * #macro(SPURETEXT $SPURETEXT_)$SPURETEXT_#end
 * #########pure text as literal #############
 * #macro(SLITERAL $SLITERAL_)$SLITERAL_#end
 * #########url output #############
 * #macro(SURL $SURL_)$SURL_#end
 *</pre>
 */
class RootReferenceFilter extends FastEntities {

    static Map<String, ReferenceAction> filter = new HashMap<String, ReferenceAction>();

    static {
        // system define
        filter.put("$screen_placeholder", ReferenceAction.LITERAL);

        // encode part
        filter.put("$!{security_h_t_m_l_z}", ReferenceAction.HTML_XSS_FILTER);
        filter.put("$!{security_x_m_l_z}", ReferenceAction.XML_ENCODE);
        filter.put("$!{security_j_s_z}", ReferenceAction.JAVASCRIPT_ENCODE);
        filter.put("$!{security_p_u_r_e_t_e_x_t_z}", ReferenceAction.HTML_ENCODE);
        filter.put("$!{security_l_i_t_e_r_a_l_z}", ReferenceAction.LITERAL);
        filter.put("$!{security_u_r_l_z}", ReferenceAction.URL_FILTER);
    }

    public static ReferenceAction getReferenceAction(String ref) {
        ReferenceAction action = filter.get(ref);
        if (action == null) {
            action = ReferenceAction.NOT_DEFINED;
        }
        return action;
    }

}

/**
 * 快速html/js/xml 编码实现
 */
class FastEntities extends FastDetectChar {

    private static final String[][]  BASIC_ARRAY     = { { "&quot;", "34" }, { "&amp;", "38" }, { "&lt;", "60" },
            { "&gt;", "62" },                       };
    private static final String[][]  APOS_ARRAY      = { { "&apos;", "39" }, };
    private static final String[][]  ISO8859_1_ARRAY = { { "&nbsp;", "160" }, { "&iexcl;", "161" },
            { "&cent;", "162" }, { "&pound;", "163" }, { "&curren;", "164" }, { "&yen;", "165" },
            { "&brvbar;", "166" }, { "&sect;", "167" }, { "&uml;", "168" }, { "&copy;", "169" }, { "&ordf;", "170" },
            { "&laquo;", "171" }, { "&not;", "172" }, { "&shy;", "173" }, { "&reg;", "174" }, { "&macr;", "175" },
            { "&deg;", "176" }, { "&plusmn;", "177" }, { "&sup2;", "178" }, { "&sup3;", "179" }, { "&acute;", "180" },
            { "&micro;", "181" }, { "&para;", "182" }, { "&middot;", "183" }, { "&cedil;", "184" },
            { "&sup1;", "185" }, { "&ordm;", "186" }, { "&raquo;", "187" }, { "&frac14;", "188" },
            { "&frac12;", "189" }, { "&frac34;", "190" }, { "&iquest;", "191" }, { "&Agrave;", "192" },
            { "&Aacute;", "193" }, { "&Acirc;", "194" }, { "&Atilde;", "195" }, { "&Auml;", "196" },
            { "&Aring;", "197" }, { "&AElig;", "198" }, { "&Ccedil;", "199" }, { "&Egrave;", "200" },
            { "&Eacute;", "201" }, { "&Ecirc;", "202" }, { "&Euml;", "203" }, { "&Igrave;", "204" },
            { "&Iacute;", "205" }, { "&Icirc;", "206" }, { "&Iuml;", "207" }, { "&ETH;", "208" },
            { "&Ntilde;", "209" }, { "&Ograve;", "210" }, { "&Oacute;", "211" }, { "&Ocirc;", "212" },
            { "&Otilde;", "213" }, { "&Ouml;", "214" }, { "&times;", "215" }, { "&Oslash;", "216" },
            { "&Ugrave;", "217" }, { "&Uacute;", "218" }, { "&Ucirc;", "219" }, { "&Uuml;", "220" },
            { "&Yacute;", "221" }, { "&THORN;", "222" }, { "&szlig;", "223" }, { "&agrave;", "224" },
            { "&aacute;", "225" }, { "&acirc;", "226" }, { "&atilde;", "227" }, { "&auml;", "228" },
            { "&aring;", "229" }, { "&aelig;", "230" }, { "&ccedil;", "231" }, { "&egrave;", "232" },
            { "&eacute;", "233" }, { "&ecirc;", "234" }, { "&euml;", "235" }, { "&igrave;", "236" },
            { "&iacute;", "237" }, { "&icirc;", "238" }, { "&iuml;", "239" }, { "&eth;", "240" },
            { "&ntilde;", "241" }, { "&ograve;", "242" }, { "&oacute;", "243" }, { "&ocirc;", "244" },
            { "&otilde;", "245" }, { "&ouml;", "246" }, { "&divide;", "247" }, { "&oslash;", "248" },
            { "&ugrave;", "249" }, { "&uacute;", "250" }, { "&ucirc;", "251" }, { "&uuml;", "252" },
            { "&yacute;", "253" }, { "&thorn;", "254" }, { "&yuml;", "255" }, };
    private static final String[][]  HTML40_ARRAY    = { { "&fnof;", "402" }, { "&Alpha;", "913" },
            { "&Beta;", "914" }, { "&Gamma;", "915" }, { "&Delta;", "916" }, { "&Epsilon;", "917" },
            { "&Zeta;", "918" }, { "&Eta;", "919" }, { "&Theta;", "920" }, { "&Iota;", "921" }, { "&Kappa;", "922" },
            { "&Lambda;", "923" }, { "&Mu;", "924" }, { "&Nu;", "925" }, { "&Xi;", "926" }, { "&Omicron;", "927" },
            { "&Pi;", "928" }, { "&Rho;", "929" },

            { "&Sigma;", "931" }, { "&Tau;", "932" }, { "&Upsilon;", "933" }, { "&Phi;", "934" }, { "&Chi;", "935" },
            { "&Psi;", "936" }, { "&Omega;", "937" }, { "&alpha;", "945" }, { "&beta;", "946" }, { "&gamma;", "947" },
            { "&delta;", "948" }, { "&epsilon;", "949" }, { "&zeta;", "950" }, { "&eta;", "951" },
            { "&theta;", "952" }, { "&iota;", "953" }, { "&kappa;", "954" }, { "&lambda;", "955" }, { "&mu;", "956" },
            { "&nu;", "957" }, { "&xi;", "958" }, { "&omicron;", "959" }, { "&pi;", "960" }, { "&rho;", "961" },
            { "&sigmaf;", "962" }, { "&sigma;", "963" }, { "&tau;", "964" }, { "&upsilon;", "965" },
            { "&phi;", "966" }, { "&chi;", "967" }, { "&psi;", "968" }, { "&omega;", "969" }, { "&thetasym;", "977" },
            { "&upsih;", "978" }, { "&piv;", "982" }, { "&bull;", "8226" }, { "&hellip;", "8230" },
            { "&prime;", "8242" }, { "&Prime;", "8243" }, { "&oline;", "8254" }, { "&frasl;", "8260" },
            { "&weierp;", "8472" }, { "&image;", "8465" }, { "&real;", "8476" }, { "&trade;", "8482" },
            { "&alefsym;", "8501" }, { "&larr;", "8592" }, { "&uarr;", "8593" }, { "&rarr;", "8594" },
            { "&darr;", "8595" }, { "&harr;", "8596" }, { "&crarr;", "8629" }, { "&lArr;", "8656" },
            { "&uArr;", "8657" }, { "&rArr;", "8658" }, { "&dArr;", "8659" }, { "&hArr;", "8660" },
            { "&forall;", "8704" }, { "&part;", "8706" }, { "&exist;", "8707" }, { "&empty;", "8709" },
            { "&nabla;", "8711" }, { "&isin;", "8712" }, { "&notin;", "8713" }, { "&ni;", "8715" },
            { "&prod;", "8719" }, { "&sum;", "8721" }, { "&minus;", "8722" }, { "&lowast;", "8727" },
            { "&radic;", "8730" }, { "&prop;", "8733" }, { "&infin;", "8734" }, { "&ang;", "8736" },
            { "&and;", "8743" }, { "&or;", "8744" }, { "&cap;", "8745" }, { "&cup;", "8746" }, { "&int;", "8747" },
            { "&there4;", "8756" }, { "&sim;", "8764" }, { "&cong;", "8773" }, { "&asymp;", "8776" },
            { "&ne;", "8800" }, { "&equiv;", "8801" }, { "&le;", "8804" }, { "&ge;", "8805" }, { "&sub;", "8834" },
            { "&sup;", "8835" }, { "&sube;", "8838" }, { "&supe;", "8839" }, { "&oplus;", "8853" },
            { "&otimes;", "8855" }, { "&perp;", "8869" }, { "&sdot;", "8901" }, { "&lceil;", "8968" },
            { "&rceil;", "8969" }, { "&lfloor;", "8970" }, { "&rfloor;", "8971" }, { "&lang;", "9001" },
            { "&rang;", "9002" }, { "&loz;", "9674" }, { "&spades;", "9824" }, { "&clubs;", "9827" },
            { "&hearts;", "9829" }, { "&diams;", "9830" }, { "&OElig;", "338" }, { "&oelig;", "339" },
            { "&Scaron;", "352" }, { "&scaron;", "353" }, { "&Yuml;", "376" }, { "&circ;", "710" },
            { "&tilde;", "732" }, { "&ensp;", "8194" }, { "&emsp;", "8195" }, { "&thinsp;", "8201" },
            { "&zwnj;", "8204" }, { "&zwj;", "8205" }, { "&lrm;", "8206" }, { "&rlm;", "8207" }, { "&ndash;", "8211" },
            { "&mdash;", "8212" }, { "&lsquo;", "8216" }, { "&rsquo;", "8217" }, { "&sbquo;", "8218" },
            { "&ldquo;", "8220" }, { "&rdquo;", "8221" }, { "&bdquo;", "8222" }, { "&dagger;", "8224" },
            { "&Dagger;", "8225" }, { "&permil;", "8240" }, { "&lsaquo;", "8249" }, { "&rsaquo;", "8250" },
            { "&euro;", "8364" },                   };
    public static final FastEntities XML;
    public static final FastEntities HTML40;

    static {
        XML = new FastEntities();
        XML.addEntities(BASIC_ARRAY);
        XML.addEntities(APOS_ARRAY);
    }

    static {
        HTML40 = new FastEntities();
        HTML40.addEntities(BASIC_ARRAY);
        HTML40.addEntities(ISO8859_1_ARRAY);
        HTML40.addEntities(HTML40_ARRAY);
    }

    public void addEntities(String[][] entityArray) {
        for (int i = 0; i < entityArray.length; ++i) {
            char c = (char) Integer.parseInt(entityArray[i][1]);
            addEntity(c, entityArray[i][0]);
            this.addTransferChar(c);
        }
    }

    /**
     * <pre>
     * 做了几个有速度的处理, 在很多情况下，可能提高１０倍以上的性能，　以及节约不少内存使用。
     * 1.返回结果根据时候发生了编码进行改进，　如果没有发生编码，那么直接返回原来的str.
     * 2.快速检查是否要进行转码
     * 3.使用快速查表，　对0-255的字符进行快速查找
     * </pre>
     * 
     * @param str
     * @return
     */
    public String escape(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder buffer = null;
        int len = str.length();
        char ch;
        char[] entityName;
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            entityName = getEntity(ch);
            if (entityName == null) {
                if (buffer != null) {
                    buffer.append(ch);
                }
            } else {
                if (buffer == null) {
                    buffer = new StringBuilder(str.length() << 1);
                    buffer.append(str, 0, i);
                }
                buffer.append(entityName);
            }
        }
        if (buffer != null) {
            return buffer.toString();
        } else {
            return str;
        }
    }

    public static String escapeJavaScript(String str) {

        if (str == null) {
            return null;
        }
        int length = str.length();
        Writer out = new StringWriter(length << 1);
        try {
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

                } else {
                    switch (ch) {
                        case '\'':
                            out.write('\\');
                            out.write('\'');
                            break;
                        case '"':
                            out.write('\\');
                            out.write('"');
                            break;
                        case '\\':
                            out.write('\\');
                            out.write('\\');
                            break;
                        default:
                            out.write(ch);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            // impossible
        }
        return out.toString();
    }

}

class FastDetectChar {

    private Map<Character, char[]> map               = new HashMap<Character, char[]>();
    private int                    LOOKUP_TABLE_SIZE = 256;
    private char[][]               lookupTable       = new char[LOOKUP_TABLE_SIZE][];
    BitSet                         maskSet           = new BitSet(1 << 16);

    public void addEntity(char charValue, String entiryName) {
        if (charValue > -1 && charValue < LOOKUP_TABLE_SIZE) {
            lookupTable[charValue] = entiryName.toCharArray();
        }
        map.put(charValue, entiryName.toCharArray());
    }

    public char[] getEntity(char charValue) {
        if (charValue < LOOKUP_TABLE_SIZE) {
            return lookupTable[charValue];
        }
        return (maskSet.get(charValue) ? map.get(charValue) : null);
    }

    /**
     * 增加一个跳越字符
     * 
     * @param c
     */
    public void addTransferChar(char c) {
        maskSet.set(c);
    }
}