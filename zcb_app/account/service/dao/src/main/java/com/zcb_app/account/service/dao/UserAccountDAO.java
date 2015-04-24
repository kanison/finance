package com.zcb_app.account.service.dao;

import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;

public interface UserAccountDAO {
	public void createUserAccount(UserAccountDO userAccountDO);

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO);

	/**
	 * ��ѯ�̻���Ϣ
	 * @param obj
	 * @return
	 */
	public SpUserInfo querySpUser(String spid);
	
	//public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO);

	/**
	 * ��ѯ����ƾ֤��Ϣ
	 * @param obj
	 * @return
	 */
	public TransVoucherDO queryTransVoucher(TransVoucherDO obj);
	
	public void c2cTransfer(AcctTransParams params);

	/**
	 * ����������
	 * 1��	���Ӷ�������鲽��6��7��װ��ͬһ��������ʵ�֣���Ϊ���Ӷ������ȻҪ��¼һ�����ᵥ��
	 * 2��	��¼���ᵥ
	 * 3��	��¼����ƾ֤��ˮ
	 * @param params
	 * @author Gu.Dongying 
	 * @date 2015��4��24�� ����11:49:08
	 */
	public void freezeUserBalance(AcctFreezeBalanParams params);
	
	//public UserAccountRollDO drawUserAccountFreeze(
	//		UserAccountRollDO userAccountRollDO);
	
	

	/**
	 * �ⶳ�ӿ�
	 * 
	 * @param userAccountRollDO
	 * @param draw
	 *            =trueʱ�ⶳ�����֣�draw=falseʱֱ�ӽⶳ�����ۼ��˻����
	 * @return
	 */
	//public UserAccountRollDO drawUserAccountUnfreeze(
	//		UserAccountRollDO userAccountRollDO, boolean draw);

	/**
	 * ֱ�����ֳɹ�
	 * 
	 * @param userAccountRollDO
	 * @return
	 */
	//public UserAccountRollDO drawUserAccountDirectSuccess(
	//		UserAccountRollDO userAccountRollDO);

	/**
	 * c2cת��
	 * 
	 * @param userAccountRollDO
	 */
	//public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
	//		UserAccountRollDO toUserAccountRollDO);

	//public List<UserAccountRollDO> queryUserAccountRolllist(
	//		UserAccountRollDO userAccountRollDO);
}
