package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ��ӦIPƵ�����޼�¼����mv_db.t_ip_limit
 * @author Gu.Dongying
 */
public class IPLimitDO implements Serializable {
	
	private static final long serialVersionUID = -8302017148267235845L;

	private BigInteger fpkid;//��ˮ��ţ�����������
	
	private String fclient_ip;//�û�ip
	
	private BigInteger ftmpl_id;//����ģ��id���������޵Ķ���ģ��id
	
	private Date fblock_stime;//�����ƵĿ�ʼʱ��
	
	private BigInteger fblock_timespan;//�����Ƶ�ʱ������Ϊ��λ��ֻ�е�ǰʱ�����fblock_stime+ fblock_timespan�Ž������

	public BigInteger getFpkid() {
		return fpkid;
	}

	public void setFpkid(BigInteger fpkid) {
		this.fpkid = fpkid;
	}

	public String getFclient_ip() {
		return fclient_ip;
	}

	public void setFclient_ip(String fclient_ip) {
		this.fclient_ip = fclient_ip;
	}

	public BigInteger getFtmpl_id() {
		return ftmpl_id;
	}

	public void setFtmpl_id(BigInteger ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	public Date getFblock_stime() {
		return fblock_stime;
	}

	public void setFblock_stime(Date fblock_stime) {
		this.fblock_stime = fblock_stime;
	}

	public BigInteger getFblock_timespan() {
		return fblock_timespan;
	}

	public void setFblock_timespan(BigInteger fblock_timespan) {
		this.fblock_timespan = fblock_timespan;
	}

}