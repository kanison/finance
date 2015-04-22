package com.zhaocb.common.aop.aspect.checklogin;

/**
 * 1.���ҳ���cookie��qlskey 2.qlskey��ΪsessionKey��ȡsession
 * 3.ȡ������uin��ҳ���qluinһ�¾��жϵ�¼��Ч,
 */
public class LoginSessionData {

	private int uinType; // �û��ĵ�¼����, 1 �C qq, 2 �C email, 3 �C �ֻ�, ��
	private String uin; // ��¼�˺�
	private long uid; // ���û���֧��ͨ�ڲ�ID
	private String loginTime; // �û���¼ʱ�Ļ���ʱ���
	private String loginIp; // �û���¼��ip

	public static final int LOGIN_TYPE_CFT = 1;

	/**
	 * <p>
	 * <ul>
	 * <li>#define CFTLOGIN 0x1 //�Ƹ�ͨ��ȫ��½</li>
	 * <li>#define QQLOGIN 0x2 //�ǲƸ�ͨ�ʺ�QQ��½</li>
	 * <li>#define PRELOGIN 0x4 //������½</li>
	 * </ul>
	 * </p>
	 * ���ǣ���ʽ�Ļ�ѡ��CFTLOGIN|PRELOGIN������һ�����>0����Ϊ�й���½<br>
	 * Ŀǰ����ͨ�����ã���2����½��ϵ��˵���˾�����������,��һ��Ȩ���ܿ�<br>
	 * �������2���ľ���2���ģ�û����ľ�һ��<br>
	 * En,��������ʽ��Ҫ������֤���̬��<br>
	 * ���ֻ��0x2�Ļ�����һ���ǲƸ�ͨ�û�,������ʾqq��¼������û��ͨ�Ƹ�ͨ�ǰ�<br>
	 */
	private int status; // ��¼״̬, 1 -- ��Ч��¼״̬������ �C ��Ч

	public int getUinType() {
		return uinType;
	}

	public void setUinType(int uinType) {
		this.uinType = uinType;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
}
