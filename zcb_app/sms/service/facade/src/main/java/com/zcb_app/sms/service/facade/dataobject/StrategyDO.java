package com.zcb_app.sms.service.facade.dataobject;

import java.math.BigInteger;

public class StrategyDO {
	
	private Long timespan;//	Ƶ�ʿ��Ƶ����ڣ�����Ϊ��λ
	
	private int mob_no_limit;//	�ֻ���Ƶ��ֵ������Ϊ��λ
	
	private int ip_limit;//	ipƵ��ֵ������Ϊ��λ
	
	private BigInteger blocktime;//	����Ƶ�ʺ����Ƶ�ʱ�䣬����Ϊ��λ

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
