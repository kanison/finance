package com.zhaocb.zcb_app.finance.fep.finance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.app.common.exception.ParameterInvalidException;
import com.app.utils.Base64Util;
import com.app.utils.CommonUtil;
import com.app.utils.MD5Util;
import com.zhaocb.zcb_app.finance.fep.facade.CftFacade;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawUsersDO;

/**
 * 
 * @author zhl
 *
 */
public class CftFacadeImpl implements CftFacade {

	private final String CHAR_SET = "GBK";

	public String batchDraw(BatchDrawDO batchDrawDO) throws Exception {
		// 检查参数
		checkParam(batchDrawDO);

		// 设置请求xml参数
		String reqXml = setRequestXml(batchDrawDO);
		String content = Base64Util.encodeBase64(reqXml.getBytes(CHAR_SET));
		String key = "1122234123";// 商户key
		String abstct = MD5Util
				.getMD5(MD5Util.getMD5(content, CHAR_SET).toLowerCase() + key,
						CHAR_SET).toLowerCase();
		StringBuffer params = new StringBuffer("content=").append(content)
				.append("&abstract=").append(abstct);

		// 获取财付通请求URL请求付款
		String cftUrl = CommonUtil.getWebConfig("cftUrl");
		// submitBatchDraw(cftUrl, params.toString());

		return reqXml;
	}

	public static void main(String[] args) throws Exception {
		BatchDrawDO draw = new BatchDrawDO();
		Set<BatchDrawUsersDO> users_set = new HashSet<BatchDrawUsersDO>();
		BatchDrawUsersDO user1 = new BatchDrawUsersDO();
		BatchDrawUsersDO user2 = new BatchDrawUsersDO();		

		user1.setSerial("US000001");
		user1.setAcc_type("1");
		user1.setRec_name("中国银行");
		user1.setPay_amt(30);
		user1.setRec_bankacc("A001");
		user1.setBank_type("1");
		user1.setArea("CN");
		user1.setCity("SZ");
		user1.setSubbank_name("高新圆分行");
		user1.setDesc("汇款");
		user1.setRecv_mobile("13700010002");

		user2.setAcc_type("2");
		user2.setRec_name("建设银行");
		
		draw.setTotal_num(1);
		draw.setTotal_amt(30);
		draw.setOp_user("test");
		draw.setOp_passwd("test");
		draw.setOp_time(new Date());
		draw.setSp_id("zcb");
		draw.setPackage_id("S00001");
		draw.setClient_ip("127.0.0.1");
		
		draw.setUsers_set(users_set);
		users_set.add(user1);
		//users_set.add(user2);

		CftFacadeImpl test = new CftFacadeImpl();
		test.batchDraw(draw);
	}

	/**
	 * 检查参数
	 * 
	 * @param draw
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public void checkParam(BatchDrawDO draw) throws IllegalArgumentException,
			IllegalAccessException {
		int totalNum = draw.getTotal_num();
		if (totalNum != draw.getUsers_set().size()) {
			throw new ParameterInvalidException("总记录数必须与明细记录数相同");
		} else if (totalNum > 20000) {
			throw new ParameterInvalidException("一个批次支持最多2万笔记录");
		}

		int totalAmt = 0;
		Field[] drawFields = draw.getClass().getFields();
		Set<BatchDrawUsersDO> userSet;
		Iterator<BatchDrawUsersDO> ite;
		BatchDrawUsersDO drawUser;
		for (Field df : drawFields) {
			if (df.getType() == java.util.Set.class) {
				userSet = (HashSet<BatchDrawUsersDO>) df.get(draw);
				ite = userSet.iterator();
				Field[] drawUserFields;
				int userPmt;
				while (ite.hasNext()) {
					drawUser = (BatchDrawUsersDO) ite.next();
					drawUserFields = drawUser.getClass().getFields();
					for (Field uf : drawUserFields) {
						if (null == uf.get(drawUser)) {
							throw new ParameterInvalidException(uf.getName()
									+ "不能为空");
						}
					}
					userPmt = drawUser.getPay_amt();
					if (userPmt <= 0) {
						throw new ParameterInvalidException("付款金额不能为0");
					}
					totalAmt += userPmt;
				}
			} else {
				if (null == df.get(draw)
						|| null == CommonUtil.trimString(df.get(draw)
								.toString())) {
					throw new ParameterInvalidException(df.getName() + "不能为空");
				}
			}
		}

		if (totalAmt != draw.getTotal_amt()) {
			throw new ParameterInvalidException("总金额必须等于明细记录金额之和");
		}
	}

	/**
	 * 设置请求xml
	 * 
	 * @param draw
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String setRequestXml(BatchDrawDO draw)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] drawFields = draw.getClass().getFields();
		Set<BatchDrawUsersDO> userSet;
		Iterator<BatchDrawUsersDO> ite;
		BatchDrawUsersDO drawUser;
		StringBuffer xmlStr = new StringBuffer();
		StringBuffer eleStr = new StringBuffer();

		for (Field df : drawFields) {
			eleStr.setLength(0);
			if (df.getType() == java.util.Set.class) {
				userSet = (HashSet<BatchDrawUsersDO>) df.get(draw);
				ite = userSet.iterator();
				xmlStr.append("<record_set>\n");
				Field[] drawUserFields;
				while (ite.hasNext()) {
					drawUser = (BatchDrawUsersDO) ite.next();
					drawUserFields = drawUser.getClass().getFields();
					xmlStr.append("<record>\n");

					for (Field uf : drawUserFields) {
						eleStr.setLength(0);
						eleStr.append("<").append(uf.getName()).append(">");
						xmlStr.append("\t").append(eleStr)
								.append(uf.get(drawUser))
								.append(eleStr.insert(1, "/")).append("\n");
					}
					xmlStr.append("</record>\n");
				}
				xmlStr.append("</record_set>");
			} else {
				eleStr.append("<").append(df.getName()).append(">");
				xmlStr.append(eleStr)
						.append(df.getName() == "op_time" ? CommonUtil
								.formatDate((Date) df.get(draw),
										"yyyyMMddHHmmssSSS") : df.get(draw))
						.append(eleStr.insert(1, "/")).append("\n");
			}
		}

		xmlStr.insert(0,
				"<?xml version=\"1.0\" encoding=\"GB2312\" ?>\n<root>\n")
				.append("\n</root>");

		System.out.println(xmlStr);

		return xmlStr.toString();
	}

	/**
	 * 提交付款
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public byte[] submitBatchDraw(String url, String content)
			throws NoSuchAlgorithmException, KeyManagementException,
			IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
				new java.security.SecureRandom());

		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes("GBK"));

		// 刷新、关闭
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			return outStream.toByteArray();
		}
		return null;
	}

	public String batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO) {
		// TODO Auto-generated method stub
		return null;
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}
