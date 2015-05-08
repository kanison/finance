package com.zcb_app.sms.service.utils;

/**
 * ������֤�빤����
 * @author Gu.Dongying
 */
public class VerifyCodeUtils {
	
	/**
	 * ������֤��
	 * @return String ��֤��
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����1:42:45
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
	 * ������֤��
	 * @return String ��֤��
	 * @author Gu.Dongying 
	 * @Date 2015��5��4�� ����2:09:04
	 */
	public static String generateVerifyCode(){
		return generateVerifyCode(4);
	}
	
}
