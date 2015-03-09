package com.zhaocb.zcb_app.finance.service.finance;

import java.util.Map;

import com.app.utils.CommonUtil;
import com.zhaocb.zcb_app.finance.service.dao.FinanceDAO;
import com.zhaocb.zcb_app.finance.service.facade.FinanceFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;

public class FinanceFacadeImp implements FinanceFacade {
	private Map<String, String> appConfig;
	private FinanceDAO financeDAO;
	
	
	public SpConfigDO querySpConfig(String spid,String bizCode){
		SpConfigDO spCondition = new SpConfigDO();
		spCondition.setSpid(spid);
		SpConfigDO retSpConfig = financeDAO.querySpConfig(spCondition);
		if(CommonUtil.trimString(bizCode) !=null && retSpConfig != null ){
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
	public void createTradeOrder(TradeOrderDO TradeOrderDO){
		
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
	
}
