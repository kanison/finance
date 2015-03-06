/**
 * 
 */
package com.tenpay.sm.web.resubmit;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.plugin.SmPlugin;

/**
 * 便于最后统一生成FormToken所需要的页面html元素的plugin
 * @author li.hongtl
 *
 */
public class FormTokenPlugin implements SmPlugin {
	public static final FormTokenPlugin INSTANCE = new FormTokenPlugin();
	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.tag.SmPlugin#generateContent()
	 */
	public String generateContent() {
		FormTokenCircle formTokenCircle = (FormTokenCircle)ContextUtil.getContext()
			.getSessionAttribute(FormTokenHandlerInterceptor.SESSION_FORM_TOKEN_CIRCLE_KEY);
		if(formTokenCircle==null) {
			formTokenCircle = new FormTokenCircle();
			ContextUtil.getContext().setSessionAttribute(
					FormTokenHandlerInterceptor.SESSION_FORM_TOKEN_CIRCLE_KEY,formTokenCircle);
		}
		String token = formTokenCircle.createToken();
		
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		String str = new java.util.Formatter().format(
				"<input type='hidden' name='%s' value='%s'/>",
				FormTokenHandlerInterceptor.PARAM_FORM_TOKEN + wc.getTreeNodeLocationId(),token).toString();
		return str;
	}

}
