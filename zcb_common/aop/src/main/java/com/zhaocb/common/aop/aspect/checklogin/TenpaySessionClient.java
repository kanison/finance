package com.zhaocb.common.aop.aspect.checklogin;

import java.io.IOException;
/**
 * TenpaySessionClient
 */
public interface TenpaySessionClient {
	/**
	 * getSession
	 * @param sessionKey
	 * @return byte[]
	 * @throws IOException
	 * @throws Exception 
	 */
	public byte[] getSession(String sessionKey) throws IOException, Exception;
	/**
	 * setSessionName
	 * @param sessionName
	 */
	public void setSessionName(String sessionName);
}
