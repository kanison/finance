package com.zcb_app.account.service.type;

/**
 * ���ᵥ����ص������ֵ�ֵ
 * @author Tinffy Lee
 *
 */
public class FreezeType {
	//����ԭ��
	public static final int FR_TRANS_FREEZE = 1; //���׶���
	public static final int FR_CSO_FREEZE = 2; //�ͷ���Customer Service officer������
	public static final int FR_RISK_FREEZE = 3; //��ض���
	public static final int FR_FETCH_FREEZE = 4; //���ֶ���
	
	//���ᵥ״̬
	public static final int FS_INIT = 1; //��ʼ
	public static final int FS_FROZEN = 2; //����
	public static final int FS_PARTLY_UFREEZE = 3; //���ֽⶳpartly unfreeze
	public static final int FS_ALL_UFREEZE = 4; //ȫ���ⶳ all unfreeze
}
