package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 财付通付款申请输入参数
 * 
 * @author zhl
 *
 */
public class BatchDrawDO implements Serializable {

	private static final long serialVersionUID = -8603556484294592594L;

	public String op_user;// 操作员
	public String op_passwd;// 操作员密码
	public Date op_time;// 操作时间
	public String sp_id;// 商户机构ID
	public String package_id;// 包序列ID
	public int total_num;// 总笔数
	public int total_amt;// 总金额
	public String client_ip;// 客户端IP地址

	public Set<BatchDrawUsersDO> users_set;// 付款用户列表,一个批次支持最多2万笔记录

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getOp_user() {
		return op_user;
	}

	public void setOp_user(String op_user) {
		this.op_user = op_user;
	}

	public String getOp_passwd() {
		return op_passwd;
	}

	public void setOp_passwd(String op_passwd) {
		this.op_passwd = op_passwd;
	}

	public Date getOp_time() {
		return op_time;
	}

	public void setOp_time(Date op_time) {
		this.op_time = op_time;
	}

	public String getSp_id() {
		return sp_id;
	}

	public void setSp_id(String sp_id) {
		this.sp_id = sp_id;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(int total_amt) {
		this.total_amt = total_amt;
	}

	public Set<BatchDrawUsersDO> getUsers_set() {
		return users_set;
	}

	public void setUsers_set(Set<BatchDrawUsersDO> users_set) {
		this.users_set = users_set;
	}

}
