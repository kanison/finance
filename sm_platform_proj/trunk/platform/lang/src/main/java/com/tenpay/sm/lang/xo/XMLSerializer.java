package com.tenpay.sm.lang.xo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Java Bean和XML之间互相转换的工具
 * 支持Map，Collection，Array等组合的bean，不需要任何Annotation 使用jdom实现 Java Bean --> xml
 * 经过比较多的测试 xml --> 到jaba bean没经过严格的测试 性能没经过严格的测试 考虑用jaxb或其它方式代替
 * 
 * 用GBK编码
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class XMLSerializer {
	public static Document serialize(Object obj, String root, String charset) {
		return serialize(obj, root, true, charset);
	}

	public static Document serialize(Object obj, String root, boolean clazz,
			String charset) {
		return new Document(format(obj, root, clazz, charset));
	}

	public static Element format(Object obj, String name, String charset) {
		return format(obj, name, true, charset);
	}

	public static void serialize(Object obj, java.io.OutputStream os,
			String root, boolean clazz, String charset) throws IOException {
		Document doc = new Document(format(obj, root, clazz, charset));
		org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(
				Format.getPrettyFormat().setEncoding(charset));
		out.output(doc, os);
	}

	public static void serialize(Object obj, java.io.PrintWriter pw,
			String root, boolean clazz, String charset) throws IOException {
		Document doc = new Document(format(obj, root, clazz, charset));
		org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(
				Format.getPrettyFormat().setEncoding(charset));
		out.output(doc, pw);
	}

	/**
	 * 反“XML序列化”得到一个对象
	 * 
	 * @param is
	 *            InputStream 输入流
	 * @param clazz
	 *            Class 类型
	 * @return Object 得到的对象
	 * @throws IOException
	 *             输入输出异常
	 * @throws JDOMException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialize(java.io.InputStream is, Class clazz)
			throws IOException, JDOMException, InvocationTargetException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		SAXBuilder b = new SAXBuilder();
		Document doc = b.build(is);
		return parse(doc.getRootElement(), clazz);
	}

	public static Element format(Object obj, String name, boolean clazz,
			String charset) {
		Element element = new Element(name);
		if (obj == null) {
			return element;
		}
		if (obj instanceof CharSequence || obj instanceof Number
				|| obj instanceof Character || obj instanceof Boolean) {
			element.setText(obj.toString());
			return element;
		}
		if (obj instanceof java.util.Date) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			element.setText(sf.format((Date) obj));
			return element;
		}
		if (obj.getClass().isArray()) {
			if (clazz) {
				// element.setAttribute("class", obj.getClass().getName());
				element.setAttribute("class", obj.getClass().getName());
			}
			int size = Array.getLength(obj);
			for (int index = 0; index < size; index++) {
				Object item = Array.get(obj, index);
				element.addContent(format(item, "item", clazz, charset));
			}
			return element;
		}
		if (obj instanceof java.util.Collection) {
			if (clazz) {
				// element.setAttribute("class", obj.getClass().getName());
				element.setAttribute("class", obj.getClass().getName());
			}
			Iterator iter = ((java.util.Collection) obj).iterator();
			while (iter.hasNext()) {
				Object item = (Object) iter.next();
				element.addContent(format(item, "item", clazz, charset));
			}
			return element;
		}
		if (obj instanceof java.util.Map) {
			if (clazz) {
				// element.setAttribute("class", obj.getClass().getName());
				element.setAttribute("class", obj.getClass().getName());
			}
			// element.sddAttribute("map",Boolean.TRUE.toString());
			Iterator iter = ((java.util.Map) obj).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry item = (Map.Entry) iter.next();
				Object key = item.getKey();
				if (key instanceof CharSequence || key instanceof Number
						|| key instanceof Character || key instanceof Boolean
						|| key instanceof java.util.Date) {
					// element.addContent(
					// format(item.getValue(), "entry", clazz)
					// .setAttribute("key", item.getKey().toString())
					element.addContent(format(item.getValue(), item.getKey()
							.toString(), clazz, charset));
				} else {
					element.addContent(new Element("entry").addContent(
							format(item.getKey(), "key", clazz, charset))
							.addContent(
									format(item.getValue(), "value", clazz,
											charset)));
				}
			}
			return element;
		}

		java.beans.PropertyDescriptor pd[] = PropertyUtils
				.getPropertyDescriptors(obj);
		for (int i = 0; i < pd.length; i++) {
			if (pd[i].getReadMethod() != null && pd[i].getWriteMethod() != null) {
				try {
					Object value = PropertyUtils.getProperty(obj, pd[i]
							.getName());
					element.addContent(format(value, pd[i].getName(), clazz,
							charset));
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}
		if (clazz) {
			// element.setAttribute("class", obj.getClass().getName());
			element.setAttribute("class", obj.getClass().getName());
		}
		return element;
	}

	/**
	 * 第一版之后没有经过测试。。。
	 * 
	 * @param element
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private static Object parse(Element element, Class clazz)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		String className = element.getAttributeValue("class");
		if (className != null) {
			clazz = Class.forName(className);
		}
		Object obj = null;
		if (clazz == null || clazz.equals(String.class)
				|| clazz.equals(int.class) || clazz.equals(Integer.class)
				|| clazz.equals(boolean.class) || clazz.equals(Boolean.class)
				|| clazz.equals(byte.class) || clazz.equals(Byte.class)
				|| clazz.equals(char.class) || clazz.equals(Character.class)
				|| clazz.equals(short.class) || clazz.equals(Short.class)
				|| clazz.equals(long.class) || clazz.equals(Long.class)
				|| clazz.equals(float.class) || clazz.equals(Float.class)
				|| clazz.equals(double.class) || clazz.equals(Double.class)) {
			obj = String.valueOf(element.getText());
			return obj;
		}
		if (clazz.equals(BigDecimal.class)) {
			String value = element.getText();
			if (value != null)
				value = value.trim();
			if (value == null || value.length() == 0
					|| value.equalsIgnoreCase("NULL"))
				return BigDecimal.ZERO;
			else
				return new BigDecimal(element.getText());
		}
		if (clazz.equals(Date.class)) {
			String value = element.getText();
			if (value != null)
				value = value.trim();
			if (value == null || value.length() == 0
					|| value.equalsIgnoreCase("NULL"))
				return null;
			else
				try {
					if (value.length() == 19) {
						SimpleDateFormat sf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						return sf.parse(value);
					} else {
						SimpleDateFormat sf = new SimpleDateFormat(
								"E MMM dd HH:mm:ss z yyyy", Locale.US);
						return sf.parse(value);
					}
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
		}
		obj = clazz.newInstance();
		if (obj instanceof java.util.Collection) {
			java.util.Collection<Object> collection = (java.util.Collection) obj;
			Iterator iter = element.getChildren().iterator();
			while (iter.hasNext()) {
				Element item = (Element) iter.next();
				collection.add(parse(item, String.class));
			}
			return collection;
		}

		Iterator iter = element.getChildren().iterator();
		while (iter.hasNext()) {
			Element item = (Element) iter.next();
			Class propertyClass = PropertyUtils.getPropertyType(obj, item
					.getName());
			Object o = parse(item, propertyClass);
			if (o != null)
				BeanUtils.setProperty(obj, item.getName(), o);
		}
		return obj;
	}

}
