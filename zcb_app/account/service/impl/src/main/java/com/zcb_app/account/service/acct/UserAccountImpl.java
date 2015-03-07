package com.zcb_app.account.service.acct;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.facade.UserAccountFacade;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;


public class UserAccountImpl implements UserAccountFacade {
	private static final Log LOG = LogFactory.getLog(UserAccountImpl.class);
	private UserAccountDAO userAccountDAO;
	private Map<String, String> appConfig;

	public UserAccountDAO getUserAccountDAO() {
		return userAccountDAO;
	}

	public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public Map<String, String> getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}

	public void createUserAccount(UserAccountDO userAccountDO) {
		userAccountDAO.createUserAccount(userAccountDO);
	}

	public UserAccountRollDO drawUserAccountDirectSuccess(
			UserAccountRollDO userAccountRollDO) {
		return userAccountDAO.drawUserAccountDirectSuccess(userAccountRollDO);
	}

	public UserAccountRollDO drawUserAccountFreeze(
			UserAccountRollDO userAccountRollDO) {
		return userAccountDAO.drawUserAccountFreeze(userAccountRollDO);
	}

	public UserAccountRollDO drawUserAccountUnfreeze(
			UserAccountRollDO userAccountRollDO, boolean draw) {
		return userAccountDAO.drawUserAccountUnfreeze(userAccountRollDO, draw);
	}

	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		return userAccountDAO.queryUserAccount(userAccountDO);
	}

	public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO) {
		return userAccountDAO.saveUserAccount(userAccountRollDO);
	}

	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
			UserAccountRollDO toUserAccountRollDO) {
		userAccountDAO.userAccountTranfer(fromUserAccountRollDO, toUserAccountRollDO);
	}
	
}
