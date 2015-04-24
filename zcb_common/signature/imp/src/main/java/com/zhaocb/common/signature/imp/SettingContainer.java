package com.zhaocb.common.signature.imp;

import java.util.Date;
import com.zhaocb.common.signature.facade.SignResult;

public class SettingContainer {
	private Date expireTime;
	private SignResult setting;
	
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
	public void setSetting(SignResult setting) {
		this.setting = setting;
	}
	public SignResult getSetting() {
		return setting;
	}

}
