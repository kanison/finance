/**
 * 
 */
package com.tenpay.sm.web.mashupserver;

import java.util.HashMap;
import java.util.Map;

import com.tenpay.sm.cache.Cache;
import com.tenpay.sm.client.facade.MashupServerFacade;
import com.tenpay.sm.client.facade.WebContentFacade;
import com.tenpay.sm.client.resource.RestResource;
import com.tenpay.sm.client.resource.TagResource;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.mashup.MashupContentGetFacade;
import com.tenpay.sm.web.mashup.TagResourceEngine;

/**
 * @author li.hongtl
 *
 */
public class MashupServerFacadeImpl implements MashupServerFacade ,MashupContentGetFacade {
	private WebContentFacade webContentFacadeClientImplInServer;
	private TagResourceEngine tagResourceEngine;
	private Cache cache;
	/* (non-Javadoc)
	 * @see com.tenpay.sm.client.facade.MashupServerFacade#registerString(java.util.Map)
	 */
	public String registerStringMap(Map<String, String> content) {
		return registerContentMap(content);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.client.facade.MashupServerFacade#registerRestResource(java.util.Map)
	 */
	public String registerRestResourceMap(Map<String, RestResource> content) {
		return registerContentMap(content);
	}
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.client.facade.MashupServerFacade#registerTagResource(java.util.Map)
	 */	
	public String registerTagResourceMap(Map<String,TagResource> content) {
		return registerContentMap(content);
	}
	
	/* (non-Javadoc)
	 * @see com.tenpay.sm.client.facade.MashupServerFacade#registerContentMap(java.util.Map)
	 */	
	public String registerContentMap(Map content) {
		if(content==null) {
			return null;
		}
		String id = java.util.UUID.randomUUID().toString();
		this.cache.put(id, content);
		return id;
	}

	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	public String registerContent(String key,Object content) {
		if(key==null) {
			key = MashupServerFacade.DEFAULT_CONTENT_KEY;
		}
		Map map = new HashMap();
		map.put(key, content);
		return registerContentMap(map);
	}
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	public String registerString(String key,String content) {
		return registerContent(key,content);
	}
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	public String registerRestResource(String key,RestResource content) {
		return registerContent(key,content);
	}
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	public String registerTagResource(String key,TagResource content) {
		return registerContent(key,content);
	}
	
	public Map<String, Object> getContent(String id) {
		Map<String, Object> content = (Map<String, Object>) this.cache.get(id);
		if(content==null) {
			content = new HashMap<String, Object>();
		}
		return content;
	}

	public Map<String, Object> getDefaultContent() {
		WebModuleContext wc = (WebModuleContext) ContextUtil.getContext();
		String id = wc.getRequest().getParameter(MashupContentGetFacade.DEFAULT_PARAM_KEY);
		return this.getContent(id);
	}
	
	public String resolveContent(Object content) {
		if(content==null) {
			return null;
		}
		else if(content instanceof RestResource) {
			RestResource rest = (RestResource)content;
			//TODO
			String result = webContentFacadeClientImplInServer.httpGetWebContent(rest.getUri(), null);
			return result;
		}
		else if(content instanceof TagResource) {
			String result = this.tagResourceEngine.resolve((TagResource)content,null);
			return result;
		}
		else {
			return content.toString();
		}
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @param tagResourceEngine the tagResourceEngine to set
	 */
	public void setTagResourceEngine(TagResourceEngine tagResourceEngine) {
		this.tagResourceEngine = tagResourceEngine;
	}

	/**
	 * @param webContentFacadeClientImplInServer the webContentFacadeClientImplInServer to set
	 */
	public void setWebContentFacadeClientImplInServer(
			WebContentFacade webContentFacadeClientImplInServer) {
		this.webContentFacadeClientImplInServer = webContentFacadeClientImplInServer;
	}

	
}
