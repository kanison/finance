package com.zcb_app.account.service.dao;

import java.util.List;

import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;

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
