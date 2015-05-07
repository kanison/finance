package com.zhaocb.zcb_app.finance.fep.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * xml工具类
 * 
 * @author zhl
 *
 */
public class XMLUtil {

	/**
	 * 解析XML到SET
	 * 
	 * @param br
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Set<Map> parseXmlToSet(BufferedReader br)
			throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(br);
		Set<Map> eleSet = new HashSet<Map>();
		Element element = doc.getRootElement();
		elementLoop(element, element.getName(), new HashMap<String, String>(),
				eleSet);
		return eleSet;
	}

	/**
	 * 递归遍历xml节点
	 * 
	 * @param element
	 * @param elementName
	 * @param eleMap
	 * @param eleSet
	 */
	private static void elementLoop(Element element, String elementName,
			Map<String, String> eleMap, Set<Map> eleSet) {
		List<Element> elements = element.getChildren();
		if (elements.isEmpty()) {
			if (elementName.matches("root|result|.*_rec")) {
				eleMap.put(element.getName(), element.getValue());
			}
		} else {
			Iterator<Element> iter = elements.iterator();
			eleMap = new HashMap<String, String>();
			eleMap.put("set_name", element.getName());
			eleSet.add(eleMap);
			while (iter.hasNext()) {
				element = iter.next();
				elementLoop(element, element.getParentElement().getName(),
						eleMap, eleSet);
			}
		}
	}

}
