package com.zhaocb.zcb_app.bill.service.dao.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.zcb_app.bill.service.dao.BillDAO;

public class BillIbatisImpl extends SqlMapClientDaoSupport implements BillDAO {

	private static final Log LOG = LogFactory.getLog(BillIbatisImpl.class);

}