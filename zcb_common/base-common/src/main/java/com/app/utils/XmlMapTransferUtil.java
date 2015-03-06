package com.app.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlMapTransferUtil {
	private static final Log LOG = LogFactory.getLog(XmlMapTransferUtil.class);
	private static char LevelChar = '_';

	public static Map<String, String> xmlToMap(String xml) {
		return xmlToMap(xml, LevelChar);
	}

	public static Map<String, String> xmlToMap(String xml, char levelChar) {
		String charset = "UTF-8";
		// 从xml头取字符编码
		int beginIndex = xml.indexOf("<?");
		if (beginIndex >= 0) {
			beginIndex = xml.indexOf("encoding", beginIndex + 2);
			if (beginIndex >= 0) {
				beginIndex = xml.indexOf('=', beginIndex + 8);
				if (beginIndex >= 0) {
					beginIndex++;
					int endIndex = xml.indexOf("?>", beginIndex);
					if (endIndex >= 0) {
						charset = xml.substring(beginIndex, endIndex).replace(
								'"', ' ').trim();
					}
				}
			}
		}
		SAXBuilder builder = new SAXBuilder();

		org.jdom.Document doc = null;
		try {
			doc = builder.build(new InputStreamReader(new ByteArrayInputStream(xml.getBytes()), charset));
			org.jdom.Element root = doc.getRootElement();
			List<org.jdom.Element> ls = root.getChildren();// 注意此处取出的是root节点下面的一层的Element集合
			Map<String, String> resultMap = new HashMap<String, String>();
			if (ls == null || ls.size() == 0)
				return null;
			recursiveDealDocument(ls, resultMap, null, levelChar);
			return resultMap;
		} catch (Exception e) {
			LOG.warn("xmlToMap ex:", e);
			e.printStackTrace();
			return null;
		}
	}

	private static void recursiveDealDocument(List<org.jdom.Element> ls,
			Map<String, String> resultMap, String parentName, char levelChar) {
		int count = 0;
		for (Iterator iter = ls.iterator(); iter.hasNext();) {
			org.jdom.Element el = (org.jdom.Element) iter.next();
			String name = el.getName();
			if (name.equalsIgnoreCase("item")) {
				name = String.valueOf(count);
				count++;
			}
			if (parentName != null)
				name = parentName + levelChar + name;
			List<org.jdom.Element> subLs = el.getChildren();
			if (subLs != null && subLs.size() > 0)
				recursiveDealDocument(subLs, resultMap, name, levelChar);
			else
				resultMap.put(name, el.getValue());
		}
	}
	
	
	public static Element format(Object obj, String name, boolean clazz,
			String charset,boolean upperFirst) {
		if (obj == null) {
			return null;
		}
		String listItemName = "item", mapKeyName = "key", mapValueName = "value";	
		if(upperFirst){
			name = name.replaceFirst(name.substring(0, 1),name.substring(0, 1).toUpperCase()); 
		}

		Element element = new Element(name);
		if (obj instanceof Number) {
			element.setText(obj.toString());
			return element;
		}		
		if (obj instanceof CharSequence
				|| obj instanceof Character || obj instanceof Boolean) {
			element.setContent(new CDATA(obj.toString()));
			return element;
		}
		if (obj instanceof java.util.Date) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			element.setContent(new CDATA(sf.format((Date) obj)));
			return element;
		}
		if (obj.getClass().isArray()) {
			int size = Array.getLength(obj);
			for (int index = 0; index < size; index++) {
				Object item = Array.get(obj, index);
				Element tmp = format(item, listItemName, clazz, charset, true);
				if (tmp != null) 
					element.addContent(tmp);
			}
			return decorateElement(element, obj.getClass().getName(), clazz);
		}
		if (obj instanceof java.util.Collection) {
			Iterator iter = ((java.util.Collection) obj).iterator();
			while (iter.hasNext()) {
				Object item = (Object) iter.next();
				Element tmp = format(item, listItemName, clazz, charset, true);
				if (tmp != null) 
					element.addContent(tmp);
			}
			return decorateElement(element, obj.getClass().getName(), clazz);
		}
		if (obj instanceof java.util.Map) {
			Iterator iter = ((java.util.Map) obj).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry item = (Map.Entry) iter.next();
				Object key = item.getKey();
				if (key instanceof CharSequence || key instanceof Number
						|| key instanceof Character || key instanceof Boolean
						|| key instanceof java.util.Date) {
					Element tmp = format(item.getValue(), item.getKey()
							.toString(), clazz, charset, true);
					if (tmp != null) 
						element.addContent(tmp);
				} else {
					Element entry = new Element("entry");
					Element tmp = format(item.getKey(), mapKeyName, clazz, charset, true);
					if (tmp != null) {
						entry.addContent(tmp);
						tmp = format(item.getValue(), mapValueName, clazz,
								charset, true);
						if (tmp != null) {
							entry.addContent(tmp);
							element.addContent(entry);
						}
					}
				}
			}
			return decorateElement(element, obj.getClass().getName(), clazz);
		}

		java.beans.PropertyDescriptor pd[] = PropertyUtils
				.getPropertyDescriptors(obj);
		for (int i = 0; i < pd.length; i++) {
			if (pd[i].getReadMethod() != null && pd[i].getWriteMethod() != null) {
				try {
					Object value = PropertyUtils.getProperty(obj,
							pd[i].getName());
					Element tmp = format(value, pd[i].getName(), clazz, charset, true);
					if (tmp != null)
						element.addContent(tmp);
				} catch (Exception ex) {
				}
			}
		}
		return decorateElement(element, obj.getClass().getName(), clazz);
	}
	
	/**
	 * @param element
	 * @param className
	 * @param clazz
	 * @return
	 */
	public static Element decorateElement(Element element, String className, boolean clazz) {
		if (clazz) {
			element.setAttribute("class", className);
		}
		if ((element.getText() == null || "".equals(element.getText())) 
				&& (element.getChildren() == null || element.getChildren().size() == 0))
			return null;
		return element;
	}

}
