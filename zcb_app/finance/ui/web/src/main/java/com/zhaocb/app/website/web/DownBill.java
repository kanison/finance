package com.zhaocb.app.website.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.app.aop.annotation.AuthMchWithoutCertOrApiWithoutCert;
import com.app.aop.annotation.LogMethod;
import com.app.utils.CommonUtil;
import com.app.utils.URLEncoder;
import com.tenpay.sm.context.ContextUtil;
import com.tenpay.sm.web.context.WebModuleContext;
import com.zhaocb.app.website.web.exception.FundMchapiWebRetException;
import com.zhaocb.app.website.web.exception.ParameterInvalidException;
import com.zhaocb.app.website.web.model.CommonOutput;
import com.zhaocb.app.website.web.model.DownBillInput;
import com.zhaocb.app.website.web.util.FundWebCommon;

/**
 * 文件下载
 * 
 * @author zhl
 */
@RequestMapping
public class DownBill {
	public static final String FILE_NOT_EXIST = "03020003";// 文件不存在
	public static final int BUFFER = 8192;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@LogMethod	
	@AuthMchWithoutCertOrApiWithoutCert
	public CommonOutput handleDownBill(DownBillInput downBillInput)
			throws IOException {

		// 参数检查
		checkParams(downBillInput);

		// 读取路径配置
		String downFilePath = FundWebCommon.getWebConfig("downFilePath");
		
		if (downFilePath == null){
			downFilePath = "up_down_files";
		}
			
		String fileNamePre = downBillInput.getFile_pre();
		if (fileNamePre.length() > 64){
			fileNamePre = fileNamePre.substring(0, 64);
		}
			
		// 替换非法字符
		fileNamePre = CommonUtil.trimFileName(fileNamePre);

		String filePath = String.format("%s/%s/%s/", downFilePath,
				downBillInput.getPartner(), downBillInput.getDate());
		String zipFileName = String.format("%s_%s_%s.zip", fileNamePre,
				downBillInput.getPartner(), downBillInput.getDate());
		String txtFileName = String.format("%s_%s_%s.txt", fileNamePre,
				downBillInput.getPartner(), downBillInput.getDate());
		
		File file = new File(filePath, zipFileName);
		if (!file.exists()) {
			File srcFile = new File(filePath, txtFileName);
			if (!srcFile.exists()) {
				CommonUtil.getHttpServletResponse().setStatus(206);
				throw new FundMchapiWebRetException(FILE_NOT_EXIST, "文件不存在");
			} else {
				// 将.txt 文件压缩成 .zip
				try {
					transferFile(filePath, txtFileName, zipFileName);
				} catch (Exception e) {
					throw new FundMchapiWebRetException(FundMchapiWebRetException.SYSTEM_ERROR,"文件IO错误，请稍后再试");	
				}
			}
		}
		
		// 读取文件并输出
		FileInputStream fis = new FileInputStream(file);
		WebModuleContext ctx = (WebModuleContext) ContextUtil.getContext();
		HttpServletResponse response = ctx.getResponse();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(zipFileName, "UTF-8"));
		response.setContentLength((int) file.length());
		OutputStream os = response.getOutputStream();
		while (fis.available() > 0) {
			os.write(fis.read());
		}
		os.flush();
		fis.close();
		return new CommonOutput();
	}

	/**
	 * 
	 * @param srcFileName
	 *            .txt
	 * @param dstFileName
	 *            .zip
	 * @throws IOException
	 */
	private void transferFile(String filePath, String srcFileName,
			String dstFileName) throws IOException {
		BufferedInputStream bis = null;
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(new File(filePath,
					dstFileName)));
			out.putNextEntry(new ZipEntry(srcFileName));
			bis = new BufferedInputStream(new FileInputStream(new File(
					filePath, srcFileName)));
			int count = 0;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
			out.close();
		} finally {
			if (bis != null)
				bis.close();
			if (out != null)
				out.close();
		}
	}

	private void checkParams(DownBillInput downBillInput) {
		if (downBillInput.getDate() == null
				|| downBillInput.getDate().length() != 8) {
			throw new ParameterInvalidException("日期格式不正确");
		} else if (downBillInput.getFile_pre() == null
				|| downBillInput.getFile_pre().length() == 0) {
			throw new ParameterInvalidException("文件前缀不能为空");
		} else if (downBillInput.getPartner() == null
				|| downBillInput.getPartner().length() == 0) {
			throw new ParameterInvalidException("商户号不能为空");
		}
	}

}
