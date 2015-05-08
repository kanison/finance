package com.zcb_app.sms.service.facade;

import com.zcb_app.sms.service.facade.dataobject.SendCodeParams;
import com.zcb_app.sms.service.facade.dataobject.SendMessageParams;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeParams;

public interface SMSServiceFacade {
	
	/**
	 * 下发短信验证码<br>
	 * 1、	检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 2、	按手机号和IP检查是否超过频率限制（可以考虑使用memcached、mysql内存表、单机缓存等方式来保存频率）<br>
	 * 3、	生成验证码<br>
	 * 4、	保存验证码，将历史的验证码信息记录流水表中<br>
	 * 5、	根据模板和输入参数，生成短信内容<br>
	 * 6、	调短信网关下发短信<br>
	 * @param params 传入参数
	 * @Return void
	 * @author Gu.Dongying 
	 * @Date 2015年4月30日 下午2:17:12
	 */
	public void sendMCode(SendCodeParams params);
	
	/**
	 * 验证短信验证码<br>
	 * 1、	检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 2、	查询当前手机号和模板ID对应的最新验码信息<br>
	 * 3、	校验是否超过有效期；校验是否超过验证次数<br>
	 * 4、	校验输入的验证码与下发的验证码是否相同，并更新验证相应的结果信息<br>
	 * @param params 传入参数
	 * @return 返回验证结果和关联信息
	 * @Return String
	 * @author Gu.Dongying 
	 * @Date 2015年4月30日 下午2:19:19
	 */
	public String verifyMCode(VerifyCodeParams params);
	
	/**
	 * 7.1.3 发送通知短信<br>
	 * 1、	检查op_code是否正确<br>
	 * 2、	检查输入参数，手机号格式是否正确，模板是否配置<br>
	 * 3、	验证IP是否为内部授权IP（下发通知类的消息只有内部白名单的机器可以调用），不再进行频率限制控制。<br>
	 * 4、	保存短信消息（将tmpl_value的值保存到Frela_info字段中），将历史的短信发送信息记录流水表中<br>
	 * 5、	根据模板和输入参数，生成短信内容<br>
	 * 6、	调短信网关下发短信
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 上午9:33:53
	 */
	public void sendSM(SendMessageParams params);
	
}
