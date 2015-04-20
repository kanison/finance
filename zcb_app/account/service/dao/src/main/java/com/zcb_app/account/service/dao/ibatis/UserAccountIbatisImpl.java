package com.zcb_app.account.service.dao.ibatis;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.CommonUtil;
import com.app.utils.MoneyType;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.dao.type.AcctTransParams;
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
	private static final Log LOG = LogFactory.getLog(UserAccountIbatisImpl.class);
		
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

		insertTransVoucher(voobj);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAccountRollDO> queryUserAccountRolllist(
			UserAccountRollDO userAccountRollDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (List<UserAccountRollDO>) client.queryForList(
				"queryUserAccountRoll", userAccountRollDO);
	}

}
