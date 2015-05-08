package com.zcb_app.account.service.dao;

import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.AcctUnFreezeBalanceParams;
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
	 * ����������<br>
	 * 1��	��ѯ�����û������˻�<br>
	 * 2��	�ж��û��Ŀ��ý��(�Ƕ������)�Ƿ��㹻���������������<br>
	 * 3��	���Ӷ�����<br>
	 * 4��	��¼���ᵥ<br>
	 * 5��	��¼����ƾ֤��ˮ
	 * @param params
	 * @author Gu.Dongying 
	 * @date 2015��4��24�� ����11:49:08
	 */
	public void freezeUserBalance(AcctFreezeBalanParams params);

	/**
	 * �ⶳ���ӿ�<br>
	 * 1�� ��ѯ�����û������˻�<br>
	 * 2�� �ж��û��Ķ������Ƿ��㹻���������������<br>
	 * 3�� ��ѯ���ᵥ��Ϣ����У��״̬�ͽ���Ƿ���ȷ<br>
	 * 4�� �޸Ķ��ᵥ�Ľⶳ����״̬<br>
	 * 5�� ��¼�û��˻���ˮ<br>
	 * 6�� ��¼����ƾ֤��ˮ
	 * @param unFreeze
	 * @Return void
	 * @author Gu.Dongying 
	 * @Date 2015��4��27�� ����11:41:13
	 */
	public void unfreezeUserBalance(AcctUnFreezeBalanceParams unFreeze);
	
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
