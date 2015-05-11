package com.zcb_app.account.service.dao.ibatis;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.CommonUtil;
import com.app.utils.MoneyType;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.AcctUnFreezeBalanceParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.exception.AccountServiceRetException;
import com.zcb_app.account.service.facade.dataobject.FreezeListDO;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zcb_app.account.service.type.FreezeType;
import com.zcb_app.account.service.type.TransType;

public class UserAccountIbatisImpl extends SqlMapClientDaoSupport implements
		UserAccountDAO {
		
	private void createUserAccountRoll(UserAccountRollDO userAccountRollDO) {
		if (userAccountRollDO == null
				|| CommonUtil.trimString(userAccountRollDO.getListid()) == null) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_NOT_EXIST, "ȱ�ٽ��׵���");
		}
		
		userAccountRollDO.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertUserAcctRoll", userAccountRollDO);
	}
	
	/**
	 * �����û��˻����
	 * @param userAccountDO
	 */
	private void updateUserAccountBalance(UserAccountDO userAccountDO)
	{
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateUserAcctBalance", userAccountDO);
		if (1 != effected_rows) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_EFFECTED_ROWS, "���µ�Ӱ��������Ϊ1");
		}
	}
	
	private void insertTransVoucher(TransVoucherDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertTransVoucher", obj);
	}
	
	/**
	 * ���涳�ᵥ��Ϣ
	 * @param obj
	 */
	private void insertFreezeList(FreezeListDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertFreezeList", obj);
	}
	
	/**
	 * ���¶��ᵥ��Ϣ
	 * @param obj
	 */
	private void updateFreezeList(FreezeListDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateFreezeList", obj);
		
		if (1 != effected_rows) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_EFFECTED_ROWS, "���µ�Ӱ��������Ϊ1");
		}
	}
	
	/**
	 * ���˻����ý���¼�˻���ˮ
	 * @param userAccountDO
	 * @param userAccountRollDO
	 * @return
	 */
	@Transactional
	private UserAccountDO subTractUserUnfreezeBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {

		// �˻�����������Ϊʣ����ý��
		BigDecimal ba = user.getBalance().subtract(
				user.getFreeze_balance());
		if (ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻�����");
		}
		
		//���û����������
		BigDecimal up_ba = user.getBalance().subtract(roll.getPay_amt());
		user.setBalance(up_ba);
		updateUserAccountBalance(user);
		
		//��¼�û��˻���ˮ	
		roll.setUid(user.getUid());
		roll.setUserid(user.getUserid());
		roll.setAcct_type(user.getAccttype());
		roll.setType(TransType.BKT_PAY_OUT);
		roll.setAcctid(user.getAcctid());
		roll.setBalance(user.getBalance());
		roll.setFreeze_balance(user.getFreeze_balance());
		createUserAccountRoll(roll);
		
		return user;
	}
	
	/**
	 * ���û��������¼�˻���ˮ
	 * @param user
	 * @param roll
	 * @return
	 */
	private UserAccountDO subTractUserFreezeBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {
		//�ж��˻��Ķ������Ƿ��㹻
		BigDecimal usr_frozen_ba = user.getFreeze_balance();
		if (usr_frozen_ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻�����");
		}
		
		//��ѯ���ᵥ		
		FreezeListDO flistObj = new FreezeListDO();
		flistObj.setListid(roll.getRela_list());
		flistObj.setUid(user.getUid());
		flistObj.setCur_type(roll.getCur_type());
		flistObj = queryFreezeList(flistObj);
		if (null == flistObj) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "��Ч�Ķ��ᵥ");
		}
		
		//��鶳�ᵥ״̬
		if (FreezeType.FS_FROZEN != flistObj.getState()
			&& FreezeType.FS_PARTLY_UFREEZE != flistObj.getState()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_FREEZE_ERROR, "���ᵥ״̬����ȷ");
		}
		
		//���õĶ�����Ϊ�ܶ�����-�ѽⶳ���
		BigDecimal list_frozen_ba = flistObj.getFreeze_amt().subtract(flistObj.getUnfreeze_amt());
		if (list_frozen_ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_NOT_ENOUGH, "�������");
		}
		
		//���ö��ᵥ�ⶳ����״̬
		BigDecimal ufamt = flistObj.getUnfreeze_amt();
		ufamt = ufamt.add(roll.getPay_amt());
		if (flistObj.getFreeze_amt().compareTo(ufamt) == 0)
			flistObj.setState(FreezeType.FS_ALL_UFREEZE);
		else
			flistObj.setState(FreezeType.FS_PARTLY_UFREEZE);
		
		flistObj.setUnfreeze_amt(ufamt);
		//���¶��ᵥ
		updateFreezeList(flistObj);
		
		//���û����������
		BigDecimal up_ba = user.getBalance().subtract(roll.getPay_amt());
		user.setBalance(up_ba);
		usr_frozen_ba = user.getFreeze_balance().subtract(roll.getPay_amt());
		user.setFreeze_balance(usr_frozen_ba);
		updateUserAccountBalance(user);
		
		//��¼�û��˻���ˮ	
		roll.setUid(user.getUid());
		roll.setUserid(user.getUserid());
		roll.setAcct_type(user.getAccttype());
		roll.setType(TransType.BKT_PAY_OUT);
		roll.setAcctid(user.getAcctid());
		roll.setBalance(user.getBalance());
		roll.setFreeze_balance(user.getFreeze_balance());
		createUserAccountRoll(roll);
		
		return user;
	}
	
	@Transactional
	private UserAccountDO subUserBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {
		//����ǽⶳ��ת����ʹ���û����������ʹ���û�δ������
		if (TransType.ACT_C2C_UNFREEZE_TRSFR == roll.getAction_type()) {
			return subTractUserFreezeBalance(user, roll);
		} else {
			return subTractUserUnfreezeBalance(user, roll);
		}			
	}
	
	/**
	 * �����˻����
	 * @param user
	 * @param roll
	 * @return
	 */
	@Transactional
	private UserAccountDO addUserBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {
		//��������Ϊ0������Ҫ���붳�ᵥ��
		Boolean bfreeze = (roll.getPayfreeze_amt().compareTo(MoneyType.ZERO) > 0);
		if( true == bfreeze
			&& null == CommonUtil.trimString(roll.getRela_list())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "ȱ�ٶ��ᵥ��");
		}
		
		//���ӻ����������Ͷ�����
		user.setBalance(user.getBalance().add(roll.getPay_amt()));
		if (bfreeze) {
			user.setFreeze_balance(user.getFreeze_balance().add(
					roll.getPayfreeze_amt()));
		}
		updateUserAccountBalance(user);
		
		//��¼�û��˻���ˮ		
		roll.setUid(user.getUid());
		roll.setUserid(user.getUserid());
		roll.setAcct_type(user.getAccttype());
		roll.setType(TransType.BKT_PAY_IN);
		roll.setAcctid(user.getAcctid());
		roll.setBalance(user.getBalance());
		roll.setFreeze_balance(user.getFreeze_balance());		
		createUserAccountRoll(roll);
		
		//����ж������Ҫ��¼���ᵥ
		if (bfreeze) {
			FreezeListDO flistObj = new FreezeListDO();
			//�û���Ϣ
			flistObj.setUid(user.getUid());
			flistObj.setUserid(user.getUserid());
			//������Ϣ�����ᵥ��Ϊ��ˮ�Ĺ�������
			flistObj.setListid(roll.getRela_list());
			flistObj.setFreeze_amt(roll.getPayfreeze_amt());
			flistObj.setCur_type(roll.getCur_type());
			flistObj.setAction_type(roll.getAction_type());
			flistObj.setRela_list(roll.getListid());
			flistObj.setReason(FreezeType.FR_TRANS_FREEZE);	
			flistObj.setMemo(roll.getMemo());
			flistObj.setIp(roll.getClient_ip());
			
			insertFreezeList(flistObj);
		}
		
		return user;
	}
	
	private void checkTransVoucher(AcctTransParams params)
	{
		TransVoucherDO tmptv = new TransVoucherDO();
		tmptv.setListid(params.getListid());
		tmptv.setAction_type(params.getAction_type());
		TransVoucherDO tv = queryTransVoucher(tmptv);
		if (null == tv)
			return;
		
		//���������ȽϹؼ������Ƿ�һ��
		if (tv.getFrom_uid() != params.getFrom_uid())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "From_uid��һ��");
		
		//LOG.debug("tv fromuserid="+tv.getFrom_userid()+",params.fromuserid="+params.getFrom_userid());
		
		if (!tv.getFrom_userid().equals(params.getFrom_userid()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "From_userid��һ��");
					
		if (tv.getTo_uid() != params.getTo_uid())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "To_uid��һ��");
		
		if (!tv.getTo_userid().equals(params.getTo_userid()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "To_userid��һ��");
		
		if (tv.getCur_type() != params.getCur_type())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "���ױ��ֲ�һ��");
		
		if (!tv.getTrans_amt().equals(params.getTrans_amt()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "���׽�һ��");
		
		if (!tv.getFrozen_amt().equals(params.getFrozen_amt()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "�����һ��");
		
		if (!tv.getRela_list().equals(params.getRela_list()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "�����ĵ��Ų�һ��");
		
		//����һ�£����ض�����������룬ҵ����ݴ˴����봦��������ظ��ύ������
		throw new AccountServiceRetException(
				AccountServiceRetException.ERR_REENTY_OK, "�����Ѿ��ɹ�");
	}
	
	public SpUserInfo querySpUser(String spid)
	{
		SpUserInfo spuser = new SpUserInfo();
		spuser.setSpid(spid);
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (SpUserInfo) client.queryForObject("querySpInfo",
				spuser);
	}
		
	/**
	 * ���������ֽ��˻�
	 */
	public void createUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("createUserAccount", userAccountDO);
	}
	
	/**
	 * ��ѯ�������ڽ��˻�
	 */
	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (UserAccountDO) client.queryForObject("queryUserAccount",
				userAccountDO);
	}
		
	/**
	 * ��ѯ����ƾ֤��Ϣ
	 * @param obj
	 * @return
	 */
	public TransVoucherDO queryTransVoucher(TransVoucherDO obj)
	{
		obj.genDataTableIndex();
		obj.genVoucher();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (TransVoucherDO) client.queryForObject("queryTransVoucher",
				obj);
	}
	
	/**
	 * ��ѯ���ᵥ��Ϣ
	 * @param obj
	 * @return
	 */
	public FreezeListDO queryFreezeList(FreezeListDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (FreezeListDO)client.queryForObject("queryFreezeList", obj);
	}
	

	@Transactional
	public void c2cTransfer(AcctTransParams params)
	{
		//��ѯƾ֤���Ƿ���ڣ�������ڼ��ؼ������Ƿ�һ��
		checkTransVoucher(params);
				
		UserAccountDO fromuser = new UserAccountDO();
		fromuser.setUid(params.getFrom_uid());
		fromuser.setCurtype(params.getCur_type());
		fromuser.setAccttype(params.getFrom_acct_type());
		fromuser.setQuerylock(true);
		
		UserAccountDO touser = new UserAccountDO();
		touser.setUid(params.getTo_uid());
		touser.setCurtype(params.getCur_type());
		touser.setAccttype(params.getTo_acct_type());
		touser.setQuerylock(true);
		
		//�������˻���Ϊ��ֹ��������uid��С����˳�����
		if (fromuser.getUid() > touser.getUid()) {
			touser = queryUserAccount(touser);		
			fromuser = queryUserAccount(fromuser);
		} else {
			fromuser = queryUserAccount(fromuser);
			touser = queryUserAccount(touser);
		}
		if (null == fromuser || null == touser) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_USER_ACCT_NOT_EXSIT, "�����˻�������");
		}
		
		//��Ǯ�˻���
		UserAccountRollDO fromuser_roll = new UserAccountRollDO();
		//���ý�����Ϣ
		fromuser_roll.setCur_type(params.getCur_type());
		fromuser_roll.setPay_amt(params.getTrans_amt());
		fromuser_roll.setPayfreeze_amt(params.getFrozen_amt());
		fromuser_roll.setAction_type(params.getAction_type());
		fromuser_roll.setTrade_acc_time(params.getTrade_acc_time());
		fromuser_roll.setListid(params.getListid());
		fromuser_roll.setRela_list(params.getRela_list());
		fromuser_roll.setMemo(params.getMemo());
		fromuser_roll.setClient_ip(params.getClient_ip());
		//����Ŀ���˻�
		fromuser_roll.setTo_uid(touser.getUid());
		fromuser_roll.setTo_userid(touser.getUserid());		
		subUserBalance(fromuser, fromuser_roll);
		
		//��Ǯ�˻���
		UserAccountRollDO touser_roll = new UserAccountRollDO();
		touser_roll.setCur_type(params.getCur_type());
		touser_roll.setPay_amt(params.getTrans_amt());
		touser_roll.setPayfreeze_amt(params.getFrozen_amt());
		touser_roll.setAction_type(params.getAction_type());
		touser_roll.setTrade_acc_time(params.getTrade_acc_time());
		touser_roll.setListid(params.getListid());
		touser_roll.setRela_list(params.getRela_list());
		touser_roll.setMemo(params.getMemo());
		touser_roll.setClient_ip(params.getClient_ip());
		//����Ŀ���˻�
		touser_roll.setTo_uid(fromuser.getUid());
		touser_roll.setTo_userid(fromuser.getUserid());
		addUserBalance(touser, touser_roll);
		
		//��¼ƾ֤��
		TransVoucherDO voobj = new TransVoucherDO();
		voobj.setVoucher(params.getListid());
		voobj.setListid(params.getListid());
		voobj.setCur_type(params.getCur_type());
		voobj.setTrans_amt(params.getTrans_amt());
		voobj.setFrozen_amt(params.getFrozen_amt());
		voobj.setAction_type(params.getAction_type());
		voobj.setTrans_type(params.getTrans_type());
		voobj.setFrom_uid(fromuser.getUid());
		voobj.setFrom_userid(fromuser.getUserid());
		voobj.setTo_uid(touser.getUid());
		voobj.setTo_userid(touser.getUserid());
		voobj.setTrade_acc_time(params.getTrade_acc_time());
		voobj.setRela_list(params.getRela_list());
		voobj.setMemo(params.getMemo());
		//����ƾ֤����Faction_type��Ϊǰ׺(4λ����)+Flistid
		voobj.genVoucher();
		insertTransVoucher(voobj);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAccountRollDO> queryUserAccountRolllist(
			UserAccountRollDO userAccountRollDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (List<UserAccountRollDO>) client.queryForList(
				"queryUserAccountRoll", userAccountRollDO);
	}

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
	@Transactional
	public void freezeUserBalance(AcctFreezeBalanParams params){
		UserAccountDO user = new UserAccountDO();
		user.setUid(params.getUid());
		user.setAccttype(params.getAcct_type());
		user.setCurtype(params.getCur_type());
		user.setQuerylock(true);
		//��ѯ�����û������˻�
		user = queryUserAccount(user);
		//�ж��û��Ŀ��ý��(�Ƕ������)�Ƿ��㹻���������������(�ܶ��ȥ�Ѷ����������ڵ��ڶ�����)
		if(params.getFreeze_amt().compareTo(user.getBalance().subtract(user.getFreeze_balance())) > -1){
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻����㣡");
		}
		
		UserAccountRollDO acctRoll = new UserAccountRollDO();
		acctRoll.setPay_amt(params.getFreeze_amt());
		acctRoll.setPayfreeze_amt(params.getFreeze_amt());
		
		//���Ӷ�����
		//user.setBalance(user.getBalance().subtract(params.getFreeze_amt()));
		user.setFreeze_balance(user.getFreeze_balance().add(params.getFreeze_amt()));
		updateUserAccountBalance(user);
		
		//��¼���ᵥ
		FreezeListDO flistObj = new FreezeListDO();
		flistObj.setUid(user.getUid());
		flistObj.setUserid(user.getUserid());
		flistObj.setListid(params.getListid());
		flistObj.setFreeze_amt(params.getFreeze_amt());
		flistObj.setCur_type(params.getCur_type());
		flistObj.setAction_type(params.getAction_type());
		flistObj.setRela_list(params.getListid());
		flistObj.setReason(params.getFreeze_reason());	
		flistObj.setMemo(params.getMemo());
		flistObj.setIp(params.getClient_ip());
		insertFreezeList(flistObj);
		
		//��¼�û��˻���ˮ
		acctRoll.setUid(params.getUid());
		acctRoll.setListid(params.getListid());
		acctRoll.setUserid(params.getUserid());
		acctRoll.setTo_uid(params.getUid());
		acctRoll.setTo_userid(params.getUserid());
		acctRoll.setCur_type(params.getCur_type());
		acctRoll.setAcct_type(params.getAcct_type()); 
		acctRoll.setType(TransType.BKT_NOT_IN_OUT);
		acctRoll.setAcctid(user.getAcctid());
		acctRoll.setBalance(user.getBalance());
		acctRoll.setFreeze_balance(user.getFreeze_balance());
		acctRoll.setAction_type(params.getAction_type());
		acctRoll.setTrans_type(params.getTrans_type());
		acctRoll.setTrade_acc_time(params.getTrade_acc_time());
		acctRoll.setMemo(params.getMemo());
		createUserAccountRoll(acctRoll);
		
		
		//��¼����ƾ֤��ˮ
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setVoucher(params.getListid());
		voucher.setListid(params.getListid());
		//���ݿ�����ʱ����Ϊ""��
		//voucher.setReq_no(""); 
		voucher.setCur_type(params.getCur_type());
		voucher.setTrans_amt(params.getFreeze_amt());
		voucher.setFrozen_amt(params.getFreeze_amt());
		voucher.setTrans_type(params.getTrans_type());
		voucher.setAction_type(params.getAction_type());
		voucher.setFrom_userid(params.getUserid());
		voucher.setFrom_uid(params.getUid());
		voucher.setTo_userid(params.getUserid());
		voucher.setTo_uid(params.getUid());
		voucher.setTrade_acc_time(params.getTrade_acc_time());
		voucher.setMemo(params.getMemo());
		//����ƾ֤����Faction_type��Ϊǰ׺(4λ����)+Flistid
		voucher.genVoucher();
		insertTransVoucher(voucher);
	}
	
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
	@Transactional
	public void unfreezeUserBalance(AcctUnFreezeBalanceParams unFreeze){
		UserAccountDO user = new UserAccountDO();
		user.setUid(unFreeze.getUid());
		user.setAccttype(unFreeze.getAcct_type());
		user.setCurtype(unFreeze.getCur_type());
		user.setQuerylock(true);
		//��ѯ�����û������˻�
		user = queryUserAccount(user);
		//�ж��û��Ķ�������Ƿ��㹻���������������
		if(unFreeze.getUnfreeze_amt().compareTo(user.getFreeze_balance()) == 1){
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_ERROR, "�˻��������㣡");
		}
		
		//��ѯ���ᵥ��Ϣ����У��״̬�ͽ���Ƿ���ȷ
		FreezeListDO freezeList = new FreezeListDO();
		freezeList.setListid(unFreeze.getFreeze_list());
		freezeList.setUid(unFreeze.getUid());
		freezeList.setCur_type(unFreeze.getCur_type());
		freezeList = queryFreezeList(freezeList);
		//�����ᵥ��Ϣ״̬Ϊȫ�����ᣬ���׳��쳣
		if(freezeList.getState() == FreezeType.FS_ALL_UFREEZE){
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_STATUS_ERROR, "ԭ���ᵥ״̬����");
		}
		int freezeStatus = FreezeType.FS_FROZEN;
		//�ж϶��ᵥ��������-�ⶳ���>=��ⶳ��� 
		switch (freezeList.getFreeze_amt().subtract(freezeList.getUnfreeze_amt())
				.compareTo(unFreeze.getUnfreeze_amt())) {
		case 0:
			freezeStatus = FreezeType.FS_ALL_UFREEZE;
			break;
		case 1:
			freezeStatus = FreezeType.FS_PARTLY_UFREEZE;
			break;
		default:
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_ERROR, "������");
		}
		UserAccountRollDO acctRoll = new UserAccountRollDO();
		acctRoll.setPay_amt(unFreeze.getUnfreeze_amt());
		acctRoll.setPayfreeze_amt(new BigDecimal(0));
		//�޸Ķ��ᵥ�Ľⶳ����״̬
		freezeList.setUnfreeze_amt(freezeList.getUnfreeze_amt().add(unFreeze.getUnfreeze_amt()));
		freezeList.setState(freezeStatus);
		updateFreezeList(freezeList);
		
		//�޸��û��Ķ�����
		//user.setBalance(user.getBalance().add(unFreeze.getUnfreeze_amt()));
		user.setFreeze_balance(user.getFreeze_balance().subtract(unFreeze.getUnfreeze_amt()));
		updateUserAccountBalance(user);
		
		//��¼�û��˻���ˮ
		acctRoll.setUid(unFreeze.getUid());
		acctRoll.setListid(unFreeze.getListid());
		acctRoll.setUserid(unFreeze.getUserid());
		acctRoll.setTo_uid(unFreeze.getUid());
		acctRoll.setTo_userid(unFreeze.getUserid());
		acctRoll.setCur_type(unFreeze.getCur_type());
		acctRoll.setAcct_type(unFreeze.getAcct_type()); 
		acctRoll.setType(TransType.BKT_NOT_IN_OUT);
		acctRoll.setAcctid(user.getAcctid());
		acctRoll.setBalance(user.getBalance());
		acctRoll.setFreeze_balance(user.getFreeze_balance());
		acctRoll.setAction_type(unFreeze.getAction_type());
		acctRoll.setTrans_type(unFreeze.getTrans_type());
		acctRoll.setTrade_acc_time(unFreeze.getTrade_acc_time());
		acctRoll.setMemo(unFreeze.getMemo());
		createUserAccountRoll(acctRoll);
		
		//��¼����ƾ֤��ˮ
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setVoucher(unFreeze.getListid());
		voucher.setListid(unFreeze.getListid());
		voucher.setCur_type(unFreeze.getCur_type());
		voucher.setTrans_amt(unFreeze.getUnfreeze_amt());
		voucher.setFrozen_amt(new BigDecimal(0));
		voucher.setTrans_type(unFreeze.getTrans_type());
		voucher.setAction_type(unFreeze.getAction_type());
		voucher.setFrom_userid(unFreeze.getUserid());
		voucher.setFrom_uid(unFreeze.getUid());
		voucher.setTo_userid(unFreeze.getUserid());
		voucher.setTo_uid(unFreeze.getUid());
		voucher.setTrade_acc_time(unFreeze.getTrade_acc_time());
		voucher.setMemo(unFreeze.getMemo());
		//����ƾ֤����Faction_type��Ϊǰ׺(4λ����)+Flistid
		voucher.genVoucher();
		insertTransVoucher(voucher);
	}
}
