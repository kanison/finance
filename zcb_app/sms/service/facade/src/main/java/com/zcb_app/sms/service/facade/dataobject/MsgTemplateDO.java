package com.zcb_app.sms.service.facade.dataobject;

public class MsgTemplateDO {
	
	private String id;//	短信模板ID，不能重复，在使用时需要人工分配再配置
	
	private int err_chk_times;//	验证错误的次数，超过次数则此验证码不再有效
	
	private int succ_chk_times;//	验证成功能的次数，用来控制是否可以多次验证，默认取1，即验证成功后不允行再次验证。
	
	private int expire_time;//	验证码过期时间，过期后验证码失效
	
	private boolean use_relakey;//	是否使用关联key进行验证
	
	private int auth_code_len;//	验证码长度
	
	private String tmpl_text;//	模板内容，以$变量名$，$auth_code$固定为验证码参数，在申请模板时可以定义参数，则验证码下发接口传入参数进行替换

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getErr_chk_times() {
		return err_chk_times;
	}

	public void setErr_chk_times(int err_chk_times) {
		this.err_chk_times = err_chk_times;
	}

	public int getSucc_chk_times() {
		return succ_chk_times;
	}

	public void setSucc_chk_times(int succ_chk_times) {
		this.succ_chk_times = succ_chk_times;
	}

	public int getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(int expire_time) {
		this.expire_time = expire_time;
	}

	public boolean isUse_relakey() {
		return use_relakey;
	}

	public void setUse_relakey(boolean use_relakey) {
		this.use_relakey = use_relakey;
	}

	public int getAuth_code_len() {
		return auth_code_len;
	}

	public void setAuth_code_len(int auth_code_len) {
		this.auth_code_len = auth_code_len;
	}

	public String getTmpl_text() {
		return tmpl_text;
	}

	public void setTmpl_text(String tmpl_text) {
		this.tmpl_text = tmpl_text;
	}


}
