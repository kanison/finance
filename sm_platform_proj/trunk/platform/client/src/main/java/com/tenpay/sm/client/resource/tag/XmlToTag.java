/**
 * 
 */
package com.tenpay.sm.client.resource.tag;

import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;

import com.tenpay.sm.client.resource.TagResource;

/**
 * @author li.hongtl
 *
 */
public class XmlToTag {
	public static TagResource ContentToTag(Content content) {
		if(content==null) {
			return null;
		}
		TagResource tagResource = new TagResource();
		if(content instanceof Element) {
			Element element = (Element)content;
			tagResource.setTagName(element.getName());
			tagResource.setTagPrefix(element.getNamespacePrefix());
			for(Object obj : element.getAttributes()) {
				Attribute attr = (Attribute)obj;
				tagResource.getAttributes().put(attr.getName(), attr.getValue());
			}
			for(Object obj : element.getContent()) {
				TagResource tr = ContentToTag((Content)obj);
				tagResource.getSubContent().add(tr);
			}
		}
		else {
			tagResource.setExpressionContent(content.getValue());
		}
		return tagResource;
	}
}
