package com.zhaocb.zcb_app.bill.service.bill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhaocb.zcb_app.bill.service.dao.BillDAO;
import com.zhaocb.zcb_app.bill.service.facade.BillFacade;
import com.zhaocb.zcb_app.bill.service.facade.dataobject.GenBillInput;

public class BillFacadeImpl implements BillFacade {

	private BillDAO billDAO;
	
	private static final Log LOG = LogFactory.getLog(BillFacade.class);

	public String genBillNo(GenBillInput billInput) {
		LOG.info("method genBillNo");
		
		
		
		return null;
	}

	public BillDAO getBillDAO() {
		return billDAO;
	}

	public void setBillDAO(BillDAO billDAO) {
		this.billDAO = billDAO;
	}
}
