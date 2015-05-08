package com.zcb_app.sms.service.utils;

/**
 * 短信验证码工具类
 * @author Gu.Dongying
 */
public class VerifyCodeUtils {
	
	/**
	 * 生成验证码
	 * @return String 验证码
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午1:42:45
	 */
	public static String generateVerifyCode(int verifyCodeLen){
		StringBuilder sb = new StringBuilder();
		long rand;
		for(int i = 0; i < verifyCodeLen; i++){			
			rand = Math.round(Math.random()*61);
			if(rand < 10){
				sb.append(rand);
			}else if(rand < 36){
				sb.append((char)(rand + 55));
			}else{
				sb.append((char)(rand + 61));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 生成验证码
	 * @return String 验证码
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午2:09:04
	 */
	public static String generateVerifyCode(){
		return generateVerifyCode(4);
	}
	
}
