package com.zhaocb.zcb_app.bill.service.dao.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.zcb_app.bill.service.dao.BillDAO;
import com.zhaocb.zcb_app.bill.service.facade.dataobject.AppIdInfo;

public class BillIbatisImpl extends SqlMapClientDaoSupport implements BillDAO {

	private static final Log LOG = LogFactory.getLog(BillIbatisImpl.class);

	public AppIdInfo queryAppIdInfo(String appId) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (AppIdInfo) client.queryForObject("queryAppIdInfo", appId);
	}

}