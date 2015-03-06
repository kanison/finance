package com.app.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XmlParseUtil {
	private static final Log LOG = LogFactory.getLog(XmlParseUtil.class);

	@SuppressWarnings("unchecked")
	public static List<String> selectXpathValue(String xml, String xpath) {
		Document doc = null;
		List<String> retList = new LinkedList<String>();
		try {
			doc = DocumentHelper.parseText(xml);
			List<Element> nodeList = doc.selectNodes(xpath);
			for (Element e : nodeList) {
				retList.add(e.getStringValue());
			}
			return retList;
		} catch (Exception e) {
			return retList;
		}
	}

	/**
	 * 解析字符串
	 * 
	 * @param xmlStr
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static org.jdom.Document parse(String xmlStr, String charsetName)
			throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder();
		xmlStr = new String(xmlStr.getBytes(charsetName), charsetName);
		return sb.build(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
	}

	public static org.jdom.Document parse(String xmlStr, String stringCharset,
			String xmlCharset) throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder();
		xmlStr = new String(xmlStr.getBytes(stringCharset), stringCharset);
		return sb.build(new ByteArrayInputStream(xmlStr.getBytes(xmlCharset)));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(BufferedReader br) {
		SAXBuilder builder = new SAXBuilder();

		org.jdom.Document doc = null;
		try {

			doc = builder.build(br);

			org.jdom.Element root = doc.getRootElement();

			List<org.jdom.Element> ls = root.getChildren();// 注意此处取出的是root节点下面的一层的Element集合
			Map<String, String> resultMap = new HashMap<String, String>();

			for (Iterator iter = ls.iterator(); iter.hasNext();) {

				org.jdom.Element el = (org.jdom.Element) iter.next();
				resultMap.put(el.getName(), el.getValue());
			}
			return resultMap;
		} catch (Exception e) {
			LOG.warn("xmlToMap ex:", e);
			return null;
		}
	}
}
