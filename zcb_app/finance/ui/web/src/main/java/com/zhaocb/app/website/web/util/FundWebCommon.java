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
	 * 读取tomcat/conf/app-config.properties配置文件
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
	// 直接输出
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
						FundMchapiWebRetException.SYSTEM_ERROR, "系统内部错误");
			} finally {
				if (printWriter != null)
					printWriter.close();
			}

		}
		
		/*
		*//**
		 * 检查通知内容的合法性     财付通订单状态，2支付成功  7退款成功
		 * @param relayServiceFacade
		 * @param finishPayInputDO
		 * @return
		 *//*
		public static boolean verifyPayNotifyContent(
				RelayServiceFacade relayServiceFacade,
				CftBuyFundNotifyInput notifyInput) {
			if(notifyInput == null || notifyInput.getPartner() == null || notifyInput.getTransaction_id() == null){
				throw new ParameterInvalidException("通知参数异常");
			}
			TenpayOrder tenpayOrder = relayServiceFacade
					.getTenpayOrderByMerIDAndTenpayOID(notifyInput
							.getPartner(), notifyInput
							.getTransaction_id(), 2);
			if (tenpayOrder != null) {
				if (tenpayOrder.getTrade_status() == null
						|| (!tenpayOrder.getTrade_status().equals("2") && !tenpayOrder
								.getTrade_status().equals("7"))) {
					// 非实时接口状态不是已完成支付，改查实时接口
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
								notifyInput.setUid(tenpayOrder.getPurchaser_uid()); //用户uid
								return true;
							}
						} catch (Exception t) {
							LOG.warn("verify tenpay notify ex:", t);
						}
					} else if (tenpayOrder.getTrade_status().equals("7"))
						return false;
					throw new ParameterInvalidException("校验单状态不正确");
				} else
					throw new ParameterInvalidException("校验单状态为空");
			}
			throw new ParameterInvalidException("支付成功通知内容验证失败");
		}
		*//**
		 * 验证支付密码  传入的payPasswd 必须保证是 276位
		 * @param payPasswd
		 * @param vali_type
		 * @param uin
		 * @param relayServiceFacade
		 *//*
		public static void checkPayPasswd(String payPasswd, int vali_type,
				String uin, RelayServiceFacade relayServiceFacade) {
			// 验证支付密码
			if(payPasswd == null || payPasswd.length()!=276){
				LOG.info("时间格式错误，payPasswd="+payPasswd);
				throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
						"密码错误，请注意数字字母组合及大小写。");
			}
			String tmp = payPasswd.substring(0, 20);
			try {
				tmp = CommonUtil.parseTmp(tmp.toCharArray());
			} catch (Exception e) {
				LOG.info("时间格式错误，20位时间种子转换为10位时出错", e);
				throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
						"密码错误，请注意数字字母组合及大小写。");
			}
			
			// 用到的支付密码是rsa2有mac 和 disk 的形式，验证时的vali_type为3
			try {
				relayServiceFacade.verifyPaypassword(uin,
						payPasswd.substring(20), tmp, vali_type);
			} catch (RelayException e) {
				if(e.getErrorCode().getRetcode().equals("1003")){
					throw new FundMchapiWebRetException(FundMchapiWebRetException.PAY_PASSWD_ERROR,
					"密码错误，请注意数字字母组合及大小写。");	
				}
				throw new FundMchapiWebRetException(e.getErrorCode().getRetcode(),
						e.getErrorCode().getMessage());
			}
		}
*/
		
}
