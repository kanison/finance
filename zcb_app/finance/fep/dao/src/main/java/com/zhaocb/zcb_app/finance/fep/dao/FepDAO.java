package com.zhaocb.zcb_app.finance.fep.dao;

import java.util.Map;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BankInfo;

public interface FepDAO {

	public BankInfo queryBankInfoByCode(String bankCode);
	
	public long queryAreaCityByCode(Map paramMap);
	
}
