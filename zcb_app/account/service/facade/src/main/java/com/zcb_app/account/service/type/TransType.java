package com.zcb_app.account.service.type;
 
/**
 * @author Tinffy Lee
 * ��������ֵ�����
 */
public class TransType {
	
	//��������trans type
	public static final int TT_C2C_TRANS = 1; //c2cת��
	
	//��������bookkeeping type
	public static final int BKT_PAY_IN = 1; //��
	public static final int BKT_PAY_OUT = 2; //��
	public static final int BKT_NOT_IN_OUT = 3; //�޳���
	
	//���ײ�������acction type
	public static final int ACT_C2C_TRSFR = 1; //C2Cת�� c2c transfer
	public static final int ACT_C2C_TRSFR_FREEZE = 7; //C2C�ⶳ��ת��	c2c transfer unfreeze
	public static final int ACT_C2C_UNFREEZE_TRSFR = 8; //C2C�ⶳ��ת��	c2c unfreeze transfer
	public static final int ACT_C2C_UNFREEZE_TRSFR_FREEZE = 10; //C2C�ⶳ��ת��,����ת�˽��	
}
