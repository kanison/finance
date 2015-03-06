/**
 * 
 */
package com.tenpay.sm.web.plugin;

/**
 * 当前模块页面展现的时候可以生成html的插件
 * @author li.hongtl
 *
 */
public interface SmPlugin {
	/**
	 * 生成插件的html
	 * @return
	 */
	String generateContent();
}
