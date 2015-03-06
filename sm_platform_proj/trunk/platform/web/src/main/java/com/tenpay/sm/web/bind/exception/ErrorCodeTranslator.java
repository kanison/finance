package com.tenpay.sm.web.bind.exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tenpay.sm.lang.config.ReloadableAppConfig;
import com.tenpay.sm.lang.error.ErrorCode;

/**
 * 错误码处理器
 * @author kansonzhang
 *
 */
public class ErrorCodeTranslator {
	
	private static final Log log = LogFactory.getLog(ErrorCodeTranslator.class);
	private static final ReloadableAppConfig appconfig = new ReloadableAppConfig();
	private static final String UNKNOWN_ERRORCODE = "88888888";
	private static final String SYSTEM_ERRORCODE = "66666666";
	/**
	 * 生成页面显示的错误信息
	 * @return
	 */
	public static ErrorCode translateErrorCode(HttpServletRequest request,ErrorCode errorCode){
		
		//不需转译，直接返回
		String ignoreTranslate = StringUtils.trimToNull(appconfig.get("ignoreTranslateErrorCode"));
		if(null == ignoreTranslate || ignoreTranslate.equalsIgnoreCase("true")){
			String logErrorCode = StringUtils.trimToNull(appconfig.get("needLogErrorCode"));
			try {
				if(logErrorCode!=null&&logErrorCode.equalsIgnoreCase("true")&&errorCode!=null){
					log.info("retcode:"+errorCode.getRetcode()+" retmsg:"+errorCode.getMessage());
				}
			} catch (Exception e) {
				log.warn(e);
			}
			return errorCode;
		}
		if(errorCode == null || StringUtils.trimToNull(errorCode.getRetcode()) == null ){
			log.info("errorCode is null ");
			//这里需要确定返回什么?????????
			return errorCode;
		}			
		if(request == null){
			log.info("unExpected: HttpServletRequest is null!");
			return errorCode;
		}
		//需要转译
		String uriStr = request.getRequestURI();
		int beginIndex = uriStr.lastIndexOf("/");
		int endIndex = uriStr.lastIndexOf(".");
		if(endIndex<=beginIndex){
			log.info("not find cgi name,URI is ：" + uriStr);
			return errorCode;
		}
		log.debug("URI is : " + uriStr);
		String cgiName = uriStr.substring(beginIndex+1, endIndex);
		//查询cgi所属模块
		String model = appconfig.get(cgiName);
		if(model == null || model.equals("")){
			log.debug("没有找到该CGI所对应的模块,需要进行配置"+cgiName);
			return errorCode;
		}
		//如果该CGI是供UI使用(不是对外接口),无需转译错误码,直接返回
		if(model.trim().equalsIgnoreCase("UI"))
			return errorCode;
		//从配置文件中获取转译之后的错误码
		ErrorCode retErrorCode = new ErrorCode();
		try {
			retErrorCode = findErrorCode(model,errorCode);
		} catch (Exception e) {
			log.warn("findErrorCode Exception,retcode:" + errorCode.getRetcode());
			return errorCode;
		}
		
		return retErrorCode;
		
	}
	
	public static ErrorCode findErrorCode(String model,ErrorCode errorCode){
		ErrorCode retErrorCode = new ErrorCode();
		//从私有错误码中找
		String cfg = appconfig.get(model+"_"+errorCode.getRetcode());
		//如果私有中没有找到，从公共中找
		if(StringUtils.trimToNull(cfg) == null){
			cfg = appconfig.get("common"+"_"+errorCode.getRetcode());
		}
		//如果没有该错误码的转译信息，判读是未知错误码还是系统错误
		String code = errorCode.getRetcode();
		String message = errorCode.getMessage();
		if(StringUtils.trimToNull(cfg) == null){
			log.info("errorCode:"+code+" errorMessage:"+message);
			//判断是否是系统错误
			if(code!= null && code.length() == 8 && "1".equals(code.substring(3, 4))){
				String sysErrorCode = appconfig.get("SYSTEM_ERRORCODE");
				if(StringUtils.trimToNull(sysErrorCode) != null)
					retErrorCode.setRetcode(sysErrorCode);
				else
					retErrorCode.setRetcode(SYSTEM_ERRORCODE);
				retErrorCode.setMessage(message);
			}else{//未知错误
				String unknownErrorCode = appconfig.get("UNKNOWN_ERRORCODE");
				if(StringUtils.trimToNull(unknownErrorCode) != null)
					retErrorCode.setRetcode(unknownErrorCode);
				else
					retErrorCode.setRetcode(UNKNOWN_ERRORCODE);
				retErrorCode.setMessage(message);
			}
		}else{//配置文件中找到相应的错误码信息
			String str[] = cfg.split(";");
			retErrorCode.setRetcode(str[0]);
			//防止配置疏忽，只配置了错误码，没有配置错误信息
			if(str.length < 2) //返回原有错误信息
				retErrorCode.setMessage(message);
			else
				retErrorCode.setMessage(str[1]);
		}
		retErrorCode.setMessage("["+retErrorCode.getRetcode()+"]["+code+"]"+
				retErrorCode.getMessage()+"["+getTimeStamp()+"]");
		return retErrorCode;
	}
	public static String getTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");
		Calendar calendar = Calendar.getInstance();
		return sdf.format(calendar.getTime());
	}
}
