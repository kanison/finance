package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.util.Date;


/**
 * 对应手机号频率受限记录表：mv_db.t_mobile_limit
 * @author Gu.Dongying
 *
 */
public class MobileLimitDO implements Serializable {
	
	private static final long serialVersionUID = 4726699177919005728L;

	private long fpkid;//流水编号（自增主键）
	
	private String fmobile_no;//手机号码
	
	private Long ftmpl_id;//短信模板id，触发受限的短信模板id
	
	private Date fblock_stime;//受限制的开始时间
	
	private long fblock_timespan;//受限制的时长，秒为单位，只有当前时间大于fblock_stime+ fblock_timespan才解除限制

	public long getFpkid() {
		return fpkid;
	}

	public void setFpkid(long fpkid) {
		this.fpkid = fpkid;
	}

	public String getFmobile_no() {
		return fmobile_no;
	}

	public void setFmobile_no(String fmobile_no) {
		this.fmobile_no = fmobile_no;
	}

	public Long getFtmpl_id() {
		return ftmpl_id;
	}

	public void setFtmpl_id(Long ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	public Date getFblock_stime() {
		return fblock_stime;
	}

	public void setFblock_stime(Date fblock_stime) {
		this.fblock_stime = fblock_stime;
	}

	public long getFblock_timespan() {
		return fblock_timespan;
	}

	public void setFblock_timespan(long fblock_timespan) {
		this.fblock_timespan = fblock_timespan;
	}

}
