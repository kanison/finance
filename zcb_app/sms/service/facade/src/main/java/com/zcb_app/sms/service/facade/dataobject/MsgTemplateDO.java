package com.zcb_app.sms.service.facade.dataobject;

public class MsgTemplateDO {
	
	private String id;//	����ģ��ID�������ظ�����ʹ��ʱ��Ҫ�˹�����������
	
	private int err_chk_times;//	��֤����Ĵ������������������֤�벻����Ч
	
	private int succ_chk_times;//	��֤�ɹ��ܵĴ��������������Ƿ���Զ����֤��Ĭ��ȡ1������֤�ɹ��������ٴ���֤��
	
	private int expire_time;//	��֤�����ʱ�䣬���ں���֤��ʧЧ
	
	private boolean use_relakey;//	�Ƿ�ʹ�ù���key������֤
	
	private int auth_code_len;//	��֤�볤��
	
	private String tmpl_text;//	ģ�����ݣ���$������$��$auth_code$�̶�Ϊ��֤�������������ģ��ʱ���Զ������������֤���·��ӿڴ�����������滻

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
