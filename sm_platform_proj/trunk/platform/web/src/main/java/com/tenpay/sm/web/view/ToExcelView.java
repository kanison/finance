/**
 * 
 */
package com.tenpay.sm.web.view;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.web.excelutils.ExcelUtils;

/**
 * @author eniacli
 */
/*
 * ${model.name} means getting property of the name from the model object.
 * ${!model.name} means that last cell and this cell merge if model.name value
 * equals last cell value. #foreach model in ${list}£¬means that iterate
 * list£¬modelId is implied index of the list. #each ${model} ${width1},${width2}
 * on ${keys}£¬model can be a Map,JavaBean,Collection or Array object, #each key
 * will show all property of the model.${width?} means merge ${width?} cells. If
 * only one width, all property use the same width. If more than one, use the
 * witdh in order, not set will use "1".${keys} can be Map,Array,List which
 * contain field's name you want to display, it's not required. ${list[0].name}
 * means get the first object from list, then read the property of name.
 * ${map(key)} get the value from the map by the key name.
 * ${list[${index}].name} [] can be a variable. ${map(${key})} () can be a
 * variable. #sum qty on ${list} where name like/=str sum qty on ${list}
 * collection by where condition. In net.sf.excelutils.tags Package, you can
 * implement ITag to exentd Tag key. eg, FooTag will parse #foo. ExcelResult for
 * webwork. ${model${index}} support. #call service.method("str", ${name}) call
 * a method #formula SUM(C${currentRowNo}:F${currentRowNo}) means output excel
 * formula SUM(C?:F?) ? means currentRowNo. #page tag to split page. #if tag
 * support beanshell scripts.
 */

public class ToExcelView extends AbstractToDataView {
	private static final Log LOG = LogFactory.getLog(ToExcelView.class);
	private static final String FILE_NAME_KEY = "down_file_name";

	private String excelPath = null;

	public ToExcelView() {
		setContentType("application/vnd.ms-excel");
		excelPath = ReloadableAppConfig.appConfig.get("excelPath");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedTemplateModel(Map modelsMap,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			modelsMap = toUserDataMap(modelsMap);

			String fileName = request.getParameter(FILE_NAME_KEY);
			if (fileName != null && fileName.length() > 0) {
				if (fileName.length() > 60)
					fileName = fileName.substring(0, 60);
				if (!fileName.endsWith(".xls"))
					fileName = fileName + ".xls";
				// Ìæ»»·Ç·¨×Ö·û
				fileName = fileName.replaceAll("\r", "");
				fileName = fileName.replaceAll("\n", "");
				response.setHeader("Content-Disposition",
						"attachment; filename="
								+ URLEncoder.encode(fileName, "UTF-8"));
			}

			if (modelsMap != null) {
				ExcelUtils.addValue(modelsMap);
				Object view = ViewUtil.getCurrentView();
				String viewName;
				if (view != null) {
					viewName = view.toString();
				} else {
					viewName = request.getParameter("viewname");
					if (viewName == null)
						viewName = request.getParameter("viewName");
					if (viewName == null) {
						viewName = request.getRequestURI();
						int index = viewName.lastIndexOf('/');
						if (index > 0) {
							viewName = viewName.substring(index + 1);
						}
					}
				}
				if (excelPath == null)
					ExcelUtils.export(request.getSession().getServletContext(),
							"/WEB-INF/xls/" + viewName, response
									.getOutputStream());
				else
					ExcelUtils.export(excelPath + viewName, response
							.getOutputStream());
			}

		} catch (Throwable t) {
			LOG.warn("ExceUtils Excelption", t);
		}
	}
}
