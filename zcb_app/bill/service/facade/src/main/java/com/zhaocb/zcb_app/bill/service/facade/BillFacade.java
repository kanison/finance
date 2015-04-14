package com.zhaocb.zcb_app.bill.service.facade;

import com.zhaocb.zcb_app.bill.service.facade.dataobject.GenBillInput;


public interface BillFacade {

	public String genBillNo(GenBillInput billInput);
	
	
}
