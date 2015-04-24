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
	 * 冻结接口指定的操作码，控制操作使用
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
	
	/**
	 * 冻结接口
	 * @param params
	 * @return 返回结果
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午9:59:59
	 */
	public String freezeUserBalance(FreezeBalanceParams params){
		AcctFreezeBalanParams afbParams = AcctFreezeBalanParams.valueOf(params);
		//检查输入参数格式是否正确
		checkTransParams(afbParams);
		//校验op_code是否正确
		checkOpCode(params);
		//查询冻结单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
		/*if(checkFrListTransactionFlow(afbParams)){
			return AccountServiceRetException.INPUT_PARAMS_ERROR;
		}*/
		
		//检查冻结账户的用户信息
		long uid = checkUserInfo(params.getUserid(), params.getAcct_type());
		afbParams.setUid(uid);
		//查询加锁用户交易账户
		//判断用户的可用金额(非冻结余额)是否足够，不够报余额不足错误
		//增加冻结金额并且记录冻结单,记录交易凭证流水
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
	 * 查询冻结单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
	 * @param params
	 * @return 相同：true，不同：false
	 * @Return boolean
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:52:53
	 */
	private boolean checkFrListTransactionFlow(AcctFreezeBalanParams params){
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setListid(params.getListid());
		voucher = userAccountDAO.queryTransVoucher(voucher);
		//如果已存在检查关键参数是否相同
		if(voucher != null){
			//检查用户、交易类型、操作类型等关键参数
			if(params.getUserid().equals(voucher.getFrom_userid()) 
					&& voucher.getTrans_type() == params.getTrans_type()
					&& voucher.getAction_type() == params.getAction_type()){				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 校验op_code
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:21:36
	 */
	private void checkOpCode(FreezeBalanceParams params){
		//校验op_code是否正确
		if(!FREEZE_OPERATION_CODE.equals(params.getOp_code())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "操作码错误");
		}
	}
	
	/**
	 * 检查输入参数格式是否正确
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:03:13
	 */
	private void checkTransParams(AcctFreezeBalanParams params) {
		if(StringUtils.isBlank(params.getUserid()) || StringUtils.isEmpty(params.getUserid())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "未指定冻结金额的用户");
		}
		if(StringUtils.isBlank(params.getListid()) || StringUtils.isEmpty(params.getListid())){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "冻结单不存在");
		}
		if (params.getFreeze_amt() == null || params.getFreeze_amt().scale() != 2){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "冻结金额格式不正确");
		}
		if (params.getAcct_type() <= 0){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "未指定用户账户类型");
		}
		if (params.getAcct_type() == AccountType.UAT_BANK){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "账户类型不能为银行账户");
		}
	}
}
