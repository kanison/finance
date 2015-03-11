package com.zhaocb.zcb_app.finance.service.facade.dataobject;

public class SpConfigDO implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7370065146778855896L;
	public static int LSTATE_VALID=1;
	public static int LSTATE_INVALID;
	public long uid;
	public String spid;
	public String spName;
	public int lstate; // 物理状态  1 有效;2 无效 该用户不可用
	public long authFlag; // 权限控制位
	public String signKey; // 商户签名key
	public String createTime;
	public String modifyTime;
	public String memo;
	
	public SpBizConfigDO spBizConfigDO;
	public AdvanceTypeConfigDO advanceTypeConfigDo;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	
	public int getLstate() {
		return lstate;
	}
	public void setLstate(int lstate) {
		this.lstate = lstate;
	}
	public long getAuthFlag() {
		return authFlag;
	}
	public void setAuthFlag(long authFlag) {
		this.authFlag = authFlag;
	}
	public String getSignKey() {
		return signKey;
	}
	public void setSignKey(String signKey) {
		this.signKey = signKey;
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
