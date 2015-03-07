package com.zcb_app.account.service.dao.ibatis;

import java.util.Date;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.CommonUtil;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.exception.AccountServiceRetException;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;

public class UserAccountIbatisImpl extends SqlMapClientDaoSupport implements
		UserAccountDAO {

	private void createUserAccountRoll(UserAccountRollDO userAccountRollDO) {
		if (userAccountRollDO == null
				|| CommonUtil.trimString(userAccountRollDO.getListid()) == null) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_NOT_EXIST, "ȱ�ٽ��׵���");
		}
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertUserAccountRoll", userAccountRollDO);

	}

	@Transactional
	public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO) {

		UserAccountDO userAccountDO = new UserAccountDO();
		userAccountDO.setUid(userAccountRollDO.getUid());
		userAccountDO.setAccount_type(userAccountRollDO.getAccount_type());
		userAccountDO.setCurtype(userAccountRollDO.getCurtype());
		userAccountDO = queryUserAccount(userAccountDO);

		userAccountRollDO.setBalance(userAccountDO.getBalance().add(
				userAccountRollDO.getPaynum()));
		userAccountRollDO.setFreeze_balance(userAccountDO.getFreeze_balance());// �������
		userAccountRollDO.setType(UserAccountRollDO.TYPE_SAVE);
		userAccountRollDO.setAcctid(userAccountDO.getAcctid());
		userAccountRollDO.setTrade_time(CommonUtil.formatDate(new Date(),
				"yyyy-MM-dd HH:mm:ss"));

		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateUserAccount", userAccountRollDO);

		createUserAccountRoll(userAccountRollDO);
		return userAccountRollDO;

	}

	public void createUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("createUserAccount", userAccountDO);
	}

	@Transactional
	public UserAccountRollDO drawUserAccountDirectSuccess(
			UserAccountRollDO userAccountRollDO) {
		UserAccountDO userAccountDO = new UserAccountDO();
		userAccountDO.setUid(userAccountRollDO.getUid());
		userAccountDO.setAccount_type(userAccountRollDO.getAccount_type());
		userAccountDO.setCurtype(userAccountRollDO.getCurtype());
		userAccountDO = queryUserAccount(userAccountDO);

		// �˻�����������Ϊʣ����ý��
		if ((userAccountDO.getBalance().subtract(userAccountDO
				.getFreeze_balance())).compareTo(userAccountRollDO.getPaynum()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻�����");
		}

		userAccountRollDO.setBalance(userAccountDO.getBalance().subtract(
				userAccountRollDO.getPaynum()));
		userAccountRollDO.setFreeze_balance(userAccountDO.getFreeze_balance());// �������
		userAccountRollDO.setType(UserAccountRollDO.TYPE_DRAW);
		userAccountRollDO.setAcctid(userAccountDO.getAcctid());
		userAccountRollDO.setTrade_time(CommonUtil.formatDate(new Date(),
				"yyyy-MM-dd HH:mm:ss"));

		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateUserAccount", userAccountRollDO);

		createUserAccountRoll(userAccountRollDO);
		return userAccountRollDO;
	}

	@Transactional
	public UserAccountRollDO drawUserAccountFreeze(
			UserAccountRollDO userAccountRollDO) {
		UserAccountDO userAccountDO = new UserAccountDO();
		userAccountDO.setUid(userAccountRollDO.getUid());
		userAccountDO.setAccount_type(userAccountRollDO.getAccount_type());
		userAccountDO.setCurtype(userAccountRollDO.getCurtype());
		userAccountDO = queryUserAccount(userAccountDO);

		// �˻�����������Ϊʣ����ý��
		if ((userAccountDO.getBalance().subtract(userAccountDO
				.getFreeze_balance())).compareTo(userAccountRollDO
				.getFreezenum()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻�����");
		}

		userAccountRollDO.setBalance(userAccountDO.getBalance());// �˻�����
		userAccountRollDO.setFreeze_balance(userAccountDO.getFreeze_balance()
				.add(userAccountRollDO.getFreezenum()));
		userAccountRollDO.setType(UserAccountRollDO.TYPE_FREEZE);
		userAccountRollDO.setAcctid(userAccountDO.getAcctid());
		userAccountRollDO.setTrade_time(CommonUtil.formatDate(new Date(),
				"yyyy-MM-dd HH:mm:ss"));

		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateUserAccount", userAccountRollDO);

		createUserAccountRoll(userAccountRollDO);
		return userAccountRollDO;
	}

	@Transactional
	public UserAccountRollDO drawUserAccountUnfreeze(
			UserAccountRollDO userAccountRollDO, boolean draw) {
		UserAccountDO userAccountDO = new UserAccountDO();
		userAccountDO.setUid(userAccountRollDO.getUid());
		userAccountDO.setAccount_type(userAccountRollDO.getAccount_type());
		userAccountDO.setCurtype(userAccountRollDO.getCurtype());
		userAccountDO = queryUserAccount(userAccountDO);

		// �˻�����Ƿ�С�ڶ��������˻��Ƿ��쳣
		if (userAccountDO.getBalance().compareTo(
				userAccountDO.getFreeze_balance()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "�˻�����");
		}
		if (userAccountDO.getFreeze_balance().compareTo(
				userAccountRollDO.getFreezenum()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_NOT_ENOUGH,
					"�������");
		}

		// ��ѯ���ᵥ
		UserAccountRollDO queryUserAccountRollDO = new UserAccountRollDO();
		queryUserAccountRollDO.setListid(userAccountRollDO.getListid());
		queryUserAccountRollDO.setUid(userAccountRollDO.getUid());
		queryUserAccountRollDO.setType(UserAccountRollDO.TYPE_FREEZE);
		queryUserAccountRollDO.setAccount_type(userAccountRollDO.getAccount_type());
		queryUserAccountRollDO.setCurtype(userAccountRollDO.getCurtype());
		List<UserAccountRollDO> queryUserAccountRollResult = queryUserAccountRolllist(queryUserAccountRollDO);
		if (queryUserAccountRollResult == null
				|| queryUserAccountRollResult.size() > 1) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_FREEZE_ERROR, "���ᵥ����");
		}

		UserAccountRollDO freezeUserAccountRollDO = queryUserAccountRollResult
				.get(0);
		// �ж϶��ᵥ�ŵ�һ����
		if (CommonUtil.trimString(freezeUserAccountRollDO.getFreeze_id()) != null
				&& !freezeUserAccountRollDO.getFreeze_id().equals(
						userAccountRollDO.getFreeze_id())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_ID_ERROR, "�ⶳ���Ŵ���");
		}
		if (freezeUserAccountRollDO.getFreezenum().compareTo(
				userAccountRollDO.getFreezenum()) != 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_ERROR, "�ⶳ������");
		}
		
		//�����Ҫ��������˻�������������ֱ�ӽⶳ
		if (draw) {
			userAccountRollDO.setPaynum(userAccountRollDO.getFreezenum());//�˻������ֵ
			userAccountRollDO.setBalance(userAccountDO.getBalance().subtract(userAccountRollDO.getPaynum()));
		}
		else{
			userAccountRollDO.setBalance(userAccountDO.getBalance());// �˻�����
		}
				
		userAccountRollDO.setFreeze_balance(userAccountDO.getFreeze_balance().subtract(userAccountRollDO.getFreezenum()));
		userAccountRollDO.setType(UserAccountRollDO.TYPE_UNFREEZE);
		userAccountRollDO.setAcctid(userAccountDO.getAcctid());
		userAccountRollDO.setTrade_time(CommonUtil.formatDate(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateUserAccount", userAccountRollDO);

		createUserAccountRoll(userAccountRollDO);
		return userAccountRollDO;
	}

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (UserAccountDO) client.queryForObject("queryUserAccount",
				userAccountDO);
	}

	@Transactional
	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
			UserAccountRollDO toUserAccountRollDO) {
		//��Ǯ�˻���
		drawUserAccountDirectSuccess(fromUserAccountRollDO);
		
		//��Ǯ�˻���
		saveUserAccount(toUserAccountRollDO);

	}

	@SuppressWarnings("unchecked")
	public List<UserAccountRollDO> queryUserAccountRolllist(
			UserAccountRollDO userAccountRollDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (List<UserAccountRollDO>) client.queryForList(
				"queryUserAccountRoll", userAccountRollDO);
	}

}
