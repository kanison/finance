package com.zhaocb.zcb_app.bill.service.bill;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.app.common.exception.ParameterInvalidException;
import com.app.utils.CommonUtil;
import com.zhaocb.zcb_app.bill.service.dao.BillDAO;
import com.zhaocb.zcb_app.bill.service.exception.BillServiceRetException;
import com.zhaocb.zcb_app.bill.service.facade.BillFacade;
import com.zhaocb.zcb_app.bill.service.facade.dataobject.AppIdInfo;
import com.zhaocb.zcb_app.bill.service.facade.dataobject.GenBillInput;

/**
 * 
 * @author zhl
 *
 */
public class BillFacadeImpl implements BillFacade {

	private BillDAO billDAO;
	private static Map<String,Integer> curSeqNoMap;
	private static Map<String,Integer> maxSeqNoMap;
	private static Map<String,Integer> startNoMap;
	private static String macNo;
	
	private static final Log LOG = LogFactory.getLog(BillFacade.class);

	public String genBillNo(GenBillInput billInfo) {
		LOG.info("method genBillNo");
		
		//������
		checkParam(billInfo);
		
		//��ȡ��ǰ10λ���		
		String seqNo = getSeqNo(billInfo);
		
		//���������ɵ���
		String billNo = setBillNo(billInfo, seqNo);
		
		return billNo;
	}

	/**
	 * ���������ɵ��� 
	 * listType!=0,����+spId(10λ)+8λ����+10λ���ɵ���ţ�
	 * listType=0,ֱ�ӷ���10λ���ɵ����
	 * @param listType
	 * @param billNo
	 * @return
	 */
	private String setBillNo(GenBillInput billInfo, String seqNo) {		
		StringBuffer billNo = new StringBuffer();
		String listType = billInfo.getListType();
		
		if("0".equals(listType)){
			billNo.append(seqNo) ;
		} else {			
			String spId = billInfo.getSpId();
			String curDate = CommonUtil.formatDate(new Date(), "yyyyMMdd");
			billNo.append(listType).append(spId).append(curDate).append(seqNo);
		}		
		
		return billNo.toString();
	}

	/**
	 * ��ȡ��ǰ10λ���,1λ������+9λ�������,�߳�������֤׼ȷ��
	 * @param billInfo
	 * @return
	 */
	private synchronized String getSeqNo(GenBillInput billInfo) {
		String appId = billInfo.getAppId();	
	
		boolean needSave = true;
		if(null != curSeqNoMap && null != maxSeqNoMap){
			Integer curSeqNo = curSeqNoMap.get(appId);
			Integer maxSeqNo = maxSeqNoMap.get(appId);
			
			//��ǰӦ������Ѿ����� �����������������������
			if (null != curSeqNo && null != maxSeqNo) {
				curSeqNo += 1;
				if (curSeqNo <= maxSeqNo) {
					curSeqNoMap.put(appId, curSeqNo);
					needSave = false;
				}
			} 	
		} else{//��һ�ε��ã����߷������������
			curSeqNoMap = new HashMap<String,Integer>();
			maxSeqNoMap = new HashMap<String,Integer>();
		}
		
		if(needSave){
			saveMaxSeqNoToFile(billInfo);
		}
		
		int curSeqNo = curSeqNoMap.get(appId);
		if(curSeqNo >= 600000000){
			throw new BillServiceRetException(BillServiceRetException.SEQ_LIMIT_ERROR, "�����Ų��ܴ��ڵ���6��");
		}		
		
		String seqNo = macNo + formatSeqNo(curSeqNo);
		
		return seqNo;
		
	}

	/**
	 * �����ļ�������
	 * @param appId
	 */
	private void saveMaxSeqNoToFile(GenBillInput billInfo){		
		String appId = billInfo.getAppId();	
		String appIdFilePath = CommonUtil.getWebConfig("appIdFilePath");// ��������ȡ�ļ�·��
		String appIdFileName = new StringBuffer(appIdFilePath).append("/").append(appId).append(".txt").toString();
		
		File appIdFile = new File(appIdFileName);			
		int maxSeqNo = 1000;
		try {
			//�ļ�������
			if(!appIdFile.exists()){
				File fileDir = appIdFile.getParentFile();
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}			
				
				maxSeqNo += startNoMap.get(appId);
				
			} else{
				BufferedReader br = new BufferedReader(new FileReader(appIdFile));
				maxSeqNo += Integer.valueOf(CommonUtil.trimString(br.readLine()));
				br.close();
			}			
			
			int curSeqNo = maxSeqNo - 999;
			if(curSeqNo >= 600000000){
				throw new BillServiceRetException(BillServiceRetException.SYSTEM_ERROR, "��ȡ����ʧ��");
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(appIdFile));		
			bw.write(String.valueOf(maxSeqNo));
			bw.flush();
			bw.close();
		} catch (IOException e) {			
			throw new BillServiceRetException(BillServiceRetException.SYSTEM_ERROR, "��ȡ����ʧ��");
		}
		
		curSeqNoMap.put(appId, maxSeqNo - 1000);
		maxSeqNoMap.put(appId, maxSeqNo);	
	}

	/**
	 * ������
	 * @param billInfo
	 */
	private void checkParam(GenBillInput billInfo) {
		String listType = billInfo.getListType();
		String spId = billInfo.getSpId();
		String appId = billInfo.getAppId();
		if (null == CommonUtil.trimString(listType)) {
			throw new ParameterInvalidException("���Ͳ���Ϊ��");
		} else if (!listType.matches("0|101|102|103|104|105")) {
			throw new ParameterInvalidException("���Ͳ��Ϸ�");
		} else if (null == CommonUtil.trimString(appId)) {
			throw new ParameterInvalidException("Ӧ��ID����Ϊ��");
		} else if (!"0".equals(listType) && (null == spId || spId.length() != 10)) {
			throw new ParameterInvalidException("�̻��Ų��Ϸ�");
		} 
		
		// ��������ȡ1λ�����ţ����֧��10̨����
		macNo = CommonUtil.getWebConfig("macNo");
		if(null == macNo || macNo.length() != 1 ||  !"0123456789".contains(macNo)){
			throw new BillServiceRetException(BillServiceRetException.MAC_NO_ERROR, "���������ô���");
		}
		
		//Ч��appid,����ȡ��Ӧ��appid��ʼ�������
		AppIdInfo appIdInfo = billDAO.queryAppIdInfo(appId);
		if(null == appIdInfo){
			throw new ParameterInvalidException("Ӧ��ID���Ϸ�");
		}				
		if(null == startNoMap){
			startNoMap = new HashMap<String,Integer>();
		}
		startNoMap.put(appId, appIdInfo.getStartNo());
	}
	
	/**
	 * ��ʽ��9λ�������,����9λǰ���Զ���0
	 * @param seqNo
	 * @return
	 */
	private String formatSeqNo(Integer seqNo) {		
		return String.format("%09d", seqNo);
	}

	public BillDAO getBillDAO() {
		return billDAO;
	}

	public void setBillDAO(BillDAO billDAO) {
		this.billDAO = billDAO;
	}
}
