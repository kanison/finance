package com.zhaocb.zcb_app.finance.fep.facade;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryOutput;

/**
 * 财付通接口
 * 
 * @author zhl
 *
 */
public interface CftFacade {

	/**
	 * 批量向用户银行卡付款
	 * 
	 * @param batchDrawDO
	 * @return
	 * @throws Exception
	 */
	public BatchDrawOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception;

	/**
	 * 查询批量付款结果
	 * 
	 * @param batchDrawQueryDO
	 * @return
	 * @throws Exception
	 */
	public BatchDrawQueryOutput batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO)
			throws Exception;

	/**
	 * 查询商户付款退票结果
	 * 
	 * @param payRefundQueryDO
	 * @return
	 * @throws Exception
	 */
	public PayRefundQueryOutput payRefundQuery(PayRefundQueryDO payRefundQueryDO)
			throws Exception;

}
