package com.zhaocb.app.website.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.aop.annotation.LogMethod;
import com.zhaocb.app.website.web.model.UseFinanceInput;
import com.zhaocb.app.website.web.model.UseFinanceOutput;
import com.zhaocb.app.website.web.util.FinanceWebComm;
import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;
import com.zhaocb.zcb_app.finance.service.facade.FundFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;


@RequestMapping
public class UseFinanceDirectly {
	private Map<String, String> appConfig;
	private FundFacade fundFacade;
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
		checkUserBind(useFinanceInput);
		
		// ��¼����
		createOrder(useFinanceInput);
		// �����˻�c2cת�˸�����
		
		// �ۼ��̻����ö��
		
		// ����
		UseFinanceOutput useFinanceOutput = new UseFinanceOutput();
		return useFinanceOutput;
	}
	
	public void checkUserBind(UseFinanceInput useFinanceInput){
		// ��ѯ�û�����Ϣ
		UserBindDO cond = new UserBindDO();
		cond.setBindId(useFinanceInput.get);
		cond.setSpid(useFinanceInput.getSpid());
		cond.setSpUserId(useFinanceInput.getSpUserId());
		UserBindDO userBindDO = financeFacade.queryUserBindInfo(userBindDO)
		// �ж�û�а󶨣����а�
	}
	public void createOrder(UseFinanceInput useFinanceInput){
		TradeOrderDO tradeOrderDO = new TradeOrderDO();
		
		//TODO ���ɶ���
		String listId = "";
		tradeOrderDO.setListId(listId);
		tradeOrderDO.setBindId("");
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
	}
	
	
	
	public void checkParam(UseFinanceInput useFinanceInput){
		
	}
	
	public FundFacade getFundFacade() {
		return fundFacade;
	}

	public void setFundFacade(FundFacade fundFacade) {
		this.fundFacade = fundFacade;
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

