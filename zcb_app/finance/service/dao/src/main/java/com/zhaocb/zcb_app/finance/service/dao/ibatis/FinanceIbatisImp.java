package com.zhaocb.zcb_app.finance.service.dao.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.zcb_app.finance.service.dao.FinanceDAO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpBizConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.TradeOrderDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserBindDO;

public class FinanceIbatisImp extends SqlMapClientDaoSupport implements
		FinanceDAO {
	private static final Log LOG = LogFactory.getLog(FundIbatisImpl.class);

	public SpConfigDO querySpConfig(SpConfigDO spConfigDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (SpConfigDO) client.queryForObject("querySpConfig", spConfigDO);
	}

	public SpBizConfigDO querySpBizConfig(SpBizConfigDO spBizConfigDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (SpBizConfigDO) client.queryForObject("querySpBizConfig",spBizConfigDO);
	}

	public AdvanceTypeConfigDO queryAdvanceTypeConfig(
			AdvanceTypeConfigDO advanceTypeConfigDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (AdvanceTypeConfigDO) client.queryForObject("querySpConfig",advanceTypeConfigDO);
	}

	public void insertTradeOrder(TradeOrderDO tradeOrderDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		client.insert("insertTradeOrder", tradeOrderDO);
	}

	public UserBindDO queryUserBindInfo(UserBindDO userBindDO) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (UserBindDO) client.queryForObject("queryUserBindDO", userBindDO);
	}

}