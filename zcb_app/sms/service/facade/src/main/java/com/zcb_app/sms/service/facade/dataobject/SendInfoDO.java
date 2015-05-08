package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.app.utils.CommonUtil;

/**
 * ��Ӧ���ŷ���Ƶ����Ϣ��¼��sms_db_xx.t_send_info_y
 * @author Gu.Dongying
 */
public class SendInfoDO implements Serializable {

	private static final long serialVersionUID = -6535631125635736315L;

	/**
	 * ��ˮ��ţ�����������
	 */
	private BigInteger fpkid;
	
	/**
	 * �ֻ�����
	 */
	private String fmobile_no;
	
	/**
	 * �û�IP
	 */
	private String fclient_ip;
	
	/**
	 * ����ʱ��
	 */
	private Date fsend_time;
	
	/**
	 * ����ģ��ID
	 */
	private BigInteger ftmpl_id;
	
	/**
	 * Ƶ�ʿ��Ƶ����ڣ�����Ϊ��λ
	 */
	private BigInteger timespan;

	/**
	 * �ֿ�������
	 */
	private String db_idx;
	
	/**
	 * �ֱ�������
	 */
	private String tb_idx;

	/**
	 * ��ȡ��ˮ��ţ�����������
	 * @return fpkid ��ˮ��ţ�����������
	 */
	public BigInteger getFpkid() {
		return fpkid;
	}

	/**
	 * ������ˮ��ţ�����������
	 * @param fpkid the ��ˮ��ţ����������� to set
	 */
	public void setFpkid(BigInteger fpkid) {
		this.fpkid = fpkid;
	}

	/**
	 * ��ȡ�ֻ�����
	 * @return fmobile_no �ֻ�����
	 */
	public String getFmobile_no() {
		return fmobile_no;
	}

	/**
	 * �����ֻ�����
	 * @param fmobile_no the �ֻ����� to set
	 */
	public void setFmobile_no(String fmobile_no) {
		this.fmobile_no = fmobile_no;
	}

	/**
	 * ��ȡ�û�IP
	 * @return fclient_ip �û�IP
	 */
	public String getFclient_ip() {
		return fclient_ip;
	}

	/**
	 * �����û�IP
	 * @param fclient_ip the �û�IP to set
	 */
	public void setFclient_ip(String fclient_ip) {
		this.fclient_ip = fclient_ip;
	}

	/**
	 * ��ȡ����ʱ��
	 * @return fsend_time ����ʱ��
	 */
	public Date getFsend_time() {
		return fsend_time;
	}

	/**
	 * ���÷���ʱ��
	 * @param fsend_time the ����ʱ�� to set
	 */
	public void setFsend_time(Date fsend_time) {
		this.fsend_time = fsend_time;
	}

	/**
	 * ��ȡ����ģ��ID
	 * @return ftmpl_id ����ģ��ID
	 */
	public BigInteger getFtmpl_id() {
		return ftmpl_id;
	}

	/**
	 * ���ö���ģ��ID
	 * @param ftmpl_id the ����ģ��ID to set
	 */
	public void setFtmpl_id(BigInteger ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	/**
	 * ��ȡ�ֿ�������
	 * @return db_idx �ֿ�������
	 */
	public String getDb_idx() {
		return db_idx;
	}

	/**
	 * ���÷ֿ�������
	 * @param db_idx the �ֿ������� to set
	 */
	public void setDb_idx(String db_idx) {
		this.db_idx = db_idx;
	}

	/**
	 * ��ȡ�ֱ�������
	 * @return tb_idx �ֱ�������
	 */
	public String getTb_idx() {
		return tb_idx;
	}

	/**
	 * ���÷ֱ�������
	 * @param tb_idx the �ֱ������� to set
	 */
	public void setTb_idx(String tb_idx) {
		this.tb_idx = tb_idx;
	}

	/**
	 * ��ȡƵ�ʿ��Ƶ����ڣ�����Ϊ��λ
	 * @return timespan Ƶ�ʿ��Ƶ����ڣ�����Ϊ��λ
	 */
	public BigInteger getTimespan() {
		return timespan;
	}

	/**
	 * ����Ƶ�ʿ��Ƶ����ڣ�����Ϊ��λ
	 * @param timespan the Ƶ�ʿ��Ƶ����ڣ�����Ϊ��λ to set
	 */
	public void setTimespan(BigInteger timespan) {
		this.timespan = timespan;
	}

	public void genDataTableIndex(){
		this.db_idx = CommonUtil.getDbIndexByMobile(this.fmobile_no);
		this.tb_idx = CommonUtil.getTbIndexByMobile(this.fmobile_no);		
	}
	
}
