package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.app.utils.CommonUtil;

/**
 * ��Ӧ������֤����ˮ��sms_db.t_mvcode_log_yyyymm
 * @author Gu.Dongying
 */
public class MsgLogDO implements Serializable {
	
	private static final long serialVersionUID = 7687449869561217005L;

	private int fpkid;//��ˮ��ţ�����������
	
	private String fmobile_no;//�ֻ�����
	
	private Long ftmpl_id;//����ģ��ID
	
	private String fverify_code;//��֤��
	
	private String frela_key;//����key
	
	private String frela_info;//�����洢����Ϣ
	
	private int fchk_suc_times;//У��ɹ��Ĵ���
	
	private int fchk_err_times;//У��ʧ�ܵĴ���
	
	private String fclient_ip;//�û�IP
	
	private Date fsend_time;//�·�ʱ��
	
	private Date fcheck_time;//���һ����֤ʱ��
	
	private Date fexpired_time;//��֤�����ʱ��
	
	private Long fstandby1;//Ԥ���ֶ�
	
	private String fstandby2;//Ԥ���ֶ�
	
	private String tb_idx;//�ֱ�������

	public String getFclient_ip() {
		return fclient_ip;
	}

	public void setFclient_ip(String fclient_ip) {
		this.fclient_ip = fclient_ip;
	}

	public int getFpkid() {
		return fpkid;
	}

	public void setFpkid(int fpkid) {
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

	public String getFverify_code() {
		return fverify_code;
	}

	public void setFverify_code(String fverify_code) {
		this.fverify_code = fverify_code;
	}

	public String getFrela_key() {
		return frela_key;
	}

	public void setFrela_key(String frela_key) {
		this.frela_key = frela_key;
	}

	public String getFrela_info() {
		return frela_info;
	}

	public void setFrela_info(String frela_info) {
		this.frela_info = frela_info;
	}

	public int getFchk_suc_times() {
		return fchk_suc_times;
	}

	public void setFchk_suc_times(int fchk_suc_times) {
		this.fchk_suc_times = fchk_suc_times;
	}

	public int getFchk_err_times() {
		return fchk_err_times;
	}

	public void setFchk_err_times(int fchk_err_times) {
		this.fchk_err_times = fchk_err_times;
	}

	public Date getFsend_time() {
		return fsend_time;
	}

	public void setFsend_time(Date fsend_time) {
		this.fsend_time = fsend_time;
	}

	public Date getFcheck_time() {
		return fcheck_time;
	}

	public void setFcheck_time(Date fcheck_time) {
		this.fcheck_time = fcheck_time;
	}

	public Date getFexpired_time() {
		return fexpired_time;
	}

	public void setFexpired_time(Date fexpired_time) {
		this.fexpired_time = fexpired_time;
	}

	public Long getFstandby1() {
		return fstandby1;
	}

	public void setFstandby1(Long fstandby1) {
		this.fstandby1 = fstandby1;
	}

	public String getFstandby2() {
		return fstandby2;
	}

	public void setFstandby2(String fstandby2) {
		this.fstandby2 = fstandby2;
	}

	public String getTb_idx() {
		return tb_idx;
	}

	public void setTb_idx(String tb_idx) {
		this.tb_idx = tb_idx;
	}

	public void genDataTableIndex(){
		this.tb_idx = CommonUtil.getTbIndexByMonth();
	}
}
