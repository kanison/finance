package com.tenpay.sm.context;

import java.util.Map;

import com.tenpay.sm.context.conversation.Conversation;




/**
 * <p>Title: </p>
 * <p>Description: xx���
 * ��ȡ���������������ģ����Ի��
 * Current < Conversation < Session < Global Context ������
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ������</p>
 * @author ������
 * @version 2.0
 */
public interface Context {

	
  abstract public Conversation getConversation() ;

  abstract public Object getCurrentAttribute(String key) ;
  abstract public void removeCurrentAttribute(String key) ;
  abstract public void setCurrentAttribute(String key,Object value) ;
  abstract Map<String,Object> getCurrentAttributesMap();
  
  abstract Map<String,String[]> getRequestParamsMap();
  abstract String getRequestParameter(String name);
  abstract String[] getRequestParameterValues(String name);
  
  abstract public Object getSessionAttribute(String key);
  abstract public void removeSessionAttribute(String key);
  abstract public void setSessionAttribute(String key,Object value);
  
  abstract public Object getApplicationAttribute(String key);
  abstract public void setApplicationAttribute(String key,Object value);
  abstract public void removeApplicationAttribute(String key);

}
