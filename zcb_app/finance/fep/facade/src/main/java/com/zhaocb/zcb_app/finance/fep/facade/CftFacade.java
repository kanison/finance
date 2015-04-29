package com.zhaocb.zcb_app.finance.fep.facade;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.CftCommOutput;

public interface CftFacade {

	/**
	 * 批量向用户银行卡付款
	 * 
	 * @param batchDrawDO
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public CftCommOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception;

	/**
	 * 查询批量付款结果
	 * 
	 * @param batchDrawQueryDO
	 * @return
	 */
	public String batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO);

}
