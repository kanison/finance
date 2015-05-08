package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.app.utils.CommonUtil;

/**
 * ��Ӧ������֤����Ϣ��:sms_db_xx.t_mvcode_info_y
 * @author Gu.Dongying
 *
 */
public class VerifyCodeInfoDO implements Serializable {
	
	private static final long serialVersionUID = -2215986650201574790L;

	/**
	 * �ֻ�����
	 */
	private String fmobile_no;
	
	/**
	 * ����ģ��id
	 */
	private BigInteger ftmpl_id;
	
	/**
	 * ��֤��
	 */
	private String fverify_code;
	
	/**
	 * ����key
	 */
	private String frela_key;
	
	/**
	 * �����洢����Ϣ
	 */
	private String frela_info;
	
	/**
	 * У��ɹ��Ĵ���
	 */
	private int fchk_suc_times;
	
	/**
	 * У��ʧ�ܵĴ���
	 */
	private int fchk_err_times;
	
	/**
	 * �û�IP
	 */
	private String fclient_ip;
	
	/**
	 * ��֤���·�ʱ��
	 */
	private Date fsend_time;
	
	/**
	 * ���һ����֤ʱ��
	 */
	private Date fcheck_time;
	
	/**
	 * ��֤�����ʱ��
	 */
	private Date fexpired_time;
	
	/**
	 * ��֤�����ʱ��(����Ϊ��λ)
	 */
	private int i_expired_time;
	
	/**
	 * Ԥ���ֶ�1
	 */
	private BigInteger fstandby1;
	
	/**
	 * Ԥ���ֶ�2
	 */
	private String fstandby2;
	
	/**
	 * �ֿ�������
	 */
	private String db_idx;
	
	/**
	 * �ֱ�������
	 */
	private String tb_idx;
	
	/**
	 * ������ʶ
	 */
	private boolean querylock = false;

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
	 * ��ȡ����ģ��id
	 * @return ftmpl_id ����ģ��id
	 */
	public BigInteger getFtmpl_id() {
		return ftmpl_id;
	}

	/**
	 * ���ö���ģ��id
	 * @param ftmpl_id the ����ģ��id to set
	 */
	public void setFtmpl_id(BigInteger ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	/**
	 * ��ȡ��֤��
	 * @return fverify_code ��֤��
	 */
	public String getFverify_code() {
		return fverify_code;
	}

	/**
	 * ������֤��
	 * @param fverify_code the ��֤�� to set
	 */
	public void setFverify_code(String fverify_code) {
		this.fverify_code = fverify_code;
	}

	/**
	 * ��ȡ����key
	 * @return frela_key ����key
	 */
	public String getFrela_key() {
		return frela_key;
	}

	/**
	 * ���ù���key
	 * @param frela_key the ����key to set
	 */
	public void setFrela_key(String frela_key) {
		this.frela_key = frela_key;
	}

	/**
	 * ��ȡ�����洢����Ϣ
	 * @return frela_info �����洢����Ϣ
	 */
	public String getFrela_info() {
		return frela_info;
	}

	/**
	 * ���ù����洢����Ϣ
	 * @param frela_info the �����洢����Ϣ to set
	 */
	public void setFrela_info(String frela_info) {
		this.frela_info = frela_info;
	}

	/**
	 * ��ȡУ��ɹ��Ĵ���
	 * @return fchk_suc_times У��ɹ��Ĵ���
	 */
	public int getFchk_suc_times() {
		return fchk_suc_times;
	}

	/**
	 * ����У��ɹ��Ĵ���
	 * @param fchk_suc_times the У��ɹ��Ĵ��� to set
	 */
	public void setFchk_suc_times(int fchk_suc_times) {
		this.fchk_suc_times = fchk_suc_times;
	}

	/**
	 * ��ȡУ��ʧ�ܵĴ���
	 * @return fchk_err_times У��ʧ�ܵĴ���
	 */
	public int getFchk_err_times() {
		return fchk_err_times;
	}

	/**
	 * ����У��ʧ�ܵĴ���
	 * @param fchk_err_times the У��ʧ�ܵĴ��� to set
	 */
	public void setFchk_err_times(int fchk_err_times) {
		this.fchk_err_times = fchk_err_times;
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
	 * ��ȡ��֤���·�ʱ��
	 * @return fsend_time ��֤���·�ʱ��
	 */
	public Date getFsend_time() {
		return fsend_time;
	}

	/**
	 * ������֤���·�ʱ��
	 * @param fsend_time the ��֤���·�ʱ�� to set
	 */
	public void setFsend_time(Date fsend_time) {
		this.fsend_time = fsend_time;
	}

	/**
	 * ��ȡ���һ����֤ʱ��
	 * @return fcheck_time ���һ����֤ʱ��
	 */
	public Date getFcheck_time() {
		return fcheck_time;
	}

	/**
	 * �������һ����֤ʱ��
	 * @param fcheck_time the ���һ����֤ʱ�� to set
	 */
	public void setFcheck_time(Date fcheck_time) {
		this.fcheck_time = fcheck_time;
	}

	/**
	 * ��ȡ��֤�����ʱ��
	 * @return fexpired_time ��֤�����ʱ��
	 */
	public Date getFexpired_time() {
		return fexpired_time;
	}

	/**
	 * ������֤�����ʱ��
	 * @param fexpired_time the ��֤�����ʱ�� to set
	 */
	public void setFexpired_time(Date fexpired_time) {
		this.fexpired_time = fexpired_time;
	}

	/**
	 * ��ȡ��֤�����ʱ��(����Ϊ��λ)
	 * @return i_expired_time ��֤�����ʱ��(����Ϊ��λ)
	 */
	public int getI_expired_time() {
		return i_expired_time;
	}

	/**
	 * ������֤�����ʱ��(����Ϊ��λ)
	 * @param i_expired_time the ��֤�����ʱ��(����Ϊ��λ) to set
	 */
	public void setI_expired_time(int i_expired_time) {
		this.i_expired_time = i_expired_time;
	}

	/**
	 * ��ȡԤ���ֶ�1
	 * @return fstandby1 Ԥ���ֶ�1
	 */
	public BigInteger getFstandby1() {
		return fstandby1;
	}

	/**
	 * ����Ԥ���ֶ�1
	 * @param fstandby1 the Ԥ���ֶ�1 to set
	 */
	public void setFstandby1(BigInteger fstandby1) {
		this.fstandby1 = fstandby1;
	}

	/**
	 * ��ȡԤ���ֶ�2
	 * @return fstandby2 Ԥ���ֶ�2
	 */
	public String getFstandby2() {
		return fstandby2;
	}

	/**
	 * ����Ԥ���ֶ�2
	 * @param fstandby2 the Ԥ���ֶ�2 to set
	 */
	public void setFstandby2(String fstandby2) {
		this.fstandby2 = fstandby2;
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
	 * ��ȡ������ʶ
	 * @return querylock ������ʶ
	 */
	public boolean isQuerylock() {
		return querylock;
	}

	/**
	 * ���ü�����ʶ
	 * @param querylock the ������ʶ to set
	 */
	public void setQuerylock(boolean querylock) {
		this.querylock = querylock;
	}

	public void genDataTableIndex(){
		this.db_idx = CommonUtil.getDbIndexByMobile(this.fmobile_no);
		this.tb_idx = CommonUtil.getTbIndexByMobile(this.fmobile_no);		
	}
}
