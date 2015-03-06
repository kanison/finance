/**
 * 
 */
package com.tenpay.sm.client.resource.tag;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.tenpay.sm.client.resource.TagResource;

/**
 * @author li.hongtl
 *
 */
public class TagToXml {
	public static Content tagToXMLContent(TagResource tagResource) {
		if(tagResource==null) {
			return null;
		}
		if(tagResource.getExpressionContent()!=null) {
			return new Text(tagResource.getExpressionContent());
		}
		else {
			String prefixUri = "http://www.tenpay.com/sm";
			if(tagResource.getTagPrefix()==null || "".equals(tagResource.getTagPrefix())) {
				prefixUri = null;
			}
			Element element = new Element(tagResource.getTagName(),tagResource.getTagPrefix(),prefixUri);
			if(tagResource.getAttributes()!=null) {
				for(String key : tagResource.getAttributes().keySet()) {
					String value = tagResource.getAttributes().get(key);
					element.setAttribute(key,value);
				}
			}
			if(tagResource.getSubContent()!=null) {
				for(TagResource subTagResource : tagResource.getSubContent()) {
					Content subCintent = tagToXMLContent(subTagResource);
					if(subCintent!=null) {
						element.addContent(subCintent);
					}
				}
			}
			return element;
		}
	}
	
	public static Element tagToXMLElement(TagResource tagResource) {
		return (Element)tagToXMLContent(tagResource);
	}
	
	public static Document tagToXMLDocument(TagResource tagResource) {
		Element root = tagToXMLElement(tagResource);
		Document doc = new Document(root);
		return doc;
	}
	

	public static String tagToXMLString(TagResource tagResource,boolean isDocument) {
		if(isDocument) {
			XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			Document doc = tagToXMLDocument(tagResource);
			return output.outputString(doc);
		}
		else {
			Content content = tagToXMLContent(tagResource);
			if(content==null || !(content instanceof Element)) {
				return tagResource.getExpressionContent();
			}
			else {
				XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
				return output.outputString((Element)content);
			}
		}
	}
}
