package com.zhaocb.zcb_app.finance.service.fund;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhaocb.zcb_app.finance.service.dao.FundDAO;
import com.zhaocb.zcb_app.finance.service.facade.FundAssistFacade;

public class FundAssistFacadeImpl implements FundAssistFacade {
	private static final Log LOG = LogFactory
			.getLog(FundAssistFacadeImpl.class);
	private FundDAO fundDAO;

}
