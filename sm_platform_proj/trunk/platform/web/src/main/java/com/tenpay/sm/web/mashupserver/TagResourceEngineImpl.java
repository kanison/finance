/**
 * 
 */
package com.tenpay.sm.web.mashupserver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.support.OgnlExpressionParser;

import com.tenpay.sm.client.resource.TagResource;
import com.tenpay.sm.web.mashup.TagResourceContext;
import com.tenpay.sm.web.mashup.TagResourceEngine;
import com.tenpay.sm.web.mashup.TagResourceException;
import com.tenpay.sm.web.mashup.TagResourceHandler;
import com.tenpay.sm.web.mashup.TagResourceHandlerResultEnum;
import com.tenpay.sm.web.mashup.el.ElExpressionParser;

/**
 * @author li.hongtl
 * 
 * <![CDATA[
<?xml version="1.0" encoding="GBK"?>
<sm:ifLogin xmlns:sm="http://www.tenpay.com/sm">
	<sm:for times="3">
		你的用户名：<sm:userName/>，
		你的余额：<sm:balance/>元。
		你的用户名：${user.userName}，
		你的余额：${user.balance}元。	
		<sm:if test="${user.balance>300}">
			<h2 color="red">你好有钱啊</h2>
		</sm:if>	
	</sm:for>
	<script language="javascript">
		alert('脚本可以执行吗？');
		window.opener = null;
		window.close();
	</script>
</sm:ifLogin>
]]>
 * 
 */
public class TagResourceEngineImpl implements TagResourceEngine {
	//private ExpressionParser expressionParser = new OgnlExpressionParser();
	private ExpressionParser expressionParser = new ElExpressionParser();
	private EvaluationContext expressionEvaluationContext;
	
	private Map<String,TagResourceHandler> handlers = new HashMap<String,TagResourceHandler>();
	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.mashup.TagResourceEngine#resolve(com.tenpay.sm.client.resource.TagResource)
	 */
	public String resolve(TagResource tagResource,Object expressionTarget) throws TagResourceException {
		if(tagResource==null) {
			return null;
		}
		TagResourceContext tagResourceContext = new TagResourceContext();
		tagResourceContext.setExpressionTarget(expressionTarget);
		if(tagResource.getExpressionContent()!=null) {
			return this.handlerExpressionContent(tagResource.getExpressionContent(),tagResourceContext);
		}
		else {			
			this.resolveInternal(tagResource,tagResourceContext);
			return tagResourceContext.getResultContent();
		}
	}
	
	protected void resolveInternal(TagResource tagResource, TagResourceContext tagResourceContext) throws TagResourceException {
		if(tagResource==null) {
			return;
		}
		if(tagResource.getExpressionContent()!=null) {
			String strContent = this.handlerExpressionContent(tagResource.getExpressionContent(),tagResourceContext);
			if(strContent!=null) {
				tagResourceContext.appendResultContent(strContent);
			}
		}
		else {
			String tagFullName =  tagResource.getTagName();
			if(tagResource.getTagPrefix()!=null && !"".equals(tagResource.getTagPrefix())) {
				tagFullName = tagResource.getTagPrefix() + ":" + tagResource.getTagName();
			}
			TagResourceHandler tagResourceHandler = (TagResourceHandler)handlers.get(tagFullName);
			TagResourceHandlerResultEnum result = TagResourceHandlerResultEnum.CONTINUE;
			
			if(tagResourceHandler==null) {
				tagResourceContext.appendResultContent("<" + tagFullName);
				if(tagResource.getAttributes()!=null) {
					for(String attrName : tagResource.getAttributes().keySet()) {
						String value = tagResource.getAttributes().get(attrName);
						if(value!=null) {
							tagResourceContext.appendResultContent(" " + attrName + "=\"" + value + "\"");
						}
					}
				}
				tagResourceContext.appendResultContent(">");
			}
			else {
				result = tagResourceHandler.doStartTag(tagResource,tagResourceContext);
			}
			
			if(TagResourceHandlerResultEnum.SKIP!=result) {
				result = this.resolveInternalTagResourceBody(tagResourceHandler, tagResource, tagResourceContext);
				while(TagResourceHandlerResultEnum.LOOP==result) {
					result = this.resolveInternalTagResourceBody(tagResourceHandler, tagResource, tagResourceContext);
				}
			}
			
			if(tagResourceHandler==null) {
				tagResourceContext.appendResultContent("</" + tagFullName + ">");
			}
			else {
				tagResourceHandler.doEndTag(tagResource,tagResourceContext);
			}
			return;
		}
	}
	
	protected TagResourceHandlerResultEnum resolveInternalTagResourceBody(TagResourceHandler tagResourceHandler, TagResource tagResource, TagResourceContext tagResourceContext) throws TagResourceException {
		if(tagResource.getSubContent()!=null) {
			for(TagResource subTagResource : tagResource.getSubContent()) {
				this.resolveInternal(subTagResource,tagResourceContext);
			}
		}
		TagResourceHandlerResultEnum result = TagResourceHandlerResultEnum.CONTINUE;
		if(tagResourceHandler!=null) {
			result = tagResourceHandler.doAfterBody(tagResource,tagResourceContext);
		}
		return result;
	}
	
	protected String handlerExpressionContent(String expressionString,TagResourceContext tagResourceContext) throws TagResourceException {
		if(expressionString==null || "".equals(expressionString)) {
			return null;
		}
		Expression exp = expressionParser.parseExpression(expressionString);
		Object result = exp.evaluate(tagResourceContext.getExpressionTarget(), this.expressionEvaluationContext);
		if(result==null) {
			return null;
		}
		return result.toString();
	}
	/**
	 * @return the handlers
	 */
	public Map<String, TagResourceHandler> getHandlers() {
		return handlers;
	}
	/**
	 * @param handlers the handlers to set
	 */
	public void setHandlers(Map<String, TagResourceHandler> handlers) {
		this.handlers = handlers;
	}

	/**
	 * @param expressionEvaluationContext the expressionEvaluationContext to set
	 */
	public void setExpressionEvaluationContext(
			EvaluationContext expressionEvaluationContext) {
		this.expressionEvaluationContext = expressionEvaluationContext;
	}

	/**
	 * @param expressionParser the expressionParser to set
	 */
	public void setExpressionParser(ExpressionParser expressionParser) {
		this.expressionParser = expressionParser;
	}
	

}
