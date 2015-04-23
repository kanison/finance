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
		
		// ������
		checkParam(useFinanceInput);
		// ������Ƿ����
		SpConfigDO spConfigDO = financeFacade.querySpConfig(useFinanceInput.getSpid(), useFinanceInput.getBizCode());
		FinanceWebComm.checkCreditBalance(spConfigDO, useFinanceInput.getTotalFee());  // ����ֱ���׳��쳣
		
		// ��ѯ�û��Ƿ�󶨣�û�󶨵Ľ��а󶨣������û���Ϣ
		UserBindDO userBindDO = checkUserBind(useFinanceInput);
		
		// ��¼����
		useFinanceInput.setListid(FinanceWebComm.genListId(useFinanceInput.getSpid()));
		TradeOrderDO tradeOrderDO = createOrder(useFinanceInput,userBindDO);
		
		// �����˻�c2cת�˸������˻�
		financeToFetchAccount(useFinanceInput);
		
		// TODO �ۼ��̻����ö�ȷ��������У����ڸ��¶���ʹ������ɹ���
		
		// ���¶���״̬Ϊʹ������ɹ�
		updateTradeOrderState(tradeOrderDO,TradeOrderDO.STATE_INIT,TradeOrderDO.STATE_FINANCE_APPLY_SUC);
		
		
		// ����
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
			LOG.info("�����˻� �� �����˻� �����ã���ȷ��, �����̻���:"+financeSpid
					+ "�����̻���:"+fetchSpid);
			throw new WebServiceRetException(WebServiceRetException.SP_CANNOT_USE,"�̻������ã���ȷ��");
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
		// ��ѯ�û�����Ϣ
		UserBindDO cond = new UserBindDO();
		
		cond.setSpid(useFinanceInput.getSpid());
		cond.setSpUserId(useFinanceInput.getSpUserId());
		UserBindDO userBindDO = financeFacade.queryUserBindInfo(cond);
		// �ж�û�а󶨣����а�
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

