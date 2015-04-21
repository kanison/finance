package com.zhaocb.common.signature.imp;

import java.security.Key;
import java.util.Date;

public class KeyContainer {
	private Date expireTime;
	private Key key;
	
	public boolean isValid()
	{
		if(expireTime.before(new Date()))
		{
			return false;
		}else
		{
			return true;
		}
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
}
