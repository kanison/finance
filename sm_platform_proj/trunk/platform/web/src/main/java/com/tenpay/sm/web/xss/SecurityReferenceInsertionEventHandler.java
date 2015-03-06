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
 * 1.html���, ʲô����ִ�У� ��ԭʼ��ʽ����� �������������Ĳ�ִ���κα�
 * ���� ��Ϊ����ִ��xss�Ĺ��˶�����һ���ǳ����ӵİ�ȫ������̣� ���������
 * ��HTML�� ����ʹ�á� �����ʾ�������Ĵ�����CPU����
 * #ZHTML($html)
 * 2.xml��������� ����ִ�� xml encode���
 * #ZXML($xml)
 * 3.js������� , ����ִ��javascript encode���
 * #ZJS($js)
 * 4.���ı��������, ����ִ��html encode
 * $pureText
 * 5. �Ѿ���ҵ�����escaped�������
 * ���Ŀǰû��ʲô�õİ취�� ֻ���޸�ҵ����룬 ȥ����Щescaped�ĵط���
 * ʹ��find . |grep java$ |xargs grep -i StringEscapeUtil.escapeHtml ��Ѱ��
 * �����г��ֵģ� ��������exodus��Ĵ��룬 ��21���ط�����ô���ġ� �������
 * �޸��������Ѷ��ǱȽϵ͵ġ�
 * 6. ʹ��$stringEscapeUtil.escapeHtml($text)����ĵط�
 * ���ڴ����޸������Ƚ��鷳�� ����ʹ��һ�����������������, ���ʹ��һ��ͳ
 * һ�滻��������$stringEscapeUtil.escapeHtml �滻��
 * #ZPURETEXT($text)
 * 7. URL�����, Ҳ�����Ǿͽ���Ҫ��URL�İ�ȫ��飬 ��ˣ� ����ʹ���ڷ�URL��
 * �ֵĵط� ��Ȼ��ʲôת�嶼������
 * һ������£� ���ǵ�URL���ʹ��URIBoker���� ���ǣ� ��ע�⵽�ˣ� �в���
 * URLʹ����URIBoker.render() ���ߣ� URIBoker.toString() ��ô����� �����
 * ���URL��html encode. ����Ǻ����εġ�
 * ��������������� �����
 * #ZURL($url), �����ָʾ�˲�Ҫʹ��html encode.
 * 8. Ϊ�˱��ⷢ�����壬 #ZHTML��Ȼ�ǲ�ת�壬 ����������ȷ˵����HTML�� ��
 * �ǣ� ������в���Ҫת���������֣� ���ǿ���ʹ��
 * #ZLITERAL($text)
 * tips: literal - �������;ԭ���.
 * 
 * ��һ��Z��Ϊ�˾��������Ӧ�ö���ĺ���ֳ�ͻ�� û�б�ĺ���. ����������Ȼ
 * �м����ط���Ҫ����ע��ĵط���
 * 
 * 1����չʵ�ֵ��Ǳ������String���� ���������Ĳ���String���� ��Щ��
 * ��������ʧЧ�� �����Ϊ�����ϵͳ�ı������ܣ���ȡ��̬�ȡ� ���һ������û
 * ������ʵ��toString()�ģ� ������������ǲ�������html���������ġ���Ϊ
 * Object.toStringʵ�ֵ��Ƕ����ַ�� ����ʵ����toString�ķ����� ���ǹ�����
 * Ϊ�ǰ�ȫ�� �����Ȼ�ϸ��������ǲ���ȷ�ģ����ǣ� ���ǵ�ϵͳ��ȷ����������
 * �����ġ�
 * 2����α���һ�����ݱ����encode��
 * �������Ǽ��� $text=&quot;s&amp;&quot;
 * a.$stringUtil.getXXX($text)
 * �����ʽ����� s&amp;
 * b.$stringUtil.getXXX(&quot;$text&quot;)
 * �����ʽ����� s&amp;amp;
 * Ϊʲô�������أ�&quot;&quot;�Ǹ���velocity, ������� ��Ѳ����͸�
 * #stringUtil.getXXX. ����һ���ǳ���Ҫע��ĵط�������ط������˴����ĵط�
 * ��Ҫ�޸ĵĵط��� Ҳ��Ŀǰ�����εĵط������
 * ��������һЩ���ε���������� ����
 * $stringUtil.equals(&quot;$text&quot;�� &quot;&amp;&quot;)
 * ����������ǣ� &quot;$text&quot;, ����Ѿ�����htlml encode, Ȼ�� &quot;&amp;&quot;�ǳ����� ����
 * ���޷���ת��ģ����������Ƿ������ص�bug.
 * ����İ취����ôд��
 * $stringUtil.equals($text�� &quot;&amp;&quot;)
 * c.$stringUtil.getXXX(&quot;the pro: $text&quot;)
 * �������� Ŀǰ���ǵ�ϵͳҲ�ǱȽ϶࣬ ����������Ļ��. ����������İ�
 * ���ǣ�HTML_XSS_FILTER
 * $stringUtil.getXXX(&quot;the pro: #ZLITERAL($text)&quot;)
 * 
 * d.$control.setTemplate($text)
 * ���ϵͳ���Զ�����
 * e.$stringUtil.escapeHtml($text)
 * �������ĵ�6�������˽���
 * f.$screen_placeholder
 * ���ϵͳ���Զ�����
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
                    // ��Ĭ�ϵ�����£�����־��¼����������html�����
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
 * HTML_XSS_FILTER: ʹ��xss���˹������
 * HTML_ENCODE: ��html encode���
 * JAVASCRIPT_ENCODE: ��js encode ���
 * XML_ENCODE: ��xml encode���
 * LITERAL: ��ԭ�������
 * URL_FILTER: ��ʱû�����κα�����Ϊ 
 * PURE_TEXT_HTML_ENCODE: ����ԭ����ϵͳʹ�ã� Ϊ�˱��ⷢ�����α���
 * NOT_DEFINED: δ����ģ� ʹ��html encode
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
 * ����html/js/xml ����ʵ��
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
     * ���˼������ٶȵĴ���, �ںܶ�����£�������ߣ��������ϵ����ܣ����Լ���Լ�����ڴ�ʹ�á�
     * 1.���ؽ������ʱ�����˱�����иĽ��������û�з������룬��ôֱ�ӷ���ԭ����str.
     * 2.���ټ���Ƿ�Ҫ����ת��
     * 3.ʹ�ÿ��ٲ������0-255���ַ����п��ٲ���
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
     * ����һ����Խ�ַ�
     * 
     * @param c
     */
    public void addTransferChar(char c) {
        maskSet.set(c);
    }
}