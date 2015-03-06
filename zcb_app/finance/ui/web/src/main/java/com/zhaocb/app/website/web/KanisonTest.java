package com.zhaocb.app.website.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.aop.annotation.LogMethod;
import com.zhaocb.app.website.web.model.CommonOutput;



/**
 * π∫¬Ú≤ ∆±
 * 
 * @author wenlonwang
 */
@RequestMapping
public class KanisonTest {
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@LogMethod
	public CommonOutput handleKanisonTest() {

		CommonOutput out = new CommonOutput();
		out.setRetCode(123);
		out.setRetMsg("kanison hello ≤‚ ‘");
		return out;
	}
}

