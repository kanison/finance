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
		//��������Ŀ��ֻ֧�������
		userAccountDO.setCurtype(CurrencyType.CNY);
		userAccountDO.setState(AccountType.UAS_NORMAL);
		userAccountDO.setLstate(1);
		userAccountDO.setBalance(new BigDecimal("0.00"));
		userAccountDO.setFreeze_balance(new BigDecimal("0.00"));
		//����ǩ��
		String sign = genSign(userAccountDO);
		userAccountDO.setSign(sign);
		userAccountDAO.createUserAccount(userAccountDO);
	}
	
	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		UserAccountDO resObj = userAccountDAO.queryUserAccount(userAccountDO);
		//���DB��ǩ������ȷ
		String sign = genSign(resObj);
		if (!sign.equalsIgnoreCase(resObj.getSign())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_DATA_SIGN_ABN, "�˻����ݱ��۸�");
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
		
		//������
		checkTransParams(params);
		
		//��ȡclient ip
		//�ݲ�ʵ��
		params.setClient_ip("");
		
		//��齻�������Ƿ���ȷ
		if (TransType.TT_C2C_TRANS != params.getTrans_type()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "�������Ͳ���c2cת��");
		}
		
		//����˻����ͣ�ֻ���̻�����ͨ�û�����c2cת��,�����˻����Ͳ�����c2cת��
		if (AccountType.UAT_NORMAL != params.getFrom_acct_type()
			&& AccountType.UAT_SP != params.getFrom_acct_type()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "��֧�ֵ��˻�����");
		}
		
		if (AccountType.UAT_NORMAL != params.getTo_acct_type()
			&& AccountType.UAT_SP != params.getTo_acct_type()) {
				throw new AccountServiceRetException(
						AccountServiceRetException.INPUT_PARAMS_ERROR, "��֧�ֵ��˻�����");
			}
		
		//���ת�˳�Ǯ�˻����û���Ϣ
		long uid = checkUserInfo(params.getFrom_userid(), params.getFrom_acct_type());
		params.setFrom_uid(uid);
		
		//���ת����Ǯ�˻����û���Ϣ
		uid = checkUserInfo(params.getTo_userid(), params.getTo_acct_type());
		params.setTo_uid(uid);
				
		userAccountDAO.c2cTransfer(params);
	}
	
	/**
	 * ��ѯ�û���Ϣ������û���Ϣ�Ƿ���ȷ
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
							AccountServiceRetException.ERR_USER_NOT_EXSIT, "�û�������");
				}
				
				return spuser.getUid();
				//break;
			case AccountType.UAT_BANK:
				return -1L;
				//break;
			default:
				throw new AccountServiceRetException(
						AccountServiceRetException.INPUT_PARAMS_ERROR, "δ֪���û�����");
				//break;
		}
	}
	
	private void checkTransParams(AcctTransParams params)
	{
		if (params.getTrans_amt().scale() != 2)
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "����ʽ����ȷ");
		
		if (params.getFrozen_amt().scale() != 2)
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "����ʽ����ȷ");
	}
	/**
	 * �����˻������ݵ�ǩ���ֶ�
	 * ��ʽΪuid|acct_type|cur_type|balance|freeze_balanc|state|KEY
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
					AccountServiceRetException.SYSTEM_ERROR, "����ǩ��ʱ�������");
		} catch (NoSuchAlgorithmException e) {
			throw new AccountServiceRetException(
					AccountServiceRetException.SYSTEM_ERROR, "��֧�ֵ�ǩ���㷨");
		}		
	}
}
