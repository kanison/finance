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
 * <p>Title: xx框架</p>
 * <p>Description: xxt框架
 * Web环境下获取各个作用域的属性的Context
 * 一般建议用WebModuleContextFilter创建WebModuleContext
 * current < request < session < conversation < application
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 洪桃李</p>
 * @author 洪桃李
 * @version 1.0
 */
public class WebModuleContext extends AbstractContext {
	public WebModuleContext() {
	}
	/**
	 * current scope的所有属性的Map
	 * 注意 WebModuleContextFilter 的配置方式，当一个请求中 包含多个模块的时候，每个模块都要经过filter，都要有一个 WebModuleContext !
	 */
	private Map<String,Object> currentScope = new HashMap<String,Object>();
	
	/**
	 * 处理当前模块的handler
	 */
	private Object handler = null;
	
	/**
	 * 上级模块
	 */
	private WebModuleContext parent = null;
	
	/**
	 * 当前模块在整个页面的模块树中的位置编号, 例如 空, .1 , .2 , .2.1, .2.2, .2.1.1, 
	 */
	private String treeNodeLocationId = "";
	
	/**
	 * 子模块的数量
	 */
	private int childWebModuleNumber = 0;
	
	
	/**
	 * ViewState(客户端Conversation)在客户端表单中的隐藏域名
	 */
	static public final String PARAM_TREE_NODE_LOCATION_ID = WebModuleContext.class.getName() + ".TREE_NODE_LOCATION_ID";
	/**
	 * ViewState(客户端Conversation)在客户端表单中的隐藏域名
	 */
	static public final String PARAM_VIEWSTATE_KEY = WebModuleContext.class.getName() + ".VIEWSTATE";
	/**
	 * ViewState(客户端Conversation)的签名(用于防篡改)在客户端表单中的隐藏域名
	 */
	static public final String PARAM_VIEWSTATE_SIGNATURE_KEY = WebModuleContext.class.getName() + ".VIEWSTATE_SIGNATURE";
//	/**
//	 * 签名用的 随机SECURITY_CODE在session中的 属性名
//	 */
//	static public final String SESSION_RANDOM_SECURITY_CODE_KEY = WebModuleContext.class.getName() + ".RANDOM_SECURITY_CODE";
	
	/**
	 * 服务器端conversation的当前conersation id在客户端表单中的隐藏域名
	 */
	static public final String PARAM_CONVERSATION_ID_KEY = WebModuleContext.class.getName() + ".CONVERSATION_ID";
	
	/**
	 * 当前conversation id 在 current 中的 属性名， 从这个属性可以知道当前是否有conversation in server，避免
	 * 通过 getConversationInServer().getConversationId()去实例化ServerConversation
	 */
	static public final String CURRENT_CONVERSATION_ID_KEY = WebModuleContext.class.getName() + ".conversationId";
	
	/**
	 * 当前请求中所有 模块 组成的 current 的一个 stack 在请求中的属性的key
	 */
	static public final String REQUEST_CURRENT_STACK_KEY = WebModuleContext.class.getName() + ".CURRENT_STACK_KEY";
	
	static public final String DEFAULT_SECURITY_CODE = "UUID.randomUUID().toString()";//UUID.randomUUID().toString();
	
	
	/**
	 * 初始化WebContext
	 * 1、记录父模块上下文
	 * 2、父模块上下文的子模块数加1
	 * 3、当前模块在树中的节点编号生成
	 * 4、把新的currentScope设置到request中，为了可以在模板中使用currentScope
	 *
	 */
	public void init(WebModuleContext parent)  {
		this.parent = parent;
		if(this.parent!=null) {
			this.parent.childWebModuleNumber++;
			this.treeNodeLocationId = this.parent.treeNodeLocationId + "." + this.parent.childWebModuleNumber;
		}
//		 * 1、把之前的currentScope移动到stack
//		 * 2、把新的currentScope设置到request中，为了可以在模板中使用currentScope		
//		Map<String,Object> oldCurrentScope = (Map<String, Object>) this.request.getAttribute("currentScope");
//		if(oldCurrentScope!=null) {
//			Stack<Map<String,Object>> currentStack = getCurrentStack();
//			currentStack.push(oldCurrentScope);
//		}
		this.request.setAttribute("currentScope", currentScope);
	}
	
	/**
	 * 销毁WebModuleContext
	 * 1、如果有父模块上下文，把父模块上下文的currentScope设置到request中，为了可以在模板中使用currentScope
	 *
	 */
	public void destroy()  {
		if(this.parent!=null) {
			this.request.setAttribute("currentScope", this.parent.currentScope);
		}
		else {
			this.request.removeAttribute("currentScope");
		}
//		 * 1、把之前的currentScope从pop中移动出来，放入request，为了前面的模板可以使用currentScope		
//		Stack<Map<String,Object>> currentStack = getCurrentStack();
//		if(!currentStack.isEmpty()) {
//			Map<String,Object> oldCurrentScope = currentStack.pop();
//			this.request.setAttribute("currentScope", oldCurrentScope);
//		}
	}
	
//	/**
//	 * 取得请求中的所有 模块的 current的stack
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
	 * 获取服务器端Conversation
	 * 如果请求中没有PARAM_CONVERSATION_ID_KEY参数，则创建新的服务器端Conversation，放入session中的一个环
	 * 可能把老的Conversation挤出去
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
	 * 获取客户端Conversation，反序列化得到Conversation
	 * 要检查viewstate的签名，签名不正确，怀疑被篡改，抛出错误
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
		    		 throw new ConversationException("ViewState签名不正确：" + signature + ",expect:" + expectDigest);
		    	 }
		    	 vs = vs.init(param);
		    	 this.setCurrentAttribute(CURRENT_VIEWSTATE_KEY,vs);
		     }
		 }
	     return vs;
	}

//	/**
//	 * 获取当前session随机生成的SecurityCode
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
	 * 是否是顶级模块上下文
	 * @return
	 */
	public boolean isTop() {
		return this.parent==null;
	}
	
	/**
	 * 发出请求的模块和当前的模块是否匹配
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
