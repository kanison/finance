package com.zhaocb.zcb_app.finance.service.finance;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonUtil;
import com.zcb_app.account.service.facade.UserAccountFacade;
import com.zcb_app.account.service.facade.dataobject.UserAccountRollDO;
import com.zhaocb.zcb_app.finance.service.dao.FinanceDAO;
import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public class FinanceFacadeImp implements FinanceFacade {
	private Map<String, String> appConfig;
	private FinanceDAO financeDAO;
	private UserAccountFacade userAccountFacade;
	private static final Log LOG = LogFactory.getLog(FinanceFacadeImp.class);
	public SpConfigDO querySpConfig(String spid,String bizCode){
		SpConfigDO spCondition = new SpConfigDO();
		spCondition.setSpid(spid);
		SpConfigDO retSpConfig = financeDAO.querySpConfig(spCondition);
		if(retSpConfig == null || retSpConfig.getLstate() == SpConfigDO.LSTATE_INVALID){
			LOG.info("商户信息为空，或商户状态不可用 spid="+spid);
			return null;
		}
		if(CommonUtil.trimString(bizCode) !=null ){
			SpBizConfigDO spBizCondition = new SpBizConfigDO();
			spBizCondition.setSpid(spid);
			spBizCondition.setBizCode(bizCode);
			SpBizConfigDO retSpBizConfig = financeDAO.querySpBizConfig(spBizCondition);
			retSpConfig.setSpBizConfigDO(retSpBizConfig);
			if(retSpBizConfig != null && retSpBizConfig.getAdvanceId()!=0){
				AdvanceTypeConfigDO advanceTypeCondition = new AdvanceTypeConfigDO();
				advanceTypeCondition.setAdvanceId(retSpBizConfig.getAdvanceId());
				retSpConfig.setAdvanceTypeConfigDo(financeDAO.queryAdvanceTypeConfig(advanceTypeCondition));
			}
		}
		return retSpConfig;
	}
	
	public void c2cTransferDirectly(UserAccountRollDO fromUser,UserAccountRollDO toUser){
		//userAccountFacade.userAccountTranfer(fromUser, toUser);
	}
	
	public void bindUser(UserBindDO userBindDO){
		financeDAO.insertUserBind(userBindDO);
	}
	
	public void updateTradeOrder(TradeOrderDO tradeOrderDO){
		financeDAO.updateTradeOrder(tradeOrderDO);
	}
	public void createTradeOrder(TradeOrderDO tradeOrderDO){
		financeDAO.insertTradeOrder(tradeOrderDO);
	}
	
	public UserBindDO queryUserBindInfo(UserBindDO userBindDO){
		UserBindDO condition = new UserBindDO();
		return financeDAO.queryUserBindInfo(condition);
	}
	public SpBizConfigDO querySpBizConfig(String spid,String bizCode){
		SpBizConfigDO spBizCondition = new SpBizConfigDO();
		spBizCondition.setSpid(spid);
		spBizCondition.setBizCode(bizCode);
		return financeDAO.querySpBizConfig(spBizCondition);
	}
	public AdvanceTypeConfigDO queryAdvanceConfig(int advanceId){  //查询垫子信息
		AdvanceTypeConfigDO advanceTypeCondition = new AdvanceTypeConfigDO();
		advanceTypeCondition.setAdvanceId(advanceId);
		return financeDAO.queryAdvanceTypeConfig(advanceTypeCondition);
	}
	
	public Map<String, String> getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}

	public FinanceDAO getFinanceDAO() {
		return financeDAO;
	}

	public void setFinanceDAO(FinanceDAO financeDAO) {
		this.financeDAO = financeDAO;
	}

	public UserAccountFacade getUserAccountFacade() {
		return userAccountFacade;
	}

	public void setUserAccountFacade(UserAccountFacade userAccountFacade) {
		this.userAccountFacade = userAccountFacade;
	}
	
}
