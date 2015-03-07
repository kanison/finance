package com.zhaocb.zcb_app.finance.service.facade.dataobject;

public class SpConfigDO implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7370065146778855896L;
	public String uid;
	public String spid;
	public String sp_name;
	public String lstate; // 物理状态  1 有效;2 无效 该用户不可用
	public long auth_flag; // 权限控制位
	public String sign_key;
	public String createTime;
	public String modifyTime;
	public String memo;
	
	public SpBizConfigDO spBizConfigDO;
	public AdvanceTypeConfigDO advanceTypeConfigDo;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getSp_name() {
		return sp_name;
	}
	public void setSp_name(String sp_name) {
		this.sp_name = sp_name;
	}
	public String getLstate() {
		return lstate;
	}
	public void setLstate(String lstate) {
		this.lstate = lstate;
	}
	
	public long getAuth_flag() {
		return auth_flag;
	}
	public void setAuth_flag(long auth_flag) {
		this.auth_flag = auth_flag;
	}
	public String getSign_key() {
		return sign_key;
	}
	public void setSign_key(String sign_key) {
		this.sign_key = sign_key;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public SpBizConfigDO getSpBizConfigDO() {
		return spBizConfigDO;
	}
	public void setSpBizConfigDO(SpBizConfigDO spBizConfigDO) {
		this.spBizConfigDO = spBizConfigDO;
	}
	public AdvanceTypeConfigDO getAdvanceTypeConfigDo() {
		return advanceTypeConfigDo;
	}
	public void setAdvanceTypeConfigDo(AdvanceTypeConfigDO advanceTypeConfigDo) {
		this.advanceTypeConfigDo = advanceTypeConfigDo;
	}
	
}
