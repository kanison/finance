package com.zhaocb.zcb_app.finance.fep.facade;

import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.CftCommOutput;

public interface CftFacade {

	/**
	 * �������û����п�����
	 * 
	 * @param batchDrawDO
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public CftCommOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception;

	/**
	 * ��ѯ����������
	 * 
	 * @param batchDrawQueryDO
	 * @return
	 */
	public String batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO);

}
