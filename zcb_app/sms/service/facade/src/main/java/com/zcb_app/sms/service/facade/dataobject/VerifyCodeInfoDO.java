package com.zcb_app.sms.service.facade.dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.app.utils.CommonUtil;

/**
 * 对应短信验证码信息表:sms_db_xx.t_mvcode_info_y
 * @author Gu.Dongying
 *
 */
public class VerifyCodeInfoDO implements Serializable {
	
	private static final long serialVersionUID = -2215986650201574790L;

	/**
	 * 手机号码
	 */
	private String fmobile_no;
	
	/**
	 * 短信模板id
	 */
	private BigInteger ftmpl_id;
	
	/**
	 * 验证码
	 */
	private String fverify_code;
	
	/**
	 * 关联key
	 */
	private String frela_key;
	
	/**
	 * 关联存储的信息
	 */
	private String frela_info;
	
	/**
	 * 校验成功的次数
	 */
	private int fchk_suc_times;
	
	/**
	 * 校验失败的次数
	 */
	private int fchk_err_times;
	
	/**
	 * 用户IP
	 */
	private String fclient_ip;
	
	/**
	 * 验证码下发时间
	 */
	private Date fsend_time;
	
	/**
	 * 最后一次验证时间
	 */
	private Date fcheck_time;
	
	/**
	 * 验证码过期时间
	 */
	private Date fexpired_time;
	
	/**
	 * 验证码过期时间(以秒为单位)
	 */
	private int i_expired_time;
	
	/**
	 * 预留字段1
	 */
	private BigInteger fstandby1;
	
	/**
	 * 预留字段2
	 */
	private String fstandby2;
	
	/**
	 * 分库名索引
	 */
	private String db_idx;
	
	/**
	 * 分表名索引
	 */
	private String tb_idx;
	
	/**
	 * 加锁标识
	 */
	private boolean querylock = false;

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
	 * 获取短信模板id
	 * @return ftmpl_id 短信模板id
	 */
	public BigInteger getFtmpl_id() {
		return ftmpl_id;
	}

	/**
	 * 设置短信模板id
	 * @param ftmpl_id the 短信模板id to set
	 */
	public void setFtmpl_id(BigInteger ftmpl_id) {
		this.ftmpl_id = ftmpl_id;
	}

	/**
	 * 获取验证码
	 * @return fverify_code 验证码
	 */
	public String getFverify_code() {
		return fverify_code;
	}

	/**
	 * 设置验证码
	 * @param fverify_code the 验证码 to set
	 */
	public void setFverify_code(String fverify_code) {
		this.fverify_code = fverify_code;
	}

	/**
	 * 获取关联key
	 * @return frela_key 关联key
	 */
	public String getFrela_key() {
		return frela_key;
	}

	/**
	 * 设置关联key
	 * @param frela_key the 关联key to set
	 */
	public void setFrela_key(String frela_key) {
		this.frela_key = frela_key;
	}

	/**
	 * 获取关联存储的信息
	 * @return frela_info 关联存储的信息
	 */
	public String getFrela_info() {
		return frela_info;
	}

	/**
	 * 设置关联存储的信息
	 * @param frela_info the 关联存储的信息 to set
	 */
	public void setFrela_info(String frela_info) {
		this.frela_info = frela_info;
	}

	/**
	 * 获取校验成功的次数
	 * @return fchk_suc_times 校验成功的次数
	 */
	public int getFchk_suc_times() {
		return fchk_suc_times;
	}

	/**
	 * 设置校验成功的次数
	 * @param fchk_suc_times the 校验成功的次数 to set
	 */
	public void setFchk_suc_times(int fchk_suc_times) {
		this.fchk_suc_times = fchk_suc_times;
	}

	/**
	 * 获取校验失败的次数
	 * @return fchk_err_times 校验失败的次数
	 */
	public int getFchk_err_times() {
		return fchk_err_times;
	}

	/**
	 * 设置校验失败的次数
	 * @param fchk_err_times the 校验失败的次数 to set
	 */
	public void setFchk_err_times(int fchk_err_times) {
		this.fchk_err_times = fchk_err_times;
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
	 * 获取验证码下发时间
	 * @return fsend_time 验证码下发时间
	 */
	public Date getFsend_time() {
		return fsend_time;
	}

	/**
	 * 设置验证码下发时间
	 * @param fsend_time the 验证码下发时间 to set
	 */
	public void setFsend_time(Date fsend_time) {
		this.fsend_time = fsend_time;
	}

	/**
	 * 获取最后一次验证时间
	 * @return fcheck_time 最后一次验证时间
	 */
	public Date getFcheck_time() {
		return fcheck_time;
	}

	/**
	 * 设置最后一次验证时间
	 * @param fcheck_time the 最后一次验证时间 to set
	 */
	public void setFcheck_time(Date fcheck_time) {
		this.fcheck_time = fcheck_time;
	}

	/**
	 * 获取验证码过期时间
	 * @return fexpired_time 验证码过期时间
	 */
	public Date getFexpired_time() {
		return fexpired_time;
	}

	/**
	 * 设置验证码过期时间
	 * @param fexpired_time the 验证码过期时间 to set
	 */
	public void setFexpired_time(Date fexpired_time) {
		this.fexpired_time = fexpired_time;
	}

	/**
	 * 获取验证码过期时间(以秒为单位)
	 * @return i_expired_time 验证码过期时间(以秒为单位)
	 */
	public int getI_expired_time() {
		return i_expired_time;
	}

	/**
	 * 设置验证码过期时间(以秒为单位)
	 * @param i_expired_time the 验证码过期时间(以秒为单位) to set
	 */
	public void setI_expired_time(int i_expired_time) {
		this.i_expired_time = i_expired_time;
	}

	/**
	 * 获取预留字段1
	 * @return fstandby1 预留字段1
	 */
	public BigInteger getFstandby1() {
		return fstandby1;
	}

	/**
	 * 设置预留字段1
	 * @param fstandby1 the 预留字段1 to set
	 */
	public void setFstandby1(BigInteger fstandby1) {
		this.fstandby1 = fstandby1;
	}

	/**
	 * 获取预留字段2
	 * @return fstandby2 预留字段2
	 */
	public String getFstandby2() {
		return fstandby2;
	}

	/**
	 * 设置预留字段2
	 * @param fstandby2 the 预留字段2 to set
	 */
	public void setFstandby2(String fstandby2) {
		this.fstandby2 = fstandby2;
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
	 * 获取加锁标识
	 * @return querylock 加锁标识
	 */
	public boolean isQuerylock() {
		return querylock;
	}

	/**
	 * 设置加锁标识
	 * @param querylock the 加锁标识 to set
	 */
	public void setQuerylock(boolean querylock) {
		this.querylock = querylock;
	}

	public void genDataTableIndex(){
		this.db_idx = CommonUtil.getDbIndexByMobile(this.fmobile_no);
		this.tb_idx = CommonUtil.getTbIndexByMobile(this.fmobile_no);		
	}
}
