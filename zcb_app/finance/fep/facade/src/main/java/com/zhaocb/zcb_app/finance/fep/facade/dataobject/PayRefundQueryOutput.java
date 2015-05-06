package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Set;

/**
 * 财付通退票查询输出参数
 * 
 * @author zhl
 *
 */
public class PayRefundQueryOutput extends PayRefundQueryProtocolDO implements
		Serializable {

	private static final long serialVersionUID = -7180408455417656154L;

	private String partner;// 商户号
	private String retcode;// 返回代码
	private String retmsg;// 返回信息
	private Integer cancel_count;// 退票笔数

	private Set<PayRefundQueryUsersDO> cancel_set;// 退票信息

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public Integer getCancel_count() {
		return cancel_count;
	}

	public void setCancel_count(Integer cancel_count) {
		this.cancel_count = cancel_count;
	}

	public Set<PayRefundQueryUsersDO> getCancel_set() {
		return cancel_set;
	}

	public void setCancel_set(Set<PayRefundQueryUsersDO> cancel_set) {
		this.cancel_set = cancel_set;
	}

}
