/**
 * 
 */
package com.tenpay.sm.web.page;

import java.util.Map;

/**
 * Page 有 多个 module
 * TODO 布局,locale,encoding等整个页面的一些特性，再考虑
 * @author li.hongtl
 *
 */
public interface Page {
	Map<String, Object> getModules();
}
