/**
 * 
 */
package com.tenpay.sm.test.tag;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.tenpay.sm.client.resource.TagResource;
import com.tenpay.sm.client.resource.tag.TagToXml;
import com.tenpay.sm.client.resource.tag.XmlToTag;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class Xml2Tag2Xml extends TestCase {
	public void testXml2Tag() throws JDOMException, IOException {
		
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(Xml2Tag2Xml.class.getClassLoader()
				.getResourceAsStream("mashupTag.xml"));
		TagResource tr = XmlToTag.ContentToTag(doc.getRootElement());
		
		String str = ToStringBuilder.reflectionToString(tr,ToStringStyle.DEFAULT_STYLE);
		System.out.println(str);
		
		String strMy = TagToXml.tagToXMLString(tr, true);
		System.out.println(strMy);
		
	}
}
