package com.zhaocb.zcb_app.finance.fep.dao.ibatis;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.zcb_app.finance.fep.dao.FepDAO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BankInfo;

public class FepIbatisImpl extends SqlMapClientDaoSupport implements FepDAO {

	private static final Log LOG = LogFactory.getLog(FepIbatisImpl.class);

	public BankInfo queryBankInfoByCode(String bankCode) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (BankInfo) client.queryForObject("queryBankInfoByCode", bankCode);
	}

	public long queryAreaCityByCode(Map paramMap) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (Long) client.queryForObject("queryAreaCityByCode", paramMap);
	}

	

}