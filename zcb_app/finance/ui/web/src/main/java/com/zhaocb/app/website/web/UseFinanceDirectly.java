package com.zhaocb.app.website.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.utils.CommonUtil;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zhaocb.app.website.web.exception.WebServiceRetException;
import com.zhaocb.app.website.web.model.UseFinanceInput;
import com.zhaocb.app.website.web.model.UseFinanceOutput;
import com.zhaocb.app.website.web.util.FinanceWebComm;
import com.zhaocb.common.aop.annotation.LogMethod;
import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;


@RequestMapping
public class UseFinanceDirectly {
	private Map<String, String> appConfig;
	private FinanceFacade financeFacade;
	private static final Log LOG = LogFactory.getLog(UseFinanceInput.class);

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@LogMethod
	public UseFinanceOutput handleTestFinance(UseFinanceInput useFinanceInput)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		// 检查参数
		checkParam(useFinanceInput);
		// 检查额度是否可用
		SpConfigDO spConfigDO = financeFacade.querySpConfig(useFinanceInput.getSpid(), useFinanceInput.getBizCode());
		FinanceWebComm.checkCreditBalance(spConfigDO, useFinanceInput.getTotalFee());  // 不足直接抛出异常
		
		// 查询用户是否绑定，没绑定的进行绑定，返回用户信息
		UserBindDO userBindDO = checkUserBind(useFinanceInput);
		
		// 记录订单
		useFinanceInput.setListid(FinanceWebComm.genListId(useFinanceInput.getSpid()));
		TradeOrderDO tradeOrderDO = createOrder(useFinanceInput,userBindDO);
		
		// 垫资账户c2c转账给提现账户
		financeToFetchAccount(useFinanceInput);
		
		// TODO 扣减商户可用额度放在事物中，放在更新订单使用申请成功中
		
		// 更新订单状态为使用申请成功
		updateTradeOrderState(tradeOrderDO,TradeOrderDO.STATE_INIT,TradeOrderDO.STATE_FINANCE_APPLY_SUC);
		
		
		// 返回
		UseFinanceOutput useFinanceOutput = new UseFinanceOutput();
		return useFinanceOutput;
	}
	
	public void updateTradeOrderState(TradeOrderDO inputTradeOrder,int fromState,int toState){
		TradeOrderDO tradeOrderDO = new TradeOrderDO();
		tradeOrderDO.setListId(inputTradeOrder.getListId());
		tradeOrderDO.setState(toState);
		tradeOrderDO.setLastState(fromState);
		
		financeFacade.updateTradeOrder(tradeOrderDO);
	}
	
	public void financeToFetchAccount(UseFinanceInput useFinanceInput){
		String financeSpid = appConfig.get("financeSpid");
		String fetchSpid = appConfig.get("fetchSpid");
		SpConfigDO financeSpConfig = financeFacade.querySpConfig(financeSpid, null);
		SpConfigDO fetchSpConfig = financeFacade.querySpConfig(fetchSpid, null);
		if(financeSpConfig == null || fetchSpConfig == null ){
			LOG.info("垫资账户 或 提现账户 不可用，请确认, 垫子商户号:"+financeSpid
					+ "提现商户号:"+fetchSpid);
			throw new WebServiceRetException(WebServiceRetException.SP_CANNOT_USE,"商户不可用，请确认");
		}
		
		UserAccountRollDO fromAccount = new UserAccountRollDO();
		fromAccount.setUid(financeSpConfig.getUid());
		fromAccount.setListid(useFinanceInput.getListid());
		//fromAccount.setCoding(useFinanceInput.getOutTradeNo());
		//fromAccount.setPaynum(useFinanceInput.getTotalFee());
		
		UserAccountRollDO toAccount = new UserAccountRollDO();
		toAccount.setUid(fetchSpConfig.getUid());
		toAccount.setListid(useFinanceInput.getListid());
		//toAccount.setCoding(useFinanceInput.getOutTradeNo());
		//toAccount.setPaynum(useFinanceInput.getTotalFee());
		
		financeFacade.c2cTransferDirectly(fromAccount,toAccount);
	}
	public UserBindDO checkUserBind(UseFinanceInput useFinanceInput){
		// 查询用户绑定信息
		UserBindDO cond = new UserBindDO();
		
		cond.setSpid(useFinanceInput.getSpid());
		cond.setSpUserId(useFinanceInput.getSpUserId());
		UserBindDO userBindDO = financeFacade.queryUserBindInfo(cond);
		// 判断没有绑定，进行绑定
		if(userBindDO == null || CommonUtil.trimString(userBindDO.getBindId()) == null){
			userBindDO = bindUser(useFinanceInput);
		}
		return userBindDO;
	}
	public UserBindDO bindUser(UseFinanceInput useFinanceInput){
		
		UserBindDO userBindDO = new UserBindDO();
		userBindDO.setBindId(FinanceWebComm.genBindId());
		userBindDO.setSpid(useFinanceInput.getSpid());
		userBindDO.setSpUserId(useFinanceInput.getSpUserId());
		userBindDO.setCreType(useFinanceInput.getCreType());
		userBindDO.setCreId(useFinanceInput.getCreId());
		userBindDO.setTrueName(useFinanceInput.getTrueName());
		userBindDO.setMobile(useFinanceInput.getMobile());
		userBindDO.setAccTime(new Date());
		financeFacade.bindUser(userBindDO);
		return userBindDO;
	}
	public TradeOrderDO createOrder(UseFinanceInput useFinanceInput,UserBindDO userBindDO){
		TradeOrderDO tradeOrderDO = new TradeOrderDO();
		
		tradeOrderDO.setListId(useFinanceInput.getListid());
		tradeOrderDO.setBindId(userBindDO.getBindId());
		tradeOrderDO.setSpid(useFinanceInput.getSpid());
		tradeOrderDO.setBizCode(useFinanceInput.getBizCode());
		tradeOrderDO.setOutTradeNo(useFinanceInput.getOutTradeNo());
		tradeOrderDO.setTotalFee(useFinanceInput.getTotalFee());
		tradeOrderDO.setUseType(useFinanceInput.getUseType());
		tradeOrderDO.setUseDays(useFinanceInput.getUseDays());
		tradeOrderDO.setBankType(useFinanceInput.getBankType());
		tradeOrderDO.setCardNo(useFinanceInput.getCardNo());
		tradeOrderDO.setTrueName(useFinanceInput.getTrueName());
		financeFacade.createTradeOrder(tradeOrderDO);
		return tradeOrderDO;
	}
	
	
	
	public void checkParam(UseFinanceInput useFinanceInput){
		
	}
	
	public FinanceFacade getFinanceFacade() {
		return financeFacade;
	}

	public void setFinanceFacade(FinanceFacade financeFacade) {
		this.financeFacade = financeFacade;
	}
	
	public Map<String, String> getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}
}

