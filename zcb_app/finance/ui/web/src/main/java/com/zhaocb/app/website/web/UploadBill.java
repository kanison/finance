package com.zhaocb.app.website.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.aop.annotation.LogMethod;
import com.app.common.exception.ParameterInvalidException;
import com.app.utils.CommonUtil;
import com.zhaocb.app.website.web.exception.FundMchapiWebRetException;
import com.zhaocb.app.website.web.model.CommonOutput;
import com.zhaocb.app.website.web.model.UploadBillInput;
import com.zhaocb.app.website.web.util.FundWebCommon;

/**
 * �ϴ��ļ�
 * 
 * @author zhl
 */
@RequestMapping
public class UploadBill {
	private static final Log LOG = LogFactory.getLog(UploadBill.class);

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@LogMethod
	public CommonOutput  handleUploadBill(UploadBillInput uploadBillInput)
			throws IOException, NoSuchAlgorithmException {
		
		// �������
		checkParams(uploadBillInput);

		// ��������ȡ�ļ�·��
		String uploadFilePath = CommonUtil.getWebConfig("uploadFilePath");

		if (uploadFilePath == null){
			uploadFilePath = "up_down_files";
		}			
		
		// ����ļ������ȣ��滻�Ƿ��ַ�
		String fileNamePre = uploadBillInput.getFile_pre();
		if (fileNamePre.length() > 64) {
			fileNamePre = fileNamePre.substring(0, 64);
		}		
		fileNamePre = CommonUtil.trimFileName(fileNamePre);
		
		//����ļ�����
		String fileSubfix;
		switch (uploadBillInput.getFile_type()) {
		case 0:
			fileSubfix = "txt";
			break;
		case 1:
			fileSubfix = "zip";
			break;
		default:
			throw new ParameterInvalidException("�ϴ����ļ�������ʱֻ֧��txt zip");
		}		
		
		//�����ļ�����·��
		File file = new File(String.format("%s/%s/%s/%s_%s_%s.%s",
				uploadFilePath, uploadBillInput.getPartner(),
				uploadBillInput.getDate(), fileNamePre,
				uploadBillInput.getPartner(), uploadBillInput.getDate(),
				fileSubfix));		
		if (!canUploadBillFile(fileNamePre, file)) {
			throw new FundMchapiWebRetException(FundMchapiWebRetException.CAN_NOT_UPLOAD_FILE, "�ϴ�����������ѳ����ļ����ϴ�ʱ��");
		}
		
		//��鲢�����ļ�Ŀ¼
		File parent = file.getParentFile();
		if (!parent.exists()){
			parent.mkdirs();
		}			

		// �����ļ�
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(uploadBillInput.getFile_body().getBytes());
		fos.close();

		return new CommonOutput();

	}

	private void checkParams(UploadBillInput uploadBillInput)
			throws NoSuchAlgorithmException, IOException {
		if (uploadBillInput.getDate() == null
				|| uploadBillInput.getDate().length() != 8) {
			throw new ParameterInvalidException("���ڸ�ʽ����ȷ");
		} else if (uploadBillInput.getFile_body() == null
				|| uploadBillInput.getFile_body().isEmpty()) {
			throw new ParameterInvalidException("�ļ��岻��Ϊ��");
		} else if (uploadBillInput.getFile_pre() == null
				|| uploadBillInput.getFile_pre().length() == 0) {
			throw new ParameterInvalidException("�ļ�ǰ׺����Ϊ��");
		} else if (uploadBillInput.getPartner() == null
				|| uploadBillInput.getPartner().length() == 0) {
			throw new ParameterInvalidException("�̻��Ų���Ϊ��");
		} else if (!checkMD5(uploadBillInput.getFile_body().getBytes(),
				uploadBillInput.getFile_md5())) {
			throw new ParameterInvalidException("�ļ�MD5У��ʧ��");
		}
		if(uploadBillInput.getFile_type()!=0 && uploadBillInput.getFile_type()!=1){
			throw new ParameterInvalidException("�ϴ����ļ�������ʱֻ֧��txt zip");
		}
	}

	private boolean checkMD5(byte[] strTemp, String md5Str)
			throws NoSuchAlgorithmException {
		if (strTemp == null || strTemp.length == 0 || md5Str == null
				|| md5Str.length() == 0) {
			return false;
		}

		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		BigInteger value1 = new BigInteger(1, md);
		BigInteger value2 = new BigInteger(md5Str, 16);
		return value1.equals(value2);
	}

	private boolean canUploadBillFile(String fileNamePre, File file) {
		if (!file.exists()) {
			return true;
		}

		String canReloadEndTime = FundWebCommon.getAppConfig("endtime_"
				+ fileNamePre);
		if (canReloadEndTime == null) {
			return false;
		}

		try {
			Date now = new Date();
			String nowDate = CommonUtil.formatDate(now, "yyyy-MM-dd");
			if (now.before(CommonUtil.parseDate(nowDate + " "
					+ canReloadEndTime, "yyyy-MM-dd HH:mm:ss"))) {
				synchronized (this) {
					int round = (int) (Math.random() * 100000);
					File newFile = new File(file.getParent(), file.getName()
							+ round);
					if (!file.renameTo(newFile)) {
						round = (int) (Math.random() * 100000);
						newFile = new File(file.getParent(), file.getName()
								+ round);
						if (!file.renameTo(newFile)) {
							return false;
						}
					}
				}
				return true;
			}

		} catch (ParseException e) {
			LOG.warn("parse reload file exception");
		}
		return false;
	}
}
