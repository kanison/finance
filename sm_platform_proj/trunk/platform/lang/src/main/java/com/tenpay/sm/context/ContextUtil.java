package com.tenpay.sm.context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.context.app.AppContext;

/**
 * <p>Title: </p>
 * <p>Description: xx���
 * ͨ��ThreadLocal����õ�ǰThread��Context����
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: ������</p>
 * @author ������
 * @version 2.0
 */

public class ContextUtil {
  private static final Log LOG = LogFactory.getLog(ContextUtil.class);
  protected static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<Context>();
  
  /**
   * ���õ�ǰ��Context
   * @param context
   */
  public static void setContext(Context context) {
    contextThreadLocal.set(context);
  }
  
  /**
   * ȡ�õ�ǰ��Context����������ڣ�Ĭ�Ͻ���һ��AppContext
   * @return
   */
  public static Context getContext() {
    Context ctx = (Context)contextThreadLocal.get();
    if(ctx==null) {
    	if(LOG.isDebugEnabled()) {
    		LOG.debug("��ǰ�̲߳�����Context����ʼ����Ĭ�ϵ�AppContext");
    	}
    	ctx = new AppContext();
    	contextThreadLocal.set(ctx);
    }
    return ctx;
    //return ctx!=null?ctx:defaultContext;
  }
  
  /**
   * �жϵ�ǰ�Ƿ���Context
   * @return
   */
  public static boolean hasContext() {
	  return (contextThreadLocal.get()!=null);
  }
}
