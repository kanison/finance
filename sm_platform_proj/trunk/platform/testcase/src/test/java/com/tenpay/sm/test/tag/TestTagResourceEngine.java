/**
 * 
 */
package com.tenpay.sm.test.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.tenpay.sm.client.resource.TagResource;
import com.tenpay.sm.client.resource.tag.XmlToTag;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.test.base.ContextSupportTestCase;
import com.tenpay.sm.web.mashup.TagResourceEngine;


/**
 * @author li.hongtl
 *
 */
public class TestTagResourceEngine extends ContextSupportTestCase {
	TagResourceEngine tagResourceEngine;
	
	public void testTagResourceEngine() throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(TestTagResourceEngine.class.getClassLoader()
				.getResourceAsStream("mashupTag.xml"));
		TagResource tagResource = XmlToTag.ContentToTag(doc.getRootElement());
		
		ContextUtil.getContext().setSessionAttribute("loginName", "ÕÅÈý");
		
		User user = new User("tom jerry",new Integer(28));
		Map map = new HashMap();
		map.put("user", user);
		
		String str = tagResourceEngine.resolve(tagResource,map);
		System.out.println(str);
		
		user.setBalance(420);
		str = tagResourceEngine.resolve(tagResource,map);
		System.out.println(str);
		
	}
	
	/**
	 * @param tagResourceEngine the tagResourceEngine to set
	 */
	public void setTagResourceEngine(TagResourceEngine tagResourceEngine) {
		this.tagResourceEngine = tagResourceEngine;
	}
	

	public class User {
		public User() {
		}
		public User(String userName, Integer balance) {
			this.userName = userName;
			this.balance = balance;
		}
		private String userName;
		private Integer balance;
		/**
		 * @return the balance
		 */
		public Integer getBalance() {
			return balance;
		}
		/**
		 * @param balance the balance to set
		 */
		public void setBalance(Integer balance) {
			this.balance = balance;
		}
		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		
	}
}
