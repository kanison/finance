/**
 * 
 */
package com.tenpay.sm.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tenpay.sm.lang.util.NullablePropertyUtils;

/**
 * @author torryhong
 *
 */
public class ToRESTView extends AbstractToDataView {
	public ToRESTView() {
		setContentType("text/text; charset=GBK");
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractTemplateView#renderMergedTemplateModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedTemplateModel(Map modelsMap,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		modelsMap = toUserDataMap(modelsMap);
		modelsMap = NullablePropertyUtils.getNestedProperties(modelsMap);
		if(modelsMap!=null) {
			PrintWriter pw = response.getWriter();
			int index = 0;
			for(Object key : modelsMap.keySet()) {
				if(index!=0) {
					pw.print("&");
				}
				index++;
				pw.print(key);
				pw.print("=");
				Object value = modelsMap.get(key);
				if(value!=null) {
					pw.print(java.net.URLEncoder.encode(value.toString(), "GBK"));
				}
			}
		}
	}

}
