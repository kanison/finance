package com.zcb_app.sms.service.dao;

import com.zcb_app.sms.service.dao.type.MsgSendCodeParams;
import com.zcb_app.sms.service.dao.type.MsgSendMessageParams;
import com.zcb_app.sms.service.facade.dataobject.IPLimitDO;
import com.zcb_app.sms.service.facade.dataobject.MobileLimitDO;
import com.zcb_app.sms.service.facade.dataobject.SendInfoDO;
import com.zcb_app.sms.service.facade.dataobject.VerifyCodeInfoDO;

/**
 * @author Gu.Dongying
 */
public interface SMSServiceDAO {
	
	/**
	 * 处理业务<br>
	 * 1、保存下发的验证码<br>
	 * 2、将历史的验证码信息记录流水表中<br>
	 * @param verifyCode
	 * @author Gu.Dongying 
	 * @Date 2015年5月4日 下午2:52:30
	 */
	public void saveVerifyCodeInfo(MsgSendCodeParams verifyCode);
	
	/**
	 * 查询手机号频率受限记录
	 * @param mobileLimit 手机号、模板ID
	 * @return MobileLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午9:43:14
	 */
	public MobileLimitDO queryMobileLimit(MobileLimitDO mobileLimit);
	
	/**
	 * 查询IP频率受限记录
	 * @param ipLimit IP、模板ID
	 * @return IPLimitDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午9:43:14
	 */
	public IPLimitDO queryIPLimit(IPLimitDO ipLimit);

	/**
	 * 保存短信信息
	 * @param params
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午10:55:03
	 */
	public void saveMsgInfo(MsgSendMessageParams params);
	
	/**
	 * 查询当前手机号和模板ID对应的最新验码信息(取最后一条新增的记录)
	 * @param codeInfo 当前手机号和模板ID
	 * @return VerifyCodeInfoDO
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 上午11:45:52
	 */
	public VerifyCodeInfoDO queryMsgInfo(VerifyCodeInfoDO codeInfo);
	
	/**
	 * 更新验证相应的结果信息
	 * @param codeInfo 当前手机号和模板ID
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 下午12:57:41
	 */
	public void updateVerifyCodeInfo(VerifyCodeInfoDO codeInfo);
	
	/**
	 * 新增发送短信信息记录
	 * @param sendInfo
	 * @author Gu.Dongying 
	 * @Date 2015年5月5日 下午5:40:51
	 */
	public void addSendInfo(SendInfoDO sendInfo);
	
	/**
	 * 添加手机号频率限制记录
	 * @param mobileLimit 手机号频率限制信息
	 * @author Gu.Dongying 
	 * @Date 2015年5月6日 上午9:25:01
	 */
	public void addMobileLimitInfo(MobileLimitDO mobileLimit);
	
	/**
	 * 添加IP地址频率限制信息记录
	 * @param ipLimit IP地址频率限制信息
	 * @author Gu.Dongying 
	 * @Date 2015年5月6日 上午9:26:08
	 */
	public void addIPLimitInfo(IPLimitDO ipLimit);
	
	/**
	 * 1、排除是否手机号和IP在不受频率限制的白名单内<br>
	 * 2、按手机号和IP检查是否超过频率限制，并更新频率信息
	 * 
	 * @param sParams
	 *            code:手机号 clientIP:手机号对应的客户端IP
	 * @author Gu.Dongying
	 * @Date 2015年4月30日 下午3:08:07
	 */
	public void ctrlSendCodeLimit(MsgSendCodeParams sParams);
	
}
