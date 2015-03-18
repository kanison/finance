package com.zhaocb.app.website.web.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.utils.CommonUtil;
import com.zhaocb.app.website.web.exception.WebServiceRetException;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.AdvanceTypeConfigDO;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.SpConfigDO;

public class FinanceWebComm {
	public static int BINDID_APPID = 1;
	public static int LISTID_APPID = 1;
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
		// TODO �Ƿ���Ҫ�ж������ʽ�, �Ƿ����̻�ֻ���Լ����ʽ��õ���ϵͳ�ĸ����
		AdvanceTypeConfigDO advanceTypeDO = spConfigDO.getAdvanceTypeConfigDo();
		BigDecimal canUseMoney = advanceTypeDO.getCreditQuoto().divide(advanceTypeDO.getUsedCreditQuoto()).divide(advanceTypeDO.getCreditQuotoOffset());
		if(canUseMoney.compareTo(totalMoney) < 0){
			LOG.info("�̻��������ö�Ȳ��㣬ʣ���� canUseMoney="+canUseMoney);
			throw new WebServiceRetException(WebServiceRetException.APP_CREDIT_QUOTO_NOT_ENOUGH,"�̻����ö�Ȳ���");
		}
	}

	public static String genBindId(){
		long pre = System.currentTimeMillis()%10000;
		String serialNo = CommonUtil.getBillNO(BINDID_APPID);
		return pre + serialNo;
	}
	
	public static String genListId(String spid){
		String date = CommonUtil.formatDate(new Date(),"yyyyMMdd");
		return spid + date + CommonUtil.getBillNO(LISTID_APPID);
	}
	
	public static void main(String args[]){
		long pre = System.currentTimeMillis()%10000;
		String b="1234567890";
		System.out.println(pre+b);
		
		String date = CommonUtil.formatDate(new Date(),"yyyyMMdd");
		String spid = "1000000000";
		System.out.println( spid + date + CommonUtil.getBillNO(LISTID_APPID));
		
	}
}