package com.tenpay.sm.web.context;
import java.util.HashMap;
import java.util.Map;
//import java.util.Stack;
import java.util.UUID;

import javax.servlet.http.*;
import javax.servlet.*;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.tenpay.sm.context.AbstractContext;
import com.tenpay.sm.context.conversation.Conversation;
import com.tenpay.sm.context.conversation.ConversationException;
import com.tenpay.sm.lang.util.MessageDigestUtil;
/**
 *
 * <p>Title: xx���</p>
 * <p>Description: xxt���
 * Web�����»�ȡ��������������Ե�Context
 * һ�㽨����WebModuleContextFilter����WebModuleContext
 * current < request < session < conversation < application
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ������</p>
 * @author ������
 * @version 1.0
 */
public class WebModuleContext extends AbstractContext {
	public WebModuleContext() {
	}
	/**
	 * current scope���������Ե�Map
	 * ע�� WebModuleContextFilter �����÷�ʽ����һ�������� �������ģ���ʱ��ÿ��ģ�鶼Ҫ����filter����Ҫ��һ�� WebModuleContext !
	 */
	private Map<String,Object> currentScope = new HashMap<String,Object>();
	
	/**
	 * ����ǰģ���handler
	 */
	private Object handler = null;
	
	/**
	 * �ϼ�ģ��
	 */
	private WebModuleContext parent = null;
	
	/**
	 * ��ǰģ��������ҳ���ģ�����е�λ�ñ��, ���� ��, .1 , .2 , .2.1, .2.2, .2.1.1, 
	 */
	private String treeNodeLocationId = "";
	
	/**
	 * ��ģ�������
	 */
	private int childWebModuleNumber = 0;
	
	
	/**
	 * ViewState(�ͻ���Conversation)�ڿͻ��˱��е���������
	 */
	static public final String PARAM_TREE_NODE_LOCATION_ID = WebModuleContext.class.getName() + ".TREE_NODE_LOCATION_ID";
	/**
	 * ViewState(�ͻ���Conversation)�ڿͻ��˱��е���������
	 */
	static public final String PARAM_VIEWSTATE_KEY = WebModuleContext.class.getName() + ".VIEWSTATE";
	/**
	 * ViewState(�ͻ���Conversation)��ǩ��(���ڷ��۸�)�ڿͻ��˱��е���������
	 */
	static public final String PARAM_VIEWSTATE_SIGNATURE_KEY = WebModuleContext.class.getName() + ".VIEWSTATE_SIGNATURE";
//	/**
//	 * ǩ���õ� ���SECURITY_CODE��session�е� ������
//	 */
//	static public final String SESSION_RANDOM_SECURITY_CODE_KEY = WebModuleContext.class.getName() + ".RANDOM_SECURITY_CODE";
	
	/**
	 * ��������conversation�ĵ�ǰconersation id�ڿͻ��˱��е���������
	 */
	static public final String PARAM_CONVERSATION_ID_KEY = WebModuleContext.class.getName() + ".CONVERSATION_ID";
	
	/**
	 * ��ǰconversation id �� current �е� �������� ��������Կ���֪����ǰ�Ƿ���conversation in server������
	 * ͨ�� getConversationInServer().getConversationId()ȥʵ����ServerConversation
	 */
	static public final String CURRENT_CONVERSATION_ID_KEY = WebModuleContext.class.getName() + ".conversationId";
	
	/**
	 * ��ǰ���������� ģ�� ��ɵ� current ��һ�� stack �������е����Ե�key
	 */
	static public final String REQUEST_CURRENT_STACK_KEY = WebModuleContext.class.getName() + ".CURRENT_STACK_KEY";
	
	static public final String DEFAULT_SECURITY_CODE = "UUID.randomUUID().toString()";//UUID.randomUUID().toString();
	
	
	/**
	 * ��ʼ��WebContext
	 * 1����¼��ģ��������
	 * 2����ģ�������ĵ���ģ������1
	 * 3����ǰģ�������еĽڵ�������
	 * 4�����µ�currentScope���õ�request�У�Ϊ�˿�����ģ����ʹ��currentScope
	 *
	 */
	public void init(WebModuleContext parent)  {
		this.parent = parent;
		if(this.parent!=null) {
			this.parent.childWebModuleNumber++;
			this.treeNodeLocationId = this.parent.treeNodeLocationId + "." + this.parent.childWebModuleNumber;
		}
//		 * 1����֮ǰ��currentScope�ƶ���stack
//		 * 2�����µ�currentScope���õ�request�У�Ϊ�˿�����ģ����ʹ��currentScope		
//		Map<String,Object> oldCurrentScope = (Map<String, Object>) this.request.getAttribute("currentScope");
//		if(oldCurrentScope!=null) {
//			Stack<Map<String,Object>> currentStack = getCurrentStack();
//			currentStack.push(oldCurrentScope);
//		}
		this.request.setAttribute("currentScope", currentScope);
	}
	
	/**
	 * ����WebModuleContext
	 * 1������и�ģ�������ģ��Ѹ�ģ�������ĵ�currentScope���õ�request�У�Ϊ�˿�����ģ����ʹ��currentScope
	 *
	 */
	public void destroy()  {
		if(this.parent!=null) {
			this.request.setAttribute("currentScope", this.parent.currentScope);
		}
		else {
			this.request.removeAttribute("currentScope");
		}
//		 * 1����֮ǰ��currentScope��pop���ƶ�����������request��Ϊ��ǰ���ģ�����ʹ��currentScope		
//		Stack<Map<String,Object>> currentStack = getCurrentStack();
//		if(!currentStack.isEmpty()) {
//			Map<String,Object> oldCurrentScope = currentStack.pop();
//			this.request.setAttribute("currentScope", oldCurrentScope);
//		}
	}
	
//	/**
//	 * ȡ�������е����� ģ��� current��stack
//	 * @return
//	 */
//	private Stack<Map<String,Object>> getCurrentStack() {
//		Stack<Map<String,Object>> currentStack = (Stack<Map<String,Object>>)
//			request.getAttribute(REQUEST_CURRENT_STACK_KEY);
//		if(currentStack==null) {
//			currentStack = new Stack<Map<String,Object>>();
//			request.setAttribute(REQUEST_CURRENT_STACK_KEY,currentStack);
//		}
//		return currentStack;
//	}

	public ApplicationContext getApplicationContext() {
		return (WebApplicationContext)this.servletContext.getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}
	
    public Object getCurrentAttribute(String key) {
        return currentScope.get(key);
    }

    public void setCurrentAttribute(String key, Object value) {
    	currentScope.put(key, value);
    }

	public void removeCurrentAttribute(String key) {
		currentScope.remove(key);
	}
	
    public Object getRequestAttribute(String key) {
        return currentScope.get(key);
    }

    public void setRequestAttribute(String key, Object value) {
    	currentScope.put(key, value);
    }

	public void removeRequestAttribute(String key) {
		currentScope.remove(key);
	}
	
  public Object getSessionAttribute(String key) {
    return httpSession.getAttribute(key.toString());
  }
  public Object getApplicationAttribute(String key) {
    return servletContext.getAttribute(key);
  }
  public void setSessionAttribute(String key,Object value) {
    httpSession.setAttribute(key,value);
  }
  public void setApplicationAttribute(String key,Object value) {
    servletContext.setAttribute(key,value);
  }

  private HttpServletRequest request;
  private HttpServletResponse response;
  private HttpSession httpSession;
  private ServletContext servletContext;
  //public HttpServlet httpServlet;
//  public ServletConfig servletConfig;
  
  
  
  public void removeApplicationAttribute(String key) {
		servletContext.removeAttribute(key);
	}



	public void removeSessionAttribute(String key) {
		httpSession.removeAttribute(key);
	}
	
