package com.zhaocb.app.website.web.util;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhaocb.app.website.web.exception.WebServiceRetException;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;

public class FinanceWebComm {
	private static Log LOG = LogFactory.getLog(FinanceWebComm.class);
	/**
	 *  ������ö��
	 *  ϵͳ����ʹ�õ������ж�ȣ����ж��ʹ�����ʹ�õ��ʶ�ȣ���������ֻ��Ҫ�����ʶ���Ƿ����
	 * @param spConfigDO
	 * @param totalMoney
	 */
	public static void checkCreditBalance(SpConfigDO spConfigDO,BigDecimal totalMoney){
		if(spConfigDO == null || spConfigDO.getAdvanceTypeConfigDo() == null){
			LOG.info("��ѯ�̻���Ϣ���󣬲�ѯ���spConfigDO ���� spConfigDO Ϊ��");
			throw new WebServiceRetException(WebServiceRetException.UNEXPECTED_ERR,"ϵͳ��æ�����Ժ�����");
		}
		AdvanceTypeConfigDO advanceTypeDO = spConfigDO.getAdvanceTypeConfigDo();
		BigDecimal canUseMoney = advanceTypeDO.getCreditQuoto().divide(advanceTypeDO.getUsedCreditQuoto()).divide(advanceTypeDO.getCreditQuotoOffset());
		if(canUseMoney.compareTo(totalMoney) < 0){
			LOG.info("�̻��������ö�Ȳ��㣬ʣ���� canUseMoney="+canUseMoney);
			throw new WebServiceRetException(WebServiceRetException.APP_CREDIT_QUOTO_NOT_ENOUGH,"�̻����ö�Ȳ���");
		}
	}

	
}