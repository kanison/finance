/**
 * 
 */
package com.tenpay.sm.web.tag;

import java.util.ArrayList;
import java.util.List;

import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.tenpay.sm.web.plugin.ScriptPlugin;
import com.tenpay.sm.web.plugin.SmPlugin;
import com.tenpay.sm.web.trace.TracePlugin;

/**
 * 当前模块页面展现的时候可以生成html的插件(SmPlugin)们的执行器
 * @author li.hongtl
 *
 */
public class SmPluginExecutor {
	private static final String CURRENT_SMPLUGINS_KEY = SmPluginExecutor.class.getName() + ".SMPLUGIN_KEY.";
	/**
	 * 当前模块中增加插件
	 * @param plugin
	 */
	public static void addPlugin(SmPlugin plugin) {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		List<SmPlugin> plugins = (List<SmPlugin>)wc.getCurrentAttribute(CURRENT_SMPLUGINS_KEY);
		if(plugins==null) {
			plugins = new ArrayList<SmPlugin>();
			ContextUtil.getContext().setCurrentAttribute(CURRENT_SMPLUGINS_KEY ,plugins);
		}
		plugins.add(plugin);
	}
	
	/**
	 * 生成form中需要的插件html内容
	 * @return
	 */
	public static String formPlugin() {
		WebModuleContext wc = (WebModuleContext)ContextUtil.getContext();
		List<SmPlugin> plugins = (List<SmPlugin>)wc.getCurrentAttribute(CURRENT_SMPLUGINS_KEY);
		if(plugins!=null) {
			StringBuffer sb = new StringBuffer();
			for(SmPlugin plugin : plugins) {
				sb.append(plugin.generateContent());
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * 增加页面中生成alert的plugin
	 * @param message
	 */
	public static void alert(String message) {
		addPlugin(new ScriptPlugin("alert('" + message + "')"));
	}
	
	/**
	 * 增加页面中生成window.close的plugin
	 */
	public static void closeWindow(boolean confirm) {
		if(!confirm) {
			addPlugin(new ScriptPlugin("window.opener=null;window.close()"));
		}
		else {
			addPlugin(new ScriptPlugin("window.close()"));
		}
	}
	
	public static String trace() {
		return TracePlugin.INSTANCE.generateContent();
	}
}
