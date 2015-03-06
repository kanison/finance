package com.tenpay.sm.context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.context.app.AppContext;

/**
 * <p>Title: </p>
 * <p>Description: xx框架
 * 通过ThreadLocal来获得当前Thread的Context的类
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 洪桃李</p>
 * @author 洪桃李
 * @version 2.0
 */

public class ContextUtil {
  private static final Log LOG = LogFactory.getLog(ContextUtil.class);
  protected static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<Context>();
  
  /**
   * 设置当前的Context
   * @param context
   */
  public static void setContext(Context context) {
    contextThreadLocal.set(context);
  }
  
  /**
   * 取得当前的Context，如果不存在，默认建立一个AppContext
   * @return
   */
  public static Context getContext() {
    Context ctx = (Context)contextThreadLocal.get();
    if(ctx==null) {
    	if(LOG.isDebugEnabled()) {
    		LOG.debug("当前线程不存在Context，开始创建默认的AppContext");
    	}
    	ctx = new AppContext();
    	contextThreadLocal.set(ctx);
    }
    return ctx;
    //return ctx!=null?ctx:defaultContext;
  }
  
  /**
   * 判断当前是否有Context
   * @return
   */
  public static boolean hasContext() {
	  return (contextThreadLocal.get()!=null);
  }
}
