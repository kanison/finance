package com.zhaocb.zcb_app.finance.fep.finance;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.app.common.exception.CommonException;
import com.app.common.exception.ParameterInvalidException;
import com.app.utils.Base64Util;
import com.app.utils.CommonUtil;
import com.app.utils.MD5Util;
import com.app.utils.XmlParseUtil;
import com.zhaocb.zcb_app.finance.fep.dao.FepDAO;
import com.zhaocb.zcb_app.finance.fep.facade.CftFacade;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BankInfo;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawUsersDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.CftCommOutput;

/**
 * 
 * @author zhl
 *
 */
public class CftFacadeImpl implements CftFacade {

	private static final String CHAR_SET = "GBK";
	private static final String BATCH_DRAW_OP_CODE = "1013";
	private static final String BATCH_DRAW_OP_NAME = "batch_draw";
	private FepDAO fepDAO;

	public CftCommOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception {
		// ����������
		checkParam(batchDrawDO);

		// ��ȡ�̻�key
		String merchantKey = CommonUtil.getWebConfig("merchantKey");
		if (null == merchantKey) {
			throw new CommonException(CommonException.SYSTEM_ERROR, "�̻�keyδ����");
		}

		// ��ȡ�Ƹ�ͨurl
		String cftUrl = CommonUtil.getWebConfig("cftUrl");
		if (null == cftUrl) {
			throw new CommonException(CommonException.SYSTEM_ERROR, "�Ƹ�ͨurlδ����");
		}

		// ��������������xml
		String reqXml = setRequestXml(batchDrawDO);

		// ��xml����base64�������ɲ���content
		String content = Base64Util.encodeBase64(reqXml.getBytes(CHAR_SET));

		// �������ɲ���abstract
		String abstct = MD5Util.getMD5(
				MD5Util.getMD5(content, CHAR_SET).toLowerCase() + merchantKey,
				CHAR_SET).toLowerCase();

		// ���Ӳ���params
		StringBuffer params = new StringBuffer("content=").append(content)
				.append("&abstract=").append(abstct);

		// �ύ��������
		// String respXml = submitBatchDraw(cftUrl, params.toString());

		// ���ؽ������
		return parseResponseXml("");
	}

