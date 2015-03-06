/**
 * 
 */
package com.app.utils;

import java.io.IOException;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.cache.Cache;

public class AliSpyMemCachedWrapper implements Cache {
	private static final Log LOG = LogFactory.getLog(AliSpyMemCachedWrapper.class);
	
	private MemcachedClient memcachedClient;
	private Map<String, String> appConfig;
	private String prefix ="test_";

	private void makesureinit() {
		if (memcachedClient == null) {
			String addr = appConfig.get("alicacheAddress");
			if(CommonUtil.trimString(addr) == null){
				addr = "92da7d1e5b9811e4.m.cnhzaliqshpub001.ocs.aliyuncs.com:11211";
			}
			String username = appConfig.get("alicacheUsername");
			if(CommonUtil.trimString(username) == null){
				username = "92da7d1e5b9811e4";
			}
			String password = appConfig.get("alicachePassword");
			if(CommonUtil.trimString(password) == null){
				password = "123456";
			}

			try {
				AuthDescriptor ad = new AuthDescriptor(
						new String[] { "PLAIN" }, new PlainCallbackHandler(
								username, password));
				memcachedClient = new MemcachedClient(
						new ConnectionFactoryBuilder().setProtocol(
								Protocol.BINARY).setAuthDescriptor(ad).build(),
						AddrUtil.getAddresses(addr));
			} catch (IOException e) {
				/**
				 * TODO Log init memcached connection exception
				 */
				throw new RuntimeException("初始化memcached连接异常", e);
			}
			
			LOG.info("初始化ocsmemcached连接成功");
			
			//ocs 只有一份，测试环境数据不能写到生成环境上，只有明确配置生成环境才能写入生成环境，否则都写入测试环境
			String serveicType = CommonUtil.trimString(appConfig.get("serveicType"));
			if(serveicType != null && serveicType.equals("shengchan")){
				prefix = ""; //生成环境不用加前缀
			}

		}
	}

	public Object get(String key) {
		this.makesureinit();
		return memcachedClient.get(prefix+key);
	}

	public boolean isExist(String key) {
		this.makesureinit();
		return memcachedClient.get(prefix+key) != null;
	}

	/**
	 * 默认7天过期
	 */
	public Object put(String key, Object value) {
		this.makesureinit();
		return memcachedClient.set(prefix+key, 7 * 24 * 60 * 60, value);// 7天过期
	}

	public Object put(String key, int expire, Object value) {
		if (expire > 30 * 24 * 60 * 60) {
			expire = 0;// 避免触发memcache的默认配置，缓存时间不超过30天的问题，如果expire>30天，则设置永不过期
		}
		this.makesureinit();
		return memcachedClient.set(prefix+key, expire, value);
	}

	public Object remove(String key) {
		this.makesureinit();
		return memcachedClient.delete(prefix+key);
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}

	public Map<String, String> getAppConfig() {
		return appConfig;
	}

}
