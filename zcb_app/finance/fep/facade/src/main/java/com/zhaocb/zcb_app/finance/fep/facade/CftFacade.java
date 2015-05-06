package com.zhaocb.zcb_app.finance.fep.facade;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryOutput;

/**
 * �Ƹ�ͨ�ӿ�
 * 
 * @author zhl
 *
 */
public interface CftFacade {

	/**
	 * �������û����п�����
	 * 
	 * @param batchDrawDO
	 * @return
	 * @throws Exception
	 */
	public BatchDrawOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception;

	/**
	 * ��ѯ����������
	 * 
	 * @param batchDrawQueryDO
	 * @return
	 * @throws Exception
	 */
	public BatchDrawQueryOutput batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO)
			throws Exception;

	/**
	 * ��ѯ�̻�������Ʊ���
	 * 
	 * @param payRefundQueryDO
	 * @return
	 * @throws Exception
	 */
	public PayRefundQueryOutput payRefundQuery(PayRefundQueryDO payRefundQueryDO)
			throws Exception;

}
