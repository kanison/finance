/**
 * 
 */
package com.tenpay.sm.lang.error;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.tenpay.sm.lang.config.ReloadableAppConfig;

/**
 * @author torryhong
 * 
 */
public class ErrorCode implements java.io.Serializable {
	private static final long serialVersionUID = -7767059598469242922L;
	private static final ErrorCode success = new ErrorCode("common", 0);

	public static ErrorCode success() {
		return success;
	}

	public static ErrorCode code(Object code) {
		return new ErrorCode(code);
	}

	public static ErrorCode code(String category, Object code) {
		return new ErrorCode(category, code);
	}

	public static ErrorCode code(String category, Object code, String[] args) {
		return new ErrorCode(category, code, args);
	}

	public ErrorCode() {
	}

	public ErrorCode(Object code) {
		this();
		this.code = code;
	}

	public ErrorCode(String category, Object code) {
		this(code);
		this.category = category;
	}

	public ErrorCode(String category, Object code, String[] args) {
		this(category, code);
		this.args = args;
	}

	private String category = "default";
	private Object code;
	private String[] args;
	private String message;
	private String retcode;

	public String getRetcode() {
		String currentRetcode;
		if (this.retcode == null) {// retcode没有设置，调用外部配置文件进行转义
			String retcodeMapping = ReloadableAppConfig.appConfig.get(code
					.toString());
			if (retcodeMapping != null && retcodeMapping.length() > 0) {
				String retcodeValue[] = retcodeMapping.split(",");
				currentRetcode=retcodeValue[0];
			} else {
				currentRetcode= "19001";
			}

		} else
		{
			currentRetcode= retcode;
		}
		if(currentRetcode.length()==5)
		{
			String retCodePrefix=ReloadableAppConfig.appConfig.get("retCodePrefix");
			if(retCodePrefix!=null&&retCodePrefix.length()==3)
				currentRetcode=retCodePrefix+currentRetcode;
		}
		return currentRetcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String[] getArgs() {
		return args;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Object getCode() {
		return code;
	}

	public void setCode(Object code) {
		this.code = code;
	}

	public String getMessage() {
		if (this.message == null) {// message没有设置，使用code值
			String retcodeMapping = ReloadableAppConfig.appConfig.get(code
					.toString());
			if (retcodeMapping != null && retcodeMapping.length() > 0) {
				String retcodeValue[] = retcodeMapping.split(",");
				if (retcodeValue.length > 1)
					return retcodeValue[1];
			}
			return code.toString();
		} else
			return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
