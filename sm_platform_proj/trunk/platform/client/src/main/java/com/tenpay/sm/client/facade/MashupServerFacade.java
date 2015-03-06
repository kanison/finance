/**
 * 
 */
package com.tenpay.sm.client.facade;

import java.util.Map;

import com.tenpay.sm.client.resource.RestResource;
import com.tenpay.sm.client.resource.TagResource;


/**
 * @author li.hongtl
 * 访问mashup服务器的接口
 */
public interface MashupServerFacade {
	String DEFAULT_CONTENT_KEY = "resource";
	/**
	 * 向mashup服务器注册内容的接口
	 * 暂时不能用，可能是xfire的bug
	 * @param content 内容
	 * @return 返回的内容id
	 */
	@Deprecated 
	String registerContentMap(Map content);
	/**
	 * 向mashup服务器注册内容的接口
	 * @param content 内容
	 * @return 返回的内容id
	 */
	String registerStringMap(Map<String,String> content);
	
	/**
	 * 向mashup服务器注册内容的接口
	 * 暂时不能用，可能是xfire的bug
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	@Deprecated 
	String registerRestResourceMap(Map<String,RestResource> content);
	
	/**
	 * 向mashup服务器注册内容的接口
	 * 暂时不能用，可能是xfire的bug
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	@Deprecated 
	String registerTagResourceMap(Map<String,TagResource> content);
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	String registerString(String key,String content);
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	String registerRestResource(String key,RestResource content);
	/**
	 * 向mashup服务器注册内容的接口
	 * @param key 内容的key
	 * @param content content 内容
	 * @return 返回的内容id
	 */
	String registerTagResource(String key,TagResource content);	
}
