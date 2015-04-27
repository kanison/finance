package com.zcb_app.account.service.dao.ibatis;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.app.utils.CommonUtil;
import com.app.utils.MoneyType;
import com.zcb_app.account.service.dao.UserAccountDAO;
import com.zcb_app.account.service.dao.type.AcctFreezeBalanParams;
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
					AccountServiceRetException.LISTID_NOT_EXIST, "缺少交易单号");
		}
		userAccountRollDO.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertUserAcctRoll", userAccountRollDO);
	}
	
	/**
	 * 更新用户账户金额
	 * @param userAccountDO
	 */
	private void updateUserAccountBalance(UserAccountDO userAccountDO)
	{
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateUserAcctBalance", userAccountDO);
		if (1 != effected_rows) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_EFFECTED_ROWS, "更新的影响行数不为1");
		}
	}
	
	private void insertTransVoucher(TransVoucherDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertTransVoucher", obj);
	}
	
	/**
	 * 保存冻结单信息
	 * @param obj
	 */
	private void insertFreezeList(FreezeListDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertFreezeList", obj);
	}
	
	/**
	 * 更新冻结单信息
	 * @param obj
	 */
	private void updateFreezeList(FreezeListDO obj)
	{
		obj.genDataTableIndex();
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		int effected_rows = client.update("updateFreezeList", obj);
		
		if (1 != effected_rows) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_EFFECTED_ROWS, "更新的影响行数不为1");
		}
	}
	
	/**
	 * 减账户可用金额，记录账户流水
	 * @param userAccountDO
	 * @param userAccountRollDO
	 * @return
	 */
	@Transactional
	private UserAccountDO subTractUserUnfreezeBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {

		// 账户金额减冻结金额为剩余可用金额
		BigDecimal ba = user.getBalance().subtract(
				user.getFreeze_balance());
		if (ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "账户余额不足");
		}
		
		//减用户可以有余额
		BigDecimal up_ba = user.getBalance().subtract(roll.getPay_amt());
		user.setBalance(up_ba);
		updateUserAccountBalance(user);
		
		//记录用户账户流水	
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
	 * 减用户冻结金额，记录账户流水
	 * @param user
	 * @param roll
	 * @return
	 */
	private UserAccountDO subTractUserFreezeBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {
		//判断账户的冻结金额是否足够
		BigDecimal usr_frozen_ba = user.getFreeze_balance();
		if (usr_frozen_ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "账户余额不足");
		}
		
		//查询冻结单		
		FreezeListDO flistObj = new FreezeListDO();
		flistObj.setListid(roll.getRela_list());
		flistObj.setUid(user.getUid());
		flistObj.setCur_type(roll.getCur_type());
		flistObj = queryFreezeList(flistObj);
		if (null == flistObj) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "无效的冻结单");
		}
		
		//检查冻结单状态
		if (FreezeType.FS_FROZEN != flistObj.getState()
			&& FreezeType.FS_PARTLY_UFREEZE != flistObj.getState()) {
			throw new AccountServiceRetException(
					AccountServiceRetException.LISTID_FREEZE_ERROR, "冻结单状态不正确");
		}
		
		//可用的冻结金额为总冻结金额-已解冻金额
		BigDecimal list_frozen_ba = flistObj.getFreeze_amt().subtract(flistObj.getUnfreeze_amt());
		if (list_frozen_ba.compareTo(roll.getPay_amt()) < 0) {
			throw new AccountServiceRetException(
					AccountServiceRetException.FREEZE_BALANCE_NOT_ENOUGH, "冻结金额不足");
		}
		
		//设置冻结单解冻金额和状态
		BigDecimal ufamt = flistObj.getUnfreeze_amt();
		ufamt = ufamt.add(roll.getPay_amt());
		if (flistObj.getFreeze_amt().compareTo(ufamt) == 0)
			flistObj.setState(FreezeType.FS_ALL_UFREEZE);
		else
			flistObj.setState(FreezeType.FS_PARTLY_UFREEZE);
		
		flistObj.setUnfreeze_amt(ufamt);
		//更新冻结单
		updateFreezeList(flistObj);
		
		//减用户可以有余额
		BigDecimal up_ba = user.getBalance().subtract(roll.getPay_amt());
		user.setBalance(up_ba);
		usr_frozen_ba = user.getFreeze_balance().subtract(roll.getPay_amt());
		user.setFreeze_balance(usr_frozen_ba);
		updateUserAccountBalance(user);
		
		//记录用户账户流水	
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
		//如果是解冻并转账则使用用户冻结金额，否则使用用户未冻结金额
		if (TransType.ACT_C2C_UNFREEZE_TRSFR == roll.getAction_type()) {
			return subTractUserFreezeBalance(user, roll);
		} else {
			return subTractUserUnfreezeBalance(user, roll);
		}			
	}
	
	/**
	 * 增加账户金额
	 * @param user
	 * @param roll
	 * @return
	 */
	@Transactional
	private UserAccountDO addUserBalance(
			UserAccountDO user,
			UserAccountRollDO roll) {
		//如果冻结金额不为0，则需要传入冻结单号
		Boolean bfreeze = (roll.getPayfreeze_amt().compareTo(MoneyType.ZERO) > 0);
		if( true == bfreeze
			&& null == CommonUtil.trimString(roll.getRela_list())) {
			throw new AccountServiceRetException(
					AccountServiceRetException.INPUT_PARAMS_ERROR, "缺少冻结单号");
		}
		
		//增加户可以有余额和冻结金额
		user.setBalance(user.getBalance().add(roll.getPay_amt()));
		if (bfreeze) {
			user.setFreeze_balance(user.getFreeze_balance().add(
					roll.getPayfreeze_amt()));
		}
		updateUserAccountBalance(user);
		
		//记录用户账户流水		
		roll.setUid(user.getUid());
		roll.setUserid(user.getUserid());
		roll.setAcct_type(user.getAccttype());
		roll.setType(TransType.BKT_PAY_IN);
		roll.setAcctid(user.getAcctid());
		roll.setBalance(user.getBalance());
		roll.setFreeze_balance(user.getFreeze_balance());		
		createUserAccountRoll(roll);
		
		//如果有冻结金额，需要记录冻结单
		if (bfreeze) {
			FreezeListDO flistObj = new FreezeListDO();
			//用户信息
			flistObj.setUid(user.getUid());
			flistObj.setUserid(user.getUserid());
			//交易信息，冻结单号为流水的关联单号
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
		
		//如果存在则比较关键参数是否一致
		if (tv.getFrom_uid() != params.getFrom_uid())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "From_uid不一致");
		
		//LOG.debug("tv fromuserid="+tv.getFrom_userid()+",params.fromuserid="+params.getFrom_userid());
		
		if (!tv.getFrom_userid().equals(params.getFrom_userid()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "From_userid不一致");
					
		if (tv.getTo_uid() != params.getTo_uid())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "To_uid不一致");
		
		if (!tv.getTo_userid().equals(params.getTo_userid()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "To_userid不一致");
		
		if (tv.getCur_type() != params.getCur_type())
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "交易币种不一致");
		
		if (!tv.getTrans_amt().equals(params.getTrans_amt()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "交易金额不一致");
		
		if (!tv.getFrozen_amt().equals(params.getFrozen_amt()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "冻结金额不一致");
		
		if (!tv.getRela_list().equals(params.getRela_list()))
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_REENTY_INCONSISTENT, "关联的单号不一致");
		
		//参数一致，报特定的重入错误码，业务根据此错误码处理已完成重复提交的请求
		throw new AccountServiceRetException(
				AccountServiceRetException.ERR_REENTY_OK, "交易已经成功");
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
	 * 创建交易现金账户
	 */
	public void createUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("createUserAccount", userAccountDO);
	}
	
	/**
	 * 查询交易现在金账户
	 */
	public UserAccountDO queryUserAccount(UserAccountDO userAccountDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (UserAccountDO) client.queryForObject("queryUserAccount",
				userAccountDO);
	}
		
	/**
	 * 查询交易凭证信息
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
	 * 查询冻结单信息
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
		//查询凭证单是否存在，如果存在检查关键参数是否一致
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
		
		//锁交易账户，为防止死锁，按uid从小到的顺序加锁
		if (fromuser.getUid() > touser.getUid()) {
			touser = queryUserAccount(touser);		
			fromuser = queryUserAccount(fromuser);
		} else {
			fromuser = queryUserAccount(fromuser);
			touser = queryUserAccount(touser);
		}
		if (null == fromuser || null == touser) {
			throw new AccountServiceRetException(
					AccountServiceRetException.ERR_USER_ACCT_NOT_EXSIT, "交易账户不存在");
		}
		
		//出钱账户减
		UserAccountRollDO fromuser_roll = new UserAccountRollDO();
		//设置交易信息
		fromuser_roll.setCur_type(params.getCur_type());
		fromuser_roll.setPay_amt(params.getTrans_amt());
		fromuser_roll.setPayfreeze_amt(params.getFrozen_amt());
		fromuser_roll.setAction_type(params.getAction_type());
		fromuser_roll.setTrade_acc_time(params.getTrade_acc_time());
		fromuser_roll.setListid(params.getListid());
		fromuser_roll.setRela_list(params.getRela_list());
		fromuser_roll.setMemo(params.getMemo());
		fromuser_roll.setClient_ip(params.getClient_ip());
		//设置目标账户
		fromuser_roll.setTo_uid(touser.getUid());
		fromuser_roll.setTo_userid(touser.getUserid());		
		subUserBalance(fromuser, fromuser_roll);
		
		//入钱账户加
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
		//设置目标账户
		touser_roll.setTo_uid(fromuser.getUid());
		touser_roll.setTo_userid(fromuser.getUserid());
		addUserBalance(touser, touser_roll);
		
		//记录凭证单
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

	/**
	 * 操作冻结金额
	 * 1、	查询加锁用户交易账户
	 * 2、	判断用户的可用金额(非冻结余额)是否足够，不够报余额不足错误
	 * 3、	增加冻结金额
	 * 4、	记录冻结单
	 * 5、	记录交易凭证流水
	 * @param params
	 * @author Gu.Dongying 
	 * @date 2015年4月24日 上午11:49:08
	 */
	@Transactional
	public void freezeUserBalance(AcctFreezeBalanParams params){
		UserAccountDO user = new UserAccountDO();
		user.setUid(params.getUid());
		user.setAccttype(params.getAcct_type());
		user.setCurtype(params.getCur_type());
		user.setQuerylock(true);
		//查询加锁用户交易账户
		user = queryUserAccount(user);
		//判断用户的可用金额(非冻结余额)是否足够，不够报余额不足错误
		if(params.getFreeze_amt().compareTo(user.getBalance()) == -1){
			throw new AccountServiceRetException(
					AccountServiceRetException.BALANCE_NOT_ENOUGH, "账户余额不足");
		}
		
		//增加冻结金额
		user.setBalance(user.getBalance().subtract(params.getFreeze_amt()));
		user.setFreeze_balance(user.getFreeze_balance().add(params.getFreeze_amt()));
		updateUserAccountBalance(user);
		
		//记录冻结单
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
		
		//记录交易凭证流水
		TransVoucherDO voucher = new TransVoucherDO();
		voucher.setVoucher(params.getListid());
		voucher.setListid(params.getListid());
		//数据库必填，暂时设置为""；
		voucher.setReq_no("");
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
		insertTransVoucher(voucher);
		
	}
}
