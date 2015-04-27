package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Date;

public class BatchDrawQueryDO implements Serializable {

	private static final long serialVersionUID = 7175564168798990868L;

	public final String op_code = "1014";
	public final String op_name = "batch_draw_query";
	public String service_version = "1.2";

	public String op_user;// 操作员
	public String op_passwd;// 操作员密码
	public Date op_time;// 操作时间
	public String sp_id;// 商户机构ID
	public String package_id;// 包序列ID
	public String client_ip;// 客户端IP地址

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

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getOp_code() {
		return op_code;
	}

	public String getOp_name() {
		return op_name;
	}

	public String getService_version() {
		return service_version;
	}

}