	/**
	 * ��ȡ��������Conversation
	 * ���������û��PARAM_CONVERSATION_ID_KEY�������򴴽��µķ�������Conversation������session�е�һ����
	 * ���ܰ��ϵ�Conversation����ȥ
	 */
	public Conversation getConversationInServer() {
		Conversation conversation = super.getConversationInServer();
		if(conversation.getConversationId()==null) {
			String conversationId = request.getParameter(PARAM_CONVERSATION_ID_KEY + this.treeNodeLocationId);
			if("".equals(conversationId)) {
				conversationId = null;
			}
			conversation = conversation.init(conversationId);
			if(conversation!=null && conversation.getConversationId()!=null) {
				this.setCurrentAttribute(CURRENT_CONVERSATION_ID_KEY, conversation.getConversationId());
				this.setCurrentAttribute(CURRENT_SERVER_CONVERSATION_KEY,conversation);
			}
		}
		return conversation;
	}
	
	/**
	 * ��ȡ�ͻ���Conversation�������л��õ�Conversation
	 * Ҫ���viewstate��ǩ����ǩ������ȷ�����ɱ��۸ģ��׳�����
	 */
	public Conversation getConversationInClient() {
		 Conversation vs = super.getConversationInClient();
		 if(vs.getConversationId()==null) {
		     String param = this.request.getParameter(PARAM_VIEWSTATE_KEY + this.treeNodeLocationId);
		     if(param!=null && !"".equals(param)) {
		    	 String signature = this.request.getParameter(PARAM_VIEWSTATE_SIGNATURE_KEY + this.treeNodeLocationId);
		    	 //String expectDigest = MessageDigestUtil.digest(param + getSessionRandomSecurityCode());
		    	 String expectDigest = MessageDigestUtil.digest(param + DEFAULT_SECURITY_CODE);
		    	 if(!expectDigest.equals(signature)) {
		    		 throw new ConversationException("ViewStateǩ������ȷ��" + signature + ",expect:" + expectDigest);
		    	 }
		    	 vs = vs.init(param);
		    	 this.setCurrentAttribute(CURRENT_VIEWSTATE_KEY,vs);
		     }
		 }
	     return vs;
	}

//	/**
//	 * ��ȡ��ǰsession������ɵ�SecurityCode
//	 * @return
//	 */
//	public String getSessionRandomSecurityCode() {
//		String securityCode = (String)this.httpSession.getAttribute(SESSION_RANDOM_SECURITY_CODE_KEY);
//		if(securityCode==null) {
//			securityCode = UUID.randomUUID().toString();
//			this.httpSession.setAttribute(SESSION_RANDOM_SECURITY_CODE_KEY,securityCode);
//		}
//		return securityCode;
//	}

	public Map<String, Object> getCurrentAttributesMap() {
		return this.currentScope;
	}
	
	public int getChildWebModuleNumber() {
		return childWebModuleNumber;
	}
	public Map<String, Object> getCurrentScope() {
		return currentScope;
	}
	public WebModuleContext getParent() {
		return parent;
	}
	public String getTreeNodeLocationId() {
		return treeNodeLocationId;
	}
	
	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	/**
	 * �Ƿ��Ƕ���ģ��������
	 * @return
	 */
	public boolean isTop() {
		return this.parent==null;
	}
	
	/**
	 * ���������ģ��͵�ǰ��ģ���Ƿ�ƥ��
	 * @return
	 */
	public boolean isMatchRequest() {
		if("GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		String treeNodeLocationId = request.getParameter(PARAM_TREE_NODE_LOCATION_ID); 
		if(StringUtils.hasText(treeNodeLocationId)) {
			if(treeNodeLocationId.equals(this.treeNodeLocationId)) {
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the httpSession
	 */
	public HttpSession getHttpSession() {
		return httpSession;
	}

	/**
	 * @param httpSession the httpSession to set
	 */
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public Map<String, String[]> getRequestParamsMap() {
		return this.request.getParameterMap();
	}
	
	public String getRequestParameter(String name) {
		return this.request.getParameter(name);
	}
	
	public String[] getRequestParameterValues(String name) {
		return this.request.getParameterValues(name);
	}
}
