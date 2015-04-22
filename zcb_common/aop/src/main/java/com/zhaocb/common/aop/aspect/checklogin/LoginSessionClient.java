package com.zhaocb.common.aop.aspect.checklogin;

import java.io.IOException;
/**
 * 登陆态获取服务接口
 */
public interface LoginSessionClient {
	/**
	 * 根据sessionKey去session服务器获取登陆态
	 * @param sessionKey
	 * @return LoginSessionData
	 * @throws IOException
	 * @throws Exception 
	 */
	LoginSessionData getSession(String sessionKey) throws IOException, Exception;
}
