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
import com.zcb_app.account.service.dao.type.AcctUnFreezeBalanceParams;
import com.zcb_app.account.service.dao.type.SpUserInfo;
import com.zcb_app.account.service.exception.AccountServiceRetException;
import com.zcb_app.account.service.facade.UserAccountFacade;
import com.zcb_app.account.service.facade.dataobject.C2CTransParams;
import com.zcb_app.account.service.facade.dataobject.FreezeBalanceParams;
import com.zcb_app.account.service.facade.dataobject.TransVoucherDO;
import com.zcb_app.account.service.facade.dataobject.UnFreezeBalanceParams;
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
	private static final String FREEZE_OPERATION_CODE = "100010001";
	/*
	 * 解冻接口指定的操作码，控制操作使用
	 */
	private static final String UNFREEZE_OPERATION_CODE = "100010002";
	
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
	public void freezeUserBalance(FreezeBalanceParams params){
		AcctFreezeBalanParams afbParams = AcctFreezeBalanParams.valueOf(params);
		//检查输入参数格式是否正确
		checkFreezeTransParams(afbParams);
		//校验op_code是否正确
		checkOpCode(params.getOp_code(), FREEZE_OPERATION_CODE);
		//查询冻结单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
		checkFrListTransactionFlow(afbParams);
		
		//检查冻结账户的用户信息
		long uid = checkUserInfo(params.getUserid(), params.getAcct_type());
		afbParams.setUid(uid);
		//查询加锁用户交易账户
		//判断用户的可用金额(非冻结余额)是否足够，不够报余额不足错误
		//增加冻结金额并且记录冻结单,记录交易凭证流水
		userAccountDAO.freezeUserBalance(afbParams);
		
		StringBuilder sb = new StringBuilder();
		sb.append("冻结账户金额信息 ID:");
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
		
		throw new AccountServiceRetException(
				AccountServiceRetException.ERR_REENTY_OK, "交易已经成功！");
	}
	
	/**
	 * 查询冻结单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
	 * @param params
	 * @Return boolean
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:52:53
	 */
	private void checkFrListTransactionFlow(AcctFreezeBalanParams params) {
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setListid(params.getListid());
		voucher.setAction_type(params.getAction_type());
		voucher = userAccountDAO.queryTransVoucher(voucher);
		// 如果已存在检查关键参数是否相同
		if (voucher != null) {
			// 检查用户、币种、冻结金额等关键参数
			if(!params.getUserid().equals(voucher.getFrom_userid())){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT,
						"UserID参数不一致!");
			}
			if(voucher.getCur_type() != params.getCur_type()){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT, "交易币种不一致");
			}
			if(params.getFreeze_amt().compareTo(
					voucher.getFrozen_amt()) != 0){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT,
						"冻结金额不一致!");
			}
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_OK, "交易已经成功!");
		}
	}
	
	/**
	 * 校验op_code
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:21:36
	 */
	private void checkOpCode(String opCode, String code){
		if(StringUtils.isBlank(opCode) || StringUtils.isEmpty(opCode)){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "操作码不能为空!");
		}
		//校验op_code是否正确
		if(!code.equals(opCode)){
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "操作码错误!");
		}
	}
	
	/**
	 * 冻结接口检查输入参数格式是否正确
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:03:13
	 */
	private void checkFreezeTransParams(AcctFreezeBalanParams params) {
		if (StringUtils.isBlank(params.getUserid())
				|| StringUtils.isEmpty(params.getUserid())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_USER_NOT_EXSIT,
					"未指定冻结金额的用户!");
		}
		if (StringUtils.isBlank(params.getListid())
				|| StringUtils.isEmpty(params.getListid())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_NOT_EXIST, "冻结单不存在!");
		}
		if (params.getFreeze_amt() == null
				|| params.getFreeze_amt().scale() != 2) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "冻结金额格式不正确!");
		}
		if (params.getAcct_type() <= 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "未指定用户账户类型!");
		}
		if (params.getAcct_type() == AccountType.UAT_BANK) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR,
					"账户类型不能为银行账户!");
		}

		if (params.getTrade_acc_time() == null) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "交易时间不能为空!");
		}
	}
	
	/**
	 * 解冻接口
	 * @param params
	 * @return 返回解冻结果
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015年4月27日 上午10:34:13
	 */
	public void unfreezeUserBalance(UnFreezeBalanceParams params){
		//1、 检查输入参数格式是否正确，比如金额是否正确，单号是否为空
		checkUnFreezeTransParam(params);
		//2、 校验op_code是否正确
		checkOpCode(params.getOp_code(), UNFREEZE_OPERATION_CODE);
		
		AcctUnFreezeBalanceParams unFreeze = AcctUnFreezeBalanceParams.valueOf(params);
		//3、 查询解冻单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
		checkUnFrListTransactionFlow(unFreeze);
		
		long uid = checkUserInfo(params.getUserid(), params.getAcct_type());
		unFreeze.setUid(uid);
		
		//4、 查询加锁用户交易账户
		//5、 判断用户的冻结金额是否足够，不够报余额不足错误
		//6、 查询冻结单信息，并校验状态和金额是否正确
		//7、 修改冻结单的解冻金额和状态
		//8、 记录用户账户流水（建议步骤6、7、8封装在同一个函数中实现）
		//9、 记录交易凭证流水
		userAccountDAO.unfreezeUserBalance(unFreeze);
		
		StringBuilder sb = new StringBuilder();
		sb.append("解冻账户金额信息 ID:");
		sb.append(unFreeze.getUid());
		sb.append("|List ID:");
		sb.append(unFreeze.getListid());
		sb.append("|User ID:");
		sb.append(unFreeze.getUserid());
		sb.append("|Account type:");
		sb.append(unFreeze.getAcct_type());
		sb.append("|Currency type:");
		sb.append(unFreeze.getCur_type());
		sb.append("|Action type:");
		sb.append(unFreeze.getAction_type());
		sb.append("|Trans type:");
		sb.append(unFreeze.getTrans_type());
		sb.append("|UnFreeze amount:");
		sb.append(unFreeze.getUnfreeze_amt());
		LOG.debug(sb.toString());
		
		throw new AccountServiceRetException(
				AccountServiceRetException.ERR_REENTY_OK, "交易已经成功！");
	}
	
	/**
	 * 检查输入参数格式是否正确，比如金额是否正确，单号是否为空
	 * @param params
	 * @Return void
	 * @author Gu.Dongying 
	 * @Date 2015年4月27日 上午11:34:10
	 */
	private void checkUnFreezeTransParam(UnFreezeBalanceParams params) {
		if (StringUtils.isBlank(params.getUserid())
				|| StringUtils.isEmpty(params.getUserid())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_USER_NOT_EXSIT,
					"未指定解冻金额的用户!");
		}
		if (StringUtils.isBlank(params.getListid())
				|| StringUtils.isEmpty(params.getListid())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_ID_ERROR, "解冻单不存在!");
		}
		if (params.getUnfreeze_amt() == null
				|| params.getUnfreeze_amt().scale() != 2) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_ERROR,
					"解冻金额格式不正确!");
		}
		if (params.getAcct_type() <= 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "未指定用户账户类型!");
		}
		if (params.getAcct_type() == AccountType.UAT_BANK) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR,
					"账户类型不能为银行账户!");
		}

		if (StringUtils.isBlank(params.getFreeze_list())
				|| StringUtils.isEmpty(params.getFreeze_list())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "原冻结单不能为空!");
		}

		if (params.getTrade_acc_time() == null) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "交易时间不能为空!");
		}
	}
	
	/**
	 * 查询解冻单号的交易凭证流水是否已存在，如果已存在检查关键参数是否相同。相同则返回重入错误码。
	 * @param params
	 * @return 相同：true，不同：false
	 * @author Gu.Dongying 
	 * @Date 2015年4月24日 上午11:52:53
	 */
	private void checkUnFrListTransactionFlow(AcctUnFreezeBalanceParams unFreeze) {
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setListid(unFreeze.getListid());
		voucher.setAction_type(unFreeze.getAction_type());
		voucher = userAccountDAO.queryTransVoucher(voucher);
		// 如果已存在检查关键参数是否相同
		if (voucher != null) {
			// 检查用户、币种、解冻金额等关键参数
			if(!unFreeze.getUserid().equals(voucher.getFrom_userid())){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT,
						"UserID参数不一致!");
			}
			if(voucher.getCur_type() != unFreeze.getCur_type()){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT, "交易币种不一致");
			}
			if(unFreeze.getUnfreeze_amt().compareTo(
					voucher.getFrozen_amt()) != 0){
				throw new AccountServiceRetException(
						AccountServiceRetException.ERR_REENTY_INCONSISTENT,
						"解冻金额不一致!");
			}
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_OK, "交易已经成功!");
		}
	}
	
	
}
