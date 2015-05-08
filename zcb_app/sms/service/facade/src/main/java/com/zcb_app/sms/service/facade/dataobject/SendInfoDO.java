package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.app.utils.CommonUtil;

/**
 * 对应短信发送频率信息记录表：sms_db_xx.t_send_info_y
 * @author Gu.Dongying
 */
public class SendInfoDO implements Serializable {

	private static final long serialVersionUID = -6535631125635736315L;

	/**
	 * 流水编号（自增主键）
	 */
	private BigInteger fpkid;
	
	/**
	 * 手机号码
	 */
	private String fmobile_no;
	
	/**
	 * 用户IP
	 */
	private String fclient_ip;
	
	/**
	 * 发送时间
	 */
	private Date fsend_time;
	
	/**
	 * 短信模板ID
	 */
	private BigInteger ftmpl_id;
	
	/**
	 * 频率控制的周期，以秒为单位
	 */
	private BigInteger timespan;

	/**
	 * 分库名索引
	 */
	private String db_idx;
	
	/**
	 * 分表名索引
	 */
	private String tb_idx;

	/**
	 * 获取流水编号（自增主键）
	 * @return fpkid 流水编号（自增主键）
	 */
	public BigInteger getFpkid() {
		return fpkid;
	}

	/**
	 * 设置流水编号（自增主键）
	 * @param fpkid the 流水编号（自增主键） to set
	 */
	public void setFpkid(BigInteger fpkid) {
		this.fpkid = fpkid;
	}

	/**
	 * 获取手机号码
	 * @return fmobile_no 手机号码
	 */
	public String getFmobile_no() {
		return fmobile_no;
	}

	/**
	 * 设置手机号码
	 * @param fmobile_no the 手机号码 to set
	 */
	public void setFmobile_no(String fmobile_no) {
		this.fmobile_no = fmobile_no;
	}

	/**
	 * 获取用户IP
	 * @return fclient_ip 用户IP
	 */
	public String getFclient_ip() {
		return fclient_ip;
	}

	/**
	 * 设置用户IP
	 * @param fclient_ip the 用户IP to set
	 */
	public void setFclient_ip(String fclient_ip) {
		this.fclient_ip = fclient_ip;
	}

	/**
	 * 获取发送时间
	 * @return fsend_time 发送时间
	 */
	public Date getFsend_time() {
		return fsend_time;
	}

	/**
	 * 设置发送时间
	 * @param fsend_time the 发送时间 to set
	 */
	public void setFsend_time(Date fsend_time) {
		this.fsend_time = fsend_time;
	}

	/**
	 * 获取短信模板ID
	 * @return ftmpl_id 短信模板ID
	 */
	public BigInteger getFtmpl_id() {
		return ftmpl_id;
	}

	/**
	 * 设置短信模板ID
	 * @param ftmpl_id the 短信模板ID to set
	 */
	public void setFtmpl_id(BigInteger ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	/**
	 * 获取分库名索引
	 * @return db_idx 分库名索引
	 */
	public String getDb_idx() {
		return db_idx;
	}

	/**
	 * 设置分库名索引
	 * @param db_idx the 分库名索引 to set
	 */
	public void setDb_idx(String db_idx) {
		this.db_idx = db_idx;
	}

	/**
	 * 获取分表名索引
	 * @return tb_idx 分表名索引
	 */
	public String getTb_idx() {
		return tb_idx;
	}

	/**
	 * 设置分表名索引
	 * @param tb_idx the 分表名索引 to set
	 */
	public void setTb_idx(String tb_idx) {
		this.tb_idx = tb_idx;
	}

	/**
	 * 获取频率控制的周期，以秒为单位
	 * @return timespan 频率控制的周期，以秒为单位
	 */
	public BigInteger getTimespan() {
		return timespan;
	}

	/**
	 * 设置频率控制的周期，以秒为单位
	 * @param timespan the 频率控制的周期，以秒为单位 to set
	 */
	public void setTimespan(BigInteger timespan) {
		this.timespan = timespan;
	}

	public void genDataTableIndex(){
		this.db_idx = CommonUtil.getDbIndexByMobile(this.fmobile_no);
		this.tb_idx = CommonUtil.getTbIndexByMobile(this.fmobile_no);		
	}
	
}
