package com.zhaocb.common.aop.aspect;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.RedirectView;

import com.app.utils.CommonUtil;
import com.tenpay.sm.web.view.ViewUtil;

public class RedirectToLogin {
	private static final Log LOG = LogFactory.getLog(RedirectToLogin.class);

	public static void redirectToLogin(String login_url) {
		if (ViewUtil.getCurrentRequestViewType().isRenderHTML()) {
			if (login_url == null) {
				throw new RuntimeException("not set login_url");
			}
			HttpServletRequest request = CommonUtil.getHttpServletRequest();
			StringBuilder url = new StringBuilder(login_url);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			url.append("?tmstmp=");
			url.append(new Date().getTime() / 1000);
			try {
				String oriUrl = URLEncoder.encode(httpRequest.getRequestURL()
						.toString(), "GBK");
				url.append("&resulturl=");
				url.append(oriUrl);
				url.append("&u1=");
				url.append(oriUrl);
			} catch (UnsupportedEncodingException e) {
				LOG.error("±àÂëÒì³£", e);
			}
			String toUrl = url.toString();
			if (LOG.isDebugEnabled())
				LOG.debug("Ìø×ªÖÁµÇÂ½Ò³Ãæ: " + toUrl);
			ViewUtil.setView(new RedirectView(toUrl));
		}
	}

}
