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
 * �����봦����
 * @author kansonzhang
 *
 */
public class ErrorCodeTranslator {
	
	private static final Log log = LogFactory.getLog(ErrorCodeTranslator.class);
	private static final ReloadableAppConfig appconfig = new ReloadableAppConfig();
	private static final String UNKNOWN_ERRORCODE = "88888888";
	private static final String SYSTEM_ERRORCODE = "66666666";
	/**
	 * ����ҳ����ʾ�Ĵ�����Ϣ
	 * @return
	 */
	public static ErrorCode translateErrorCode(HttpServletRequest request,ErrorCode errorCode){
		
		//����ת�룬ֱ�ӷ���
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
			//������Ҫȷ������ʲô?????????
			return errorCode;
		}			
		if(request == null){
			log.info("unExpected: HttpServletRequest is null!");
			return errorCode;
		}
		//��Ҫת��
		String uriStr = request.getRequestURI();
		int beginIndex = uriStr.lastIndexOf("/");
		int endIndex = uriStr.lastIndexOf(".");
		if(endIndex<=beginIndex){
			log.info("not find cgi name,URI is ��" + uriStr);
			return errorCode;
		}
		log.debug("URI is : " + uriStr);
		String cgiName = uriStr.substring(beginIndex+1, endIndex);
		//��ѯcgi����ģ��
		String model = appconfig.get(cgiName);
		if(model == null || model.equals("")){
			log.debug("û���ҵ���CGI����Ӧ��ģ��,��Ҫ��������"+cgiName);
			return errorCode;
		}
		//�����CGI�ǹ�UIʹ��(���Ƕ���ӿ�),����ת�������,ֱ�ӷ���
		if(model.trim().equalsIgnoreCase("UI"))
			return errorCode;
		//�������ļ��л�ȡת��֮��Ĵ�����
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
		//��˽�д���������
		String cfg = appconfig.get(model+"_"+errorCode.getRetcode());
		//���˽����û���ҵ����ӹ�������
		if(StringUtils.trimToNull(cfg) == null){
			cfg = appconfig.get("common"+"_"+errorCode.getRetcode());
		}
		//���û�иô������ת����Ϣ���ж���δ֪�����뻹��ϵͳ����
		String code = errorCode.getRetcode();
		String message = errorCode.getMessage();
		if(StringUtils.trimToNull(cfg) == null){
			log.info("errorCode:"+code+" errorMessage:"+message);
			//�ж��Ƿ���ϵͳ����
			if(code!= null && code.length() == 8 && "1".equals(code.substring(3, 4))){
				String sysErrorCode = appconfig.get("SYSTEM_ERRORCODE");
				if(StringUtils.trimToNull(sysErrorCode) != null)
					retErrorCode.setRetcode(sysErrorCode);
				else
					retErrorCode.setRetcode(SYSTEM_ERRORCODE);
				retErrorCode.setMessage(message);
			}else{//δ֪����
				String unknownErrorCode = appconfig.get("UNKNOWN_ERRORCODE");
				if(StringUtils.trimToNull(unknownErrorCode) != null)
					retErrorCode.setRetcode(unknownErrorCode);
				else
					retErrorCode.setRetcode(UNKNOWN_ERRORCODE);
				retErrorCode.setMessage(message);
			}
		}else{//�����ļ����ҵ���Ӧ�Ĵ�������Ϣ
			String str[] = cfg.split(";");
			retErrorCode.setRetcode(str[0]);
			//��ֹ���������ֻ�����˴����룬û�����ô�����Ϣ
			if(str.length < 2) //����ԭ�д�����Ϣ
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
