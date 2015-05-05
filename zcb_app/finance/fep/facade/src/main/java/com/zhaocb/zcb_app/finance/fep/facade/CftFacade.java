package com.zhaocb.zcb_app.finance.fep.facade;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryOutput;

/**
 * �Ƹ�ͨ�ӿ�
 * 
 * @author Honly
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

}
