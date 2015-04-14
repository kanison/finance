package com.zhaocb.app.website.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.utils.CommonUtil;
import com.app.utils.IPUtil;
import com.app.utils.MD5Util;
import com.zhaocb.app.website.web.exception.ParameterInvalidException;
import com.zhaocb.app.website.web.exception.WebServiceRetException;
import com.zhaocb.app.website.web.model.CommonOutput;
import com.zhaocb.zcb_app.finance.service.facade.FundFacade;
import com.zhaocb.zcb_app.finance.service.facade.dataobject.UserInfoDO;

@RequestMapping
public class RegUser {

	private FundFacade fundFacade;
	private static final Log LOG = LogFactory.getLog(RegUser.class);

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public CommonOutput handleRegUser(UserInfoDO userInfoDO)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {

		LOG.info("handleRegUser user name = " + userInfoDO.getUserName());

		// 检查参数
		checkParam(userInfoDO);

		// 检查用户
		checkUser(userInfoDO.getUserName());

		// 完善用户信息
		setUserInfoDO(userInfoDO);

		// 保存用户
		saveUserInfo(userInfoDO);

		// 返回结果
		return null;
	}

	private void saveUserInfo(UserInfoDO userInfoDO) {
		long result = fundFacade.insertUserInfo(userInfoDO);
		if (result < 1) {
			throw new WebServiceRetException(
					WebServiceRetException.SYSTEM_ERROR, "保存用户失败");
		}
	}

	private void checkUser(String userName) {
		UserInfoDO userInfo = fundFacade.queryUserByUserName(userName);
		if (null != userInfo) {
			throw new WebServiceRetException(
					WebServiceRetException.USER_REG_ERROR, "该用户已存在");
		}
	}

	private void setUserInfoDO(UserInfoDO userInfoDO)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String ip = IPUtil.getClientIP(CommonUtil.getHttpServletRequest());
		userInfoDO.setCreType(1);
		userInfoDO.setRegIp(ip);
		userInfoDO.setUserSeed(CommonUtil.getUUID());
		userInfoDO.setLoginIp(ip);
		userInfoDO.setUserPwd(getHash(userInfoDO.getUserPwd()));
		userInfoDO.setSign(getSign(userInfoDO));
	}

	private String getSign(UserInfoDO userInfoDO) {
		// TODO Auto-generated method stub
		return "test";
	}

	private String getHash(String pwd) throws UnsupportedEncodingException,
			NoSuchAlgorithmException {
		return MD5Util.getMD5(pwd, "UTF-8");
	}

	private void checkParam(UserInfoDO userInfoDO) {
		if (null == CommonUtil.trimString(userInfoDO.getUserName())) {
			throw new ParameterInvalidException("用户名不能为空");
		} else if (null == CommonUtil.trimString(userInfoDO.getTrueName())) {
			throw new ParameterInvalidException("用户真实姓名不能为空");
		} else if (0 == userInfoDO.getSex()) {
			throw new ParameterInvalidException("用户性别不能为空");
		} else if (null == CommonUtil.trimString(userInfoDO.getUserPwd())) {
			throw new ParameterInvalidException("用户密码不能为空");
		} else if (null == CommonUtil.trimString(userInfoDO.getEmail())) {
			throw new ParameterInvalidException("用户邮箱不能为空");
		} else if (null == CommonUtil.trimString(userInfoDO.getMobile())) {
			throw new ParameterInvalidException("用户电话号码不能为空");
		}

	}

	public FundFacade getFundFacade() {
		return fundFacade;
	}

	public void setFundFacade(FundFacade fundFacade) {
		this.fundFacade = fundFacade;
	}

}
