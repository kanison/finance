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
	
	private static final Log LOG = LogFactory.getLog(BillFacade.class);

	public String genBillNo(GenBillInput billInput) {
		LOG.info("method genBillNo");
		
		//检查参数
		checkParam(billInput);
		
		//获取当前10位序号		
		String seqNo = getSeqNo(billInput);
		
		//按规则生成单号
		String billNo = setBillNo(billInput, seqNo);
		
		return billNo;
	}

	/**
	 * 按规则生成单号 
	 * listType!=0,类型+spId(10位)+8位日期+10位生成的序号；
	 * listType=0,直接返回10位生成的序号
	 * @param listType
	 * @param billNo
	 * @return
	 */
	private String setBillNo(GenBillInput billInput, String seqNo) {		
		StringBuffer billNo = new StringBuffer();
		String listType = billInput.getListType();
		
		if("0".equals(listType)){
			billNo.append(seqNo) ;
		} else {			
			String spId = billInput.getSpId();
			String curDate = CommonUtil.formatDate(new Date(), "yyyyMMdd");
			billNo.append(listType).append(spId).append(curDate).append(seqNo);
		}		
		
		return billNo.toString();
	}

	/**
	 * 获取当前10位序号,1位机器号+9位自增序号,线程锁定保证准确性
	 * @param billInput
	 * @return
	 */
	private synchronized String getSeqNo(GenBillInput billInput) {
		String appId = billInput.getAppId();	
	
		boolean needSave = true;
		if(null != curSeqNoMap && null != maxSeqNoMap){
			Integer curSeqNo = curSeqNoMap.get(appId);
			Integer maxSeqNo = maxSeqNoMap.get(appId);
			
			//当前应用序号已经生成 如果大于最大序号则重新生成
			if (null != curSeqNo && null != maxSeqNo) {
				curSeqNo += 1;
				if (curSeqNo <= maxSeqNo) {
					curSeqNoMap.put(appId, curSeqNo);
					needSave = false;
				}
			} 	
		} else{//第一次调用，或者服务重启的情况
			curSeqNoMap = new HashMap<String,Integer>();
			maxSeqNoMap = new HashMap<String,Integer>();
		}
		
		if(needSave){
			saveMaxSeqNoToFile(billInput);
		}
		
		int curSeqNo = curSeqNoMap.get(appId);
		if(curSeqNo > 600000000){
			throw new BillServiceRetException(BillServiceRetException.SEQ_LIMIT_ERROR, "最大序号不能超过6亿");
		}
		
		// 从配置中取1位机器号，最多支持10台机器
		String macNo = CommonUtil.getWebConfig("macNo");
		String seqNo = macNo + formatSeqNo(curSeqNo);
		
		return seqNo;
		
	}

	/**
	 * 更新文件最大序号
	 * @param appId
	 */
	private void saveMaxSeqNoToFile(GenBillInput billInput){		
		String appId = billInput.getAppId();	
		String appIdFilePath = CommonUtil.getWebConfig("appIdFilePath");// 从配置中取文件路径
		String appIdFileName = new StringBuffer(appIdFilePath).append("/").append(appId).append(".txt").toString();
		
		File appIdFile = new File(appIdFileName);			
		int maxSeqNo = 1000;
		try {
			//文件不存在
			if(!appIdFile.exists()){
				File fileDir = appIdFile.getParentFile();
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}			
				
				maxSeqNo += billInput.getAppIdInfo().getStartNo();
				
			} else{
				BufferedReader br = new BufferedReader(new FileReader(appIdFile));
				maxSeqNo += Integer.valueOf(CommonUtil.trimString(br.readLine()));
				br.close();
			}			
			
			int curSeqNo = maxSeqNo - 999;
			if(curSeqNo >= 600000000){
				throw new BillServiceRetException(BillServiceRetException.SYSTEM_ERROR, "获取单号失败");
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(appIdFile));		
			bw.write(String.valueOf(maxSeqNo));
			bw.flush();
			bw.close();
		} catch (IOException e) {			
			throw new BillServiceRetException(BillServiceRetException.SYSTEM_ERROR, "获取单号失败");
		}
		
		curSeqNoMap.put(appId, maxSeqNo - 999);
		maxSeqNoMap.put(appId, maxSeqNo);	
	}

	/**
	 * 检查参数
	 * @param billInput
	 */
	private void checkParam(GenBillInput billInput) {
		String listType = billInput.getListType();
		String spId = billInput.getSpId();
		String appId = billInput.getAppId();
		if (null == CommonUtil.trimString(listType)) {
			throw new ParameterInvalidException("类型不能为空");
		} else if (!listType.matches("0|101|102|103|104|105")) {
			throw new ParameterInvalidException("类型不合法");
		} else if (null == CommonUtil.trimString(appId)) {
			throw new ParameterInvalidException("应用ID不能为空");
		} else if (!"0".equals(listType) && (null == spId || spId.length() != 10)) {
			throw new ParameterInvalidException("商户号不合法");
		} 
		
		//效验appid,并获取对应的appid起始自增序号
		AppIdInfo appIdInfo = billDAO.queryAppIdInfo(appId);
		if(null == appIdInfo){
			throw new ParameterInvalidException("应用ID不合法");
		}		
		billInput.setAppIdInfo(appIdInfo);
	}
	
	/**
	 * 格式化9位自增序号,少于9位前面自动补0
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
