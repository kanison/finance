package com.zhaocb.common.aop.aspect.checklogin.impl;

import com.zhaocb.common.aop.aspect.checklogin.LoginSessionClient;
import com.zhaocb.common.aop.aspect.checklogin.LoginSessionData;
import com.zhaocb.common.aop.aspect.checklogin.TenpaySessionClient;
/**
 * 登陆态获取服务接口实现
 */
public class LoginSessionClientImpl implements LoginSessionClient {
	/**
	 * TenpaySessionClient
	 */
	private TenpaySessionClient tenpaySessionClient;
	public TenpaySessionClient getTenpaySessionClient() {
		return tenpaySessionClient;
	}
	public void setTenpaySessionClient(TenpaySessionClient tenpaySessionClient) {
		this.tenpaySessionClient = tenpaySessionClient;
	}
	/**
	 * 根据sessionKey去session服务器获取登陆态
	 * @param sessionKey
	 * @return LoginSessionData
	 * @throws Exception 
	 */
	public LoginSessionData getSession(String sessionKey) throws Exception {
		byte[] sessionContent = tenpaySessionClient.getSession(sessionKey);
		return SessionClientUtil.parseLoginSessionData(sessionContent);
	}
}
