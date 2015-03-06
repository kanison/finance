/**
 * 
 */
package com.tenpay.sm.web.export;

import com.tenpay.sm.client.facade.HttpRequestModel;
import com.tenpay.sm.client.facade.WebContentFacade;
import com.tenpay.sm.lang.util.ExceptionUtil;
import com.tenpay.sm.web.tag.ModuleExecutor;

/**
 * WebContentFacade在服务器端的web service实现
 * @author li.hongtl
 *
 */
public class WebContentFacadeServerImpl implements WebContentFacade {

	public String httpGetWebContent(String path, HttpRequestModel httpRequestModel) {
		String content;
		try {
			content = ModuleExecutor.execute(path);
		} catch (Exception e) {
			throw ExceptionUtil.wrapException(e);
		}
		return content;
	}

}