	/**
	 * ������
	 * 
	 * @param draw
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	private void checkParam(BatchDrawDO draw) throws IllegalArgumentException,
			IllegalAccessException {
		int totalNum = draw.getTotal_num();
		if (totalNum != draw.getUsers_set().size()) {
			throw new ParameterInvalidException("�ܼ�¼����������ϸ��¼����ͬ");
		} else if (totalNum > 20000) {
			throw new ParameterInvalidException("һ������֧�����2��ʼ�¼");
		}

		int totalAmt = 0;
		Field[] drawFields = draw.getClass().getFields();
		Set<BatchDrawUsersDO> userSet;
		Iterator<BatchDrawUsersDO> ite;
		BatchDrawUsersDO drawUser;
		String fieldName;
		for (Field df : drawFields) {
			fieldName = df.getName();
			if (df.getType() == java.util.Set.class) {
				userSet = (HashSet<BatchDrawUsersDO>) df.get(draw);
				ite = userSet.iterator();
				Field[] drawUserFields;
				int userPmt;
				String acctType;// �˻�����
				String recName;// �տ�����
				String bankType;// ��������
				BankInfo bankInfo;// ������Ϣ
				Map areaCityMap;
				while (ite.hasNext()) {
					drawUser = (BatchDrawUsersDO) ite.next();
					drawUserFields = drawUser.getClass().getFields();
					for (Field uf : drawUserFields) {
						fieldName = uf.getName();
						if (fieldName.matches("subbank_name")) {// �˴�����,����������֤֧���Ƿ����
							continue;
						}
						if (null == uf.get(drawUser)
								|| null == CommonUtil.trimString(uf.get(
										drawUser).toString())) {
							throw new ParameterInvalidException(fieldName
									+ "����Ϊ��");
						}
					}

					if (drawUser.getSerial().length() > 32) {
						throw new ParameterInvalidException(
								"ͬһ�������ڵ���ϸ��Ų��ܳ���32���ַ�");
					}

					// ��֤�ʻ����ͺ��տ����
					acctType = drawUser.getAcc_type();
					if (!acctType.matches("1|2")) {
						throw new ParameterInvalidException("�˻�����ֻ��Ϊ1��2");
					} else {
						recName = drawUser.getRec_name();
						if ("1".equals(acctType)
								&& recName.getBytes().length < 4) {
							throw new ParameterInvalidException(
									"�տ������������ڵ���4���ֽ�");
						}
						if ("2".equals(acctType)
								&& recName.getBytes().length < 9) {
							throw new ParameterInvalidException(
									"�տ��˾��������ڵ���4���ֽ�");
						}
					}

					// ��֤���С�������������Ϣ
					bankType = drawUser.getBank_type();
					bankInfo = fepDAO.queryBankInfoByCode(bankType);
					if (null == bankInfo) {
						throw new ParameterInvalidException("�������Ͳ��Ϸ�");
					} else {
						// ����ʡ��,������в���֤������дΪ0
						if ("Y".equals(bankInfo.getAreaRequestPub())) {
							areaCityMap = new HashMap<String, String>();
							areaCityMap.put("areaCode", drawUser.getArea());
							areaCityMap.put("cityCode", drawUser.getCity());
							if (fepDAO.queryAreaCityByCode(areaCityMap) < 1) {
								throw new ParameterInvalidException("ʡ�б��벻�Ϸ�");
							}
						} else {
							drawUser.setArea("0");
							drawUser.setCity("0");
						}

						// ֧������,������в���֤������дΪȫ�ǿո�
						if ("Y".equals(bankInfo.getSubBankRequestPub())) {
							if (!drawUser.getSubbank_name().matches(".*����.*")) {
								throw new ParameterInvalidException(
										"֧������ǰ����������������������");
							}
						} else {
							drawUser.setSubbank_name("��");
						}
					}

					// ��֤������
					userPmt = drawUser.getPay_amt();
					if (userPmt <= 0) {
						throw new ParameterInvalidException("�������Ϊ0");
					}
					totalAmt += userPmt;
				}
			} else {
				if (null == df.get(draw)
						|| null == CommonUtil.trimString(df.get(draw)
								.toString())) {
					throw new ParameterInvalidException(fieldName + "����Ϊ��");
				}
			}
		}

		if (totalAmt != draw.getTotal_amt()) {
			throw new ParameterInvalidException("�ܽ����������ϸ��¼���֮��");
		}

		if (draw.getPackage_id().length() > 30) {
			throw new ParameterInvalidException("���κų�������Ϊ30���ַ�");
		}
	}

	/**
	 * ��������xml
	 * 
	 * @param draw
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String setRequestXml(BatchDrawDO draw)
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

		xmlStr.insert(
				0,
				new StringBuffer()
						.append("<?xml version=\"1.0\" encoding=\"GB2312\" ?>\n<root>\n")
						.append("<op_code>").append(BATCH_DRAW_OP_CODE)
						.append("</op_code>\n").append("<op_name>")
						.append(BATCH_DRAW_OP_NAME).append("</op_name>\n"))
				.append("\n</root>");

		System.out.println(xmlStr);

		return xmlStr.toString();
	}

	/**
	 * �ύ����
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	private String submitBatchDraw(String url, String content)
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

		// ˢ�¡��ر�
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
			// return outStream.toByteArray();
			return outStream.toByteArray().toString();
		}

		return "";
	}

	private CftCommOutput parseResponseXml(String xml) {
		xml = "<?xml version=\"1.0\" encoding=\"GB2312\" ?>"
				+ "<root>"
				+ "<charset>GB2312</charset>"
				+ "<op_code>1013</op_code>"
				+ "<op_name>batch_draw</op_name>"
				+ "<op_user>�ύ��ID</op_user>"
				+ "<op_time>����ʱ�䣨yyyyMMddHHmmssSSS��</op_time>"
				+ "<package_id>������ID��YYYYMMDDXXX��</package_id>"
				+ "<result>(���ӿ�result��Ϊ��)</result>"
				+ "<retcode>�����룺0��00-�ύ�ɹ���������5.11��˵�������ڷ��ط�0��00�Ĵ����룬�̻�������ò�ѯ�ӿ�ȷ������״̬��</retcode>"
				+ "<retmsg>������������</retmsg>" + "</root>";

		Map<String, String> xmlMap = XmlParseUtil.xmlToMap(new BufferedReader(
				new StringReader(xml)));

		CftCommOutput output = new CftCommOutput();
		output.setRetcode(xmlMap.get("retcode"));
		output.setRetmsg(xmlMap.get("retmsg"));
		return output;
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

	public FepDAO getFepDAO() {
		return fepDAO;
	}

	public void setFepDAO(FepDAO fepDAO) {
		this.fepDAO = fepDAO;
	}
}
