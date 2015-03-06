/**
 * 
 */
package com.tenpay.sm.test.context;

import java.util.HashMap;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.context.conversation.ClientConversationImpl;
import com.tenpay.sm.context.conversation.Conversation;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestViewState extends TestCase {
	public void testViewState() {
		ContextUtil.setContext(null);
		ClientConversationImpl vs = (ClientConversationImpl)ContextUtil.getContext().getConversation();
		vs.setAttribute("a", "fds");
		vs.setAttribute("b", new HashMap());
		System.out.println(vs.hashCode());
		String s = vs.toBase64String();
		showReverse(s);
	}
	
	public void testEmptyViewState() {
		ContextUtil.setContext(null);
		ClientConversationImpl vs = (ClientConversationImpl)ContextUtil.getContext().getConversation();
		System.out.println(vs.hashCode());
		String s = vs.toBase64String();
		showReverse(s);
	}
	
	private void showReverse(String s) {
		System.out.println(s);
		Conversation v = ContextUtil.getContext().getConversation().init(s);
		System.out.println(v.hashCode());
		System.out.println(v.getAttributeMap().size());
		System.out.println(v.getAttribute("a"));
		System.out.println(v.getAttribute("b"));

		Conversation viewState = ContextUtil.getContext().getConversation();
		java.util.Iterator iter = viewState.getAttributeMap().keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			Object value = viewState.getAttribute(name);
			System.out.println(value);
		}
	}
}
