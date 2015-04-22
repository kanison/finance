package com.zhaocb.common.aop.aspect.checklogin;

import java.io.IOException;
/**
 * ��½̬��ȡ����ӿ�
 */
public interface LoginSessionClient {
	/**
	 * ����sessionKeyȥsession��������ȡ��½̬
	 * @param sessionKey
	 * @return LoginSessionData
	 * @throws IOException
	 * @throws Exception 
	 */
	LoginSessionData getSession(String sessionKey) throws IOException, Exception;
}
