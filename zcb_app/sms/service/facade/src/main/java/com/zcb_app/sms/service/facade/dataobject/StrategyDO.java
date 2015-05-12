package com.zcb_app.sms.service.facade.dataobject;

import java.math.BigInteger;

public class StrategyDO {
	
	private Long timespan;//	频率控制的周期，以秒为单位
	
	private int mob_no_limit;//	手机号频率值，以条为单位
	
	private int ip_limit;//	ip频率值，以条为单位
	
	private BigInteger blocktime;//	超过频率后被限制的时间，以秒为单位

	public Long getTimespan() {
		return timespan;
	}

	public void setTimespan(Long timespan) {
		this.timespan = timespan;
	}

	public int getMob_no_limit() {
		return mob_no_limit;
	}

	public void setMob_no_limit(int mob_no_limit) {
		this.mob_no_limit = mob_no_limit;
	}

	public int getIp_limit() {
		return ip_limit;
	}

	public void setIp_limit(int ip_limit) {
		this.ip_limit = ip_limit;
	}

	public BigInteger getBlocktime() {
		return blocktime;
	}

	public void setBlocktime(BigInteger blocktime) {
		this.blocktime = blocktime;
	}
}
