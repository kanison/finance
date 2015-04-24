package com.zhaocb.common.aop.filter;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.IPUtil;
import com.zhaocb.common.aop.aspect.LimitIPAccessFrequency;
import com.zhaocb.common.aop.exception.IpLimitException;

public class IpLimitFilter implements Filter {

	private LimitIPAccessFrequency limitIPAccessFrequency = new LimitIPAccessFrequency();
	private Log LOG = LogFactory.getLog(IpLimitFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String ip = IPUtil.getClientIP(request);
		if (!limitIPAccessFrequency.checkIpLimit(ip)) {
			LOG.warn("ip访问频率过高:" + ip);
			if (response instanceof HttpServletResponse) {
				((HttpServletResponse) response).setStatus(404);
				response.setContentType("text/html");
				Writer writer = response.getWriter();
				writer
						.write(ip
								+ ": \nYour ip is limited. Please try again later!");
				writer.close();
				return;
			} else
				throw new IpLimitException(
						ip
								+ ": \nYour ip is limited. Please try again later!");
		}
		chain.doFilter(request, response);
		return;
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
