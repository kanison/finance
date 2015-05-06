package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;

/**
 * 财付通银行信息
 * 
 * @author zhl
 *
 */
public class BankInfo implements Serializable {

	private static final long serialVersionUID = -6034451080500907632L;

	private String bankType;
	private String bankCode;
	private String areaRequestPub;
	private String areaRequestPriv;
	private String subBankRequestPub;
	private String subBankRequestPriv;

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAreaRequestPub() {
		return areaRequestPub;
	}

	public void setAreaRequestPub(String areaRequestPub) {
		this.areaRequestPub = areaRequestPub;
	}

	public String getAreaRequestPriv() {
		return areaRequestPriv;
	}

	public void setAreaRequestPriv(String areaRequestPriv) {
		this.areaRequestPriv = areaRequestPriv;
	}

	public String getSubBankRequestPub() {
		return subBankRequestPub;
	}

	public void setSubBankRequestPub(String subBankRequestPub) {
		this.subBankRequestPub = subBankRequestPub;
	}

	public String getSubBankRequestPriv() {
		return subBankRequestPriv;
	}

	public void setSubBankRequestPriv(String subBankRequestPriv) {
		this.subBankRequestPriv = subBankRequestPriv;
	}

}
