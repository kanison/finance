/**
 * 
 */
package com.tenpay.sm.client.facade;

import java.util.Map;

import com.tenpay.sm.client.resource.RestResource;
import com.tenpay.sm.client.resource.TagResource;


/**
 * @author li.hongtl
 * ����mashup�������Ľӿ�
 */
public interface MashupServerFacade {
	String DEFAULT_CONTENT_KEY = "resource";
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * ��ʱ�����ã�������xfire��bug
	 * @param content ����
	 * @return ���ص�����id
	 */
	@Deprecated 
	String registerContentMap(Map content);
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * @param content ����
	 * @return ���ص�����id
	 */
	String registerStringMap(Map<String,String> content);
	
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * ��ʱ�����ã�������xfire��bug
	 * @param content content ����
	 * @return ���ص�����id
	 */
	@Deprecated 
	String registerRestResourceMap(Map<String,RestResource> content);
	
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * ��ʱ�����ã�������xfire��bug
	 * @param content content ����
	 * @return ���ص�����id
	 */
	@Deprecated 
	String registerTagResourceMap(Map<String,TagResource> content);
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * @param key ���ݵ�key
	 * @param content content ����
	 * @return ���ص�����id
	 */
	String registerString(String key,String content);
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * @param key ���ݵ�key
	 * @param content content ����
	 * @return ���ص�����id
	 */
	String registerRestResource(String key,RestResource content);
	/**
	 * ��mashup������ע�����ݵĽӿ�
	 * @param key ���ݵ�key
	 * @param content content ����
	 * @return ���ص�����id
	 */
	String registerTagResource(String key,TagResource content);	
}
