package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


/**
 * 对应IP频率受限记录表：mv_db.t_ip_limit
 * @author Gu.Dongying
 */
public class IPLimitDO implements Serializable {
	
	private static final long serialVersionUID = -8302017148267235845L;

	private BigInteger fpkid;//流水编号（自增主键）
	
	private String fclient_ip;//用户ip
	
	private BigInteger ftmpl_id;//短信模板id，触发受限的短信模板id
	
	private Date fblock_stime;//受限制的开始时间
	
	private BigInteger fblock_timespan;//受限制的时长，秒为单位，只有当前时间大于fblock_stime+ fblock_timespan才解除限制

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
