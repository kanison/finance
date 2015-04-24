package com.zcb_app.account.service.acct;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.MD5Util;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
import com.zcb_app.account.service.dao.type.AcctTransParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.exception.AccountServiceRetException;
import com.zcb_app.account.service.facade.UserAccountFacade;
import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.facade.dataobject.FreezeBalanceParams;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UserAccountDO;
import com.zcb_app.account.service.type.AccountType;
import com.zcb_app.account.service.type.CurrencyType;
import com.zcb_app.account.service.type.TransType;

public class UserAccountImpl implements UserAccountFacade {
	private static final Log LOG = LogFactory.getLog(UserAccountImpl.class);
	private UserAccountDAO userAccountDAO;
	private Map<String, String> appConfig;
	/*
	 * ����ӿ�ָ���Ĳ����룬���Ʋ���ʹ��
	 */
	private static final String FREEZE_OPERATION_CODE = "100200300";
	
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
	
	/**
	 * ����ӿ�
	 * @param params
	 * @return ���ؽ��
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015��4��24�� ����9:59:59
	 */
	public String freezeUserBalance(FreezeBalanceParams params){
		AcctFreezeBalanParams afbParams = AcctFreezeBalanParams.valueOf(params);
		//������������ʽ�Ƿ���ȷ
		checkTransParams(afbParams);
		//У��op_code�Ƿ���ȷ
		checkOpCode(params);
		//��ѯ���ᵥ�ŵĽ���ƾ֤��ˮ�Ƿ��Ѵ��ڣ�����Ѵ��ڼ��ؼ������Ƿ���ͬ����ͬ�򷵻���������롣
		/*if(checkFrListTransactionFlow(afbParams)){
			return AccountServiceRetException.INPUT_PARAMS_ERROR;
		}*/
		
		//��鶳���˻����û���Ϣ
		long uid = checkUserInfo(params.getUserid(), params.getAcct_type());
		afbParams.setUid(uid);
		//��ѯ�����û������˻�
		//�ж��û��Ŀ��ý��(�Ƕ������)�Ƿ��㹻���������������
		//���Ӷ�����Ҽ�¼���ᵥ,��¼����ƾ֤��ˮ
		userAccountDAO.freezeUserBalance(afbParams);
		
		StringBuilder sb = new StringBuilder();
		sb.append("ID:");
		sb.append(afbParams.getUid());
		sb.append("|List ID:");
		sb.append(afbParams.getListid());
		sb.append("|User ID:");
		sb.append(afbParams.getUserid());
		sb.append("|Account type:");
		sb.append(afbParams.getAcct_type());
		sb.append("|Currency type:");
		sb.append(afbParams.getCur_type());
		sb.append("|Action type:");
		sb.append(afbParams.getAction_type());
		sb.append("|Trans type:");
		sb.append(afbParams.getTrans_type());
		sb.append("|Freeze amount:");
		sb.append(afbParams.getFreeze_amt());
		LOG.debug(sb.toString());
		
		return AccountServiceRetException.ERR_REENTY_OK;
	}
	
	/**
	 * ��ѯ���ᵥ�ŵĽ���ƾ֤��ˮ�Ƿ��Ѵ��ڣ�����Ѵ��ڼ��ؼ������Ƿ���ͬ����ͬ�򷵻���������롣
	 * @param params
	 * @return ��ͬ��true����ͬ��false
	 * @Return boolean
	 * @author Gu.Dongying 
	 * @Date 2015��4��24�� ����11:52:53
	 */
	private boolean checkFrListTransactionFlow(AcctFreezeBalanParams params){
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setListid(params.getListid());
		voucher = userAccountDAO.queryTransVoucher(voucher);
		//����Ѵ��ڼ��ؼ������Ƿ���ͬ
		if(voucher != null){
			//����û����������͡��������͵ȹؼ�����
			if(params.getUserid().equals(voucher.getFrom_userid()) 
					&& voucher.getTrans_type() == params.getTrans_type()
					&& voucher.getAction_type() == params.getAction_type()){				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * У��op_code
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015��4��24�� ����11:21:36
	 */
	private void checkOpCode(FreezeBalanceParams params){
		//У��op_code�Ƿ���ȷ
		if(!FREEZE_OPERATION_CODE.equals(params.getOp_code())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "���������");
		}
	}
	
	/**
	 * ������������ʽ�Ƿ���ȷ
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015��4��24�� ����11:03:13
	 */
	private void checkTransParams(AcctFreezeBalanParams params) {
		if(StringUtils.isBlank(params.getUserid()) || StringUtils.isEmpty(params.getUserid())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "δָ����������û�");
		}
		if(StringUtils.isBlank(params.getListid()) || StringUtils.isEmpty(params.getListid())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "���ᵥ������");
		}
		if (params.getFreeze_amt() == null || params.getFreeze_amt().scale() != 2){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "�������ʽ����ȷ");
		}
		if (params.getAcct_type() <= 0){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "δָ���û��˻�����");
		}
		if (params.getAcct_type() == AccountType.UAT_BANK){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "�˻����Ͳ���Ϊ�����˻�");
		}
	}
}
