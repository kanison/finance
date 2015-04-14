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

		// ������
		checkParam(userInfoDO);

		// ����û�
		checkUser(userInfoDO.getUserName());

		// �����û���Ϣ
		setUserInfoDO(userInfoDO);

		// �����û�
		saveUserInfo(userInfoDO);

		// ���ؽ��
		return null;
	}

	private void saveUserInfo(UserInfoDO userInfoDO) {
		long result = fundFacade.insertUserInfo(userInfoDO);
		if (result < 1) {
			throw new WebServiceRetException(
					WebServiceRetException.SYSTEM_ERROR, "�����û�ʧ��");
		}
	}

	private void checkUser(String userName) {
		UserInfoDO userInfo = fundFacade.queryUserByUserName(userName);
		if (null != userInfo) {
			throw new WebServiceRetException(
					WebServiceRetException.USER_REG_ERROR, "���û��Ѵ���");
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
			throw new ParameterInvalidException("�û�������Ϊ��");
		} else if (null == CommonUtil.trimString(userInfoDO.getTrueName())) {
			throw new ParameterInvalidException("�û���ʵ��������Ϊ��");
		} else if (0 == userInfoDO.getSex()) {
			throw new ParameterInvalidException("�û��Ա���Ϊ��");
		} else if (null == CommonUtil.trimString(userInfoDO.getUserPwd())) {
			throw new ParameterInvalidException("�û����벻��Ϊ��");
		} else if (null == CommonUtil.trimString(userInfoDO.getEmail())) {
			throw new ParameterInvalidException("�û����䲻��Ϊ��");
		} else if (null == CommonUtil.trimString(userInfoDO.getMobile())) {
			throw new ParameterInvalidException("�û��绰���벻��Ϊ��");
		}

	}

	public FundFacade getFundFacade() {
		return fundFacade;
	}

	public void setFundFacade(FundFacade fundFacade) {
		this.fundFacade = fundFacade;
	}

}
