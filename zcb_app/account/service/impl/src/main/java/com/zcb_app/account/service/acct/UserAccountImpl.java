package com.zcb_app.account.service.acct;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonUtil;
import com.app.utils.IPUtil;
import com.app.utils.MD5Util;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.exception.AccountServiceRetException;
import com.zcb_app.account.service.facade.UserAccountFacade;
import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zcb_app.account.service.type.AccountType;
import com.zcb_app.account.service.type.CurrencyType;
import com.zcb_app.account.service.type.TransType;

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
		//开户币种目当只支持人民币
		userAccountDO.setCurtype(CurrencyType.CNY);
		userAccountDO.setState(AccountType.UAS_NORMAL);
		userAccountDO.setLstate(1);
		userAccountDO.setBalance(new BigDecimal("0.00"));
		userAccountDO.setFreeze_balance(new BigDecimal("0.00"));
		//生成签名
		String sign = genSign(userAccountDO);
		userAccountDO.setSign(sign);
		userAccountDAO.createUserAccount(userAccountDO);
	}
	
	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		UserAccountDO resObj = userAccountDAO.queryUserAccount(userAccountDO);
		//检查DB的签名是正确
		String sign = genSign(resObj);
		if (!sign.equalsIgnoreCase(resObj.getSign())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_DATA_SIGN_ABN, "账户数据被篡改");
		}
		
		return resObj;
	}

	/*
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

	public UserAccountRollDO saveUserAccount(UserAccountRollDO userAccountRollDO) {
		return userAccountDAO.saveUserAccount(userAccountRollDO);
	}

	public void userAccountTranfer(UserAccountRollDO fromUserAccountRollDO,
			UserAccountRollDO toUserAccountRollDO) {
		userAccountDAO.userAccountTranfer(fromUserAccountRollDO, toUserAccountRollDO);
	}
	*/
	
	public void noPwdC2cTransfer(C2CTransParams p)
	{
		AcctTransParams params = AcctTransParams.valueOf(p);
		
		//检查参数
		checkTransParams(params);
		
		//获取client ip
		//暂不实现
		params.setClient_ip("");
		
		//检查交易类型是否正确
		if (TransType.TT_C2C_TRANS != params.getTrans_type()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "交易类型不是c2c转账");
		}
		
		//检查账户类型，只有商户和普通用户允许c2c转账,银行账户类型不允许c2c转账
		if (AccountType.UAT_NORMAL != params.getFrom_acct_type()
			&& AccountType.UAT_SP != params.getFrom_acct_type()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "不支持的账户类型");
		}
		
		if (AccountType.UAT_NORMAL != params.getTo_acct_type()
			&& AccountType.UAT_SP != params.getTo_acct_type()) {
				throw new AccountServiceRetException(
						AccountServiceRetException.INPUT_PARAMS_ERROR, "不支持的账户类型");
			}
		
		//检查转账出钱账户的用户信息
		long uid = checkUserInfo(params.getFrom_userid(), params.getFrom_acct_type());
		params.setFrom_uid(uid);
		
		//检查转账收钱账户的用户信息
		uid = checkUserInfo(params.getTo_userid(), params.getTo_acct_type());
		params.setTo_uid(uid);
				
		userAccountDAO.c2cTransfer(params);
	}
	
	/**
	 * 查询用户信息，检查用户信息是否正确
	 * @param params
	 */
	private long checkUserInfo(String userid, int acct_type)
	{
		switch(acct_type) {
			case AccountType.UAS_NORMAL:
				return -1L;
				//break;
			case AccountType.UAT_SP:
				SpUserInfo spuser = userAccountDAO.querySpUser(userid);
				if (null == spuser) {
					throw new AccountServiceRetException(
							AccountServiceRetException.ERR_USER_NOT_EXSIT, "用户不存在");
				}
				
				return spuser.getUid();
				//break;
			case AccountType.UAT_BANK:
				return -1L;
				//break;
			default:
				throw new AccountServiceRetException(
						AccountServiceRetException.INPUT_PARAMS_ERROR, "未知的用户类型");
				//break;
		}
	}
	
	private void checkTransParams(AcctTransParams params)
	{
		if (params.getTrans_amt().scale() != 2)
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "金额格式不正确");
		
		if (params.getFrozen_amt().scale() != 2)
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "金额格式不正确");
	}
	/**
	 * 生成账户表数据的签名字段
	 * 格式为uid|acct_type|cur_type|balance|freeze_balanc|state|KEY
	 * @param userAccountDO
	 * @return
	 */
	private String genSign(UserAccountDO userAcct)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(userAcct.getUid());
		sb.append("|");
		sb.append(userAcct.getAccttype());
		sb.append("|");
		sb.append(userAcct.getCurtype());
		sb.append("|");
		sb.append(userAcct.getBalance());
		sb.append("|");
		sb.append(userAcct.getFreeze_balance());
		sb.append("|");
		sb.append(userAcct.getState());
		sb.append("|");
		sb.append(appConfig.get("useracct.signkey"));
		
		LOG.debug(sb.toString());
		
		try {
			return MD5Util.getMD5(sb.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new AccountServiceRetException(
					AccountServiceRetException.SYSTEM_ERROR, "生成签名时编码错误");
		} catch (NoSuchAlgorithmException e) {
			throw new AccountServiceRetException(
					AccountServiceRetException.SYSTEM_ERROR, "不支持的签名算法");
		}		
	}
}
