/**
 * 
 */
package com.tenpay.sm.web.conversation;

import org.springframework.util.StringUtils;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.context.conversation.ClientConversationImpl;
import com.tenpay.sm.lang.util.MessageDigestUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.plugin.SmPlugin;

/**
 * 便于最后统一生成Conversation所需要的html元素的plugin
 * @author li.hongtl
 *
 */
public class ConversationPlugin implements SmPlugin {
	public static final ConversationPlugin INSTANCE = new ConversationPlugin();

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.tag.SmPlugin#generateContent()
	 * 生成3个隐藏域
	 */
	public String generateContent() {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		ClientConversationImpl vs = (ClientConversationImpl)wc.getCurrentAttribute(
				WebModuleContext.CURRENT_VIEWSTATE_KEY);
		Object cid = wc.getCurrentAttribute(WebModuleContext.CURRENT_CONVERSATION_ID_KEY);
		String vsString = vs==null?"":vs.toBase64String();
		cid = cid==null?"":cid;
		String signature = "";
		if(StringUtils.hasText(vsString)) {
			//signature = MessageDigestUtil.digest(vsString+wc.getSessionRandomSecurityCode());
			signature = MessageDigestUtil.digest(vsString+WebModuleContext.DEFAULT_SECURITY_CODE);
		}
		
		String str = new java.util.Formatter().format(
				"<input type='hidden' name='%s' value='%s'/><input type='hidden' name='%s' value='%s'/><input type='hidden' name='%s' value='%s'/><input type='hidden' name='%s' value='%s'/>",
				WebModuleContext.PARAM_TREE_NODE_LOCATION_ID , wc.getTreeNodeLocationId(),
				WebModuleContext.PARAM_VIEWSTATE_KEY + wc.getTreeNodeLocationId(),vsString,
				WebModuleContext.PARAM_CONVERSATION_ID_KEY + wc.getTreeNodeLocationId(),cid,
				WebModuleContext.PARAM_VIEWSTATE_SIGNATURE_KEY + wc.getTreeNodeLocationId(),signature).toString();
		return str;
	}

}
