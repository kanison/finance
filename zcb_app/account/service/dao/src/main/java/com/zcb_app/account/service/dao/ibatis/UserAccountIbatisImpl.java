package com.zcb_app.account.service.dao.ibatis;

import java.util.Date;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.CommonUtil;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;

public class UserAccountIbatisImpl extends SqlMapClientDaoSupport implements
		UserAccountDAO {

	private void createUserAccountRoll(
			UserAccountRollDO userAccountRollDO) {
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
		userAccountRollDO.setType(UserAccountRollDO.TYPE_SAVE);
		userAccountRollDO.setAcctid(userAccountDO.getAcctid());
		userAccountRollDO.setAcc_time(CommonUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));


		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.update("updateUserAccount", userAccountRollDO);

		createUserAccountRoll(userAccountRollDO);
		return userAccountRollDO;

	}

	public void createUserAccount(UserAccountDO userAccountDO) {
		// TODO Auto-generated method stub
		
	}

	public UserAccountRollDO drawUserAccountDirectSuccess(
			UserAccountRollDO userAccountRollDO) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserAccountRollDO drawUserAccountFreeze(
			UserAccountRollDO userAccountRollDO) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserAccountRollDO drawUserAccountUnfreeze(
			UserAccountRollDO userAccountRollDO, boolean success) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		// TODO Auto-generated method stub
		return null;
	}

	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
			UserAccountRollDO toUserAccountRollDO) {
		// TODO Auto-generated method stub
		
	}


}
