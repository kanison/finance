package com.zhaocb.app.website.web.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.app.utils.CommonUtil;
//import com.tenpay.common.relay.RelayException;
//import com.tenpay.common.relay.RelayServiceFacade;
//import com.tenpay.common.relay.dataobject.TenpayOrder;
//import com.tenpay.common.web.exception.ParameterInvalidException;
//import com.tenpay.mch_app.fundapi.mchapi.web.model.CftBuyFundNotifyInput;
import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.zhaocb.app.website.web.exception.FundMchapiWebRetException;


public class FundWebCommon {
	private static Logger LOG = Logger.getLogger(FundWebCommon.class);

	public static String getAppConfig(String key) {
		return CommonUtil.trimString(ReloadableAppConfig.appConfig.get(key));
	}
	
	/**
	 * ��ȡtomcat/conf/app-config.properties�����ļ�
	 * @param key
	 * @return string
	 * @author zhl
	 */
	public static String getWebConfig(String key){
		Map<String,String> map = System.getenv();		
		String tomcatHome = map.get("CATALINA_HOME");				
		Properties prop = new Properties();   
		String property;
        try {   
        	InputStream in = new BufferedInputStream(new FileInputStream(tomcatHome +"/conf/app-config.properties"));
            prop.load(in);   
            property = CommonUtil.trimString(prop.getProperty(key));   
        } catch (Exception e) {   
        	LOG.warn("read exception ", e);
			return null;
        }   
        
        return property;
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static String getSignStr(Object object){
		Map<String,String> sortedMap = CommonUtil.ObjToPlainSortedMap(object);
		String signStr;
		try {
			signStr = CommonUtil.formatQueryParaMap(sortedMap, false,
					null);
		} catch (Exception e1) {
			LOG.warn("formatQueryParaMap exception", e1);
			return null;
		}
		return signStr;		
	}
	// ֱ�����
		public static void outPut(String retStr, String outputCharset) {

			HttpServletResponse response = CommonUtil.getHttpServletResponse();
			response.setCharacterEncoding(outputCharset);
			response.setContentType("text/html;charset=" + outputCharset);
			PrintWriter printWriter = null;
			try {
				printWriter = response.getWriter();
				printWriter.print(retStr);
				printWriter.flush();
			} catch (Exception e) {
				LOG.info("printWriter error ", e);
				throw new FundMchapiWebRetException(
						FundMchapiWebRetException.SYSTEM_ERROR, "ϵͳ�ڲ�����");
			} finally {
				if (printWriter != null)
					printWriter.close();
			}

		}
		
		/*
		*//**
		 * ���֪ͨ���ݵĺϷ���     �Ƹ�ͨ����״̬��2֧���ɹ�  7�˿�ɹ�
		 * @param relayServiceFacade
		 * @param finishPayInputDO
		 * @return
		 *//*
		public static boolean verifyPayNotifyContent(
				RelayServiceFacade relayServiceFacade,
				CftBuyFundNotifyInput notifyInput) {
			if(notifyInput == null || notifyInput.getPartner() == null || notifyInput.getTransaction_id() == null){
				throw new ParameterInvalidException("֪ͨ�����쳣");
			}
			TenpayOrder tenpayOrder = relayServiceFacade
					.getTenpayOrderByMerIDAndTenpayOID(notifyInput
							.getPartner(), notifyInput
							.getTransaction_id(), 2);
			if (tenpayOrder != null) {
				if (tenpayOrder.getTrade_status() == null
						|| (!tenpayOrder.getTrade_status().equals("2") && !tenpayOrder
								.getTrade_status().equals("7"))) {
					// ��ʵʱ�ӿ�״̬���������֧�����Ĳ�ʵʱ�ӿ�
					tenpayOrder = relayServiceFacade
							.getTenpayOrderByMerIDAndTenpayOID(notifyInput
									.getPartner(), notifyInput
									.getTransaction_id(), 0);
				}
				if (tenpayOrder.getTrade_status() != null) {
					if (tenpayOrder.getTrade_status().equals("2")) {
						try {
							if (notifyInput.getTotal_fee().equals(
									tenpayOrder.getTotal_fee())
									&& notifyInput.getPartner().equals(
											tenpayOrder.getBargainor_id())
									&& notifyInput.getOut_trade_no().equals(
											tenpayOrder.getSp_billno())
									&& notifyInput.getTransaction_id().equals(
											tenpayOrder.getTransaction_id())) {
								notifyInput.setUid(tenpayOrder.getPurchaser_uid()); //�û�uid
								return true;
							}
						} catch (Exception t) {
							LOG.warn("verify tenpay notify ex:", t);
						}
					} else if (tenpayOrder.getTrade_status().equals("7"))
						return false;
					throw new ParameterInvalidException("У�鵥״̬����ȷ");
				} else
					throw new ParameterInvalidException("У�鵥״̬Ϊ��");
			}
			throw new ParameterInvalidException("֧���ɹ�֪ͨ������֤ʧ��");
		}
		*//**
		 * ��֤֧������  �����payPasswd ���뱣֤�� 276λ
		 * @param payPasswd
		 * @param vali_type
		 * @param uin
		 * @param relayServiceFacade
		 *//*
		public static void checkPayPasswd(String payPasswd, int vali_type,
				String uin, RelayServiceFacade relayServiceFacade) {
			// ��֤֧������
			if(payPasswd == null || payPasswd.length()!=276){
				LOG.info("ʱ���ʽ����payPasswd="+payPasswd);
				throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
						"���������ע��������ĸ��ϼ���Сд��");
			}
			String tmp = payPasswd.substring(0, 20);
			try {
				tmp = CommonUtil.parseTmp(tmp.toCharArray());
			} catch (Exception e) {
				LOG.info("ʱ���ʽ����20λʱ������ת��Ϊ10λʱ����", e);
				throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
						"���������ע��������ĸ��ϼ���Сд��");
			}
			
			// �õ���֧��������rsa2��mac �� disk ����ʽ����֤ʱ��vali_typeΪ3
			try {
				relayServiceFacade.verifyPaypassword(uin,
						payPasswd.substring(20), tmp, vali_type);
			} catch (RelayException e) {
				if(e.getErrorCode().getRetcode().equals("1003")){
					throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
					"���������ע��������ĸ��ϼ���Сд��");	
				}
				throw new FundMchapiWebRetException(e.getErrorCode().getRetcode(),
						e.getErrorCode().getMessage());
			}
		}
*/
		
}
