package com.zhaocb.common.aop.aspect.checklogin.impl;

import com.zhaocb.common.aop.aspect.checklogin.LoginSessionClient;
import com.zhaocb.common.aop.aspect.checklogin.LoginSessionData;
import com.zhaocb.common.aop.aspect.checklogin.TenpaySessionClient;
/**
 * ��½̬��ȡ����ӿ�ʵ��
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
	 * ����sessionKeyȥsession��������ȡ��½̬
	 * @param sessionKey
	 * @return LoginSessionData
	 * @throws Exception 
	 */
	public LoginSessionData getSession(String sessionKey) throws Exception {
		byte[] sessionContent = tenpaySessionClient.getSession(sessionKey);
		return SessionClientUtil.parseLoginSessionData(sessionContent);
	}
}
