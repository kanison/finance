package com.zhaocb.zcb_app.finance.fep.finance;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import com.app.common.exception.ParameterInvalidException;
import com.app.utils.Base64Util;
import com.app.utils.CommonUtil;
import com.app.utils.MD5Util;
import com.app.utils.URLEncoder;
import com.app.utils.XmlParseUtil;
import com.zhaocb.zcb_app.finance.fep.dao.FepDAO;
import com.zhaocb.zcb_app.finance.fep.exception.FepServiceRetException;
import com.zhaocb.zcb_app.finance.fep.facade.CftFacade;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BankInfo;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawQueryUsersDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.BatchDrawUsersDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryDO;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryOutput;
import com.zhaocb.zcb_app.finance.fep.facade.dataobject.PayRefundQueryUsersDO;
import com.zhaocb.zcb_app.finance.fep.utils.ConnectionUtil;
import com.zhaocb.zcb_app.finance.fep.utils.SSLUtil;
import com.zhaocb.zcb_app.finance.fep.utils.XMLUtil;

/**
 * �Ƹ�ͨʵ����
 * 
 * @author zhl
 *
 */
public class CftFacadeImpl implements CftFacade {

	private static final Log LOG = LogFactory.getLog(CftFacadeImpl.class);

	private static final String CHAR_SET = "GBK";

	// �Ƹ�ͨ�����ѯ���������������Ϣ
	private static final String BATCH_DRAW_OP_CODE = "1013";
	private static final String BATCH_DRAW_OP_NAME = "batch_draw";
	private static final String BATCH_DRAW_QUERY_OP_CODE = "1014";
	private static final String BATCH_DRAW_QUERY_OP_NAME = "batch_draw_query";
	private static final String SERVICE_VERSION = "1.2";

	// �����ض���ҵ������
	private static enum BusType {
		BATCH_DRAW, BATCH_DRAW_QUERY, PAY_REFUND_QUERY
	}

	private static String MERCHANT_KEY;// �̻�key
	private static String CFT_URL;// �Ƹ�ͨurl

	// ֤����֤key������
	private static String CA_FILE;
	private static String CERT_FILE;
	private static String CERT_PASSWORD;

	private FepDAO fepDAO;

	/**
	 * �������û����п�����
	 */
	public BatchDrawOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception {
		// ������
		checkParam(batchDrawDO);

		// ��������������xml
		String reqXml = setRequestXml(batchDrawDO);

		// ���ɲ���params
		String params = genParams(reqXml);

		// �ύ��������
		String respXml = connect(CFT_URL, params);

		// ���ؽ������
		return (BatchDrawOutput) getOutput(BusType.BATCH_DRAW, respXml);
	}

	/**
	 * ��ѯ����������
	 */
	public BatchDrawQueryOutput batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO)
			throws Exception {
		// ������
		checkParam(batchDrawQueryDO);

		// ��������������xml
		String reqXml = setRequestXml(batchDrawQueryDO);

		// ���ɲ���params
		String params = genParams(reqXml);

		// �ύ�����ѯ
		String respXml = connect(CFT_URL, params);

		// ���ؽ������
		return (BatchDrawQueryOutput) getOutput(BusType.BATCH_DRAW_QUERY,
				respXml);
	}

	/**
	 * ��ѯ�̻�������Ʊ���
	 */
	public PayRefundQueryOutput payRefundQuery(PayRefundQueryDO payRefundQueryDO)
			throws Exception {
		// ������
		checkParam(payRefundQueryDO);

		// ���ɲ���params
		String params = genParams(payRefundQueryDO);

		// �ύ��Ʊ��ѯ
		String respXml = connect(CFT_URL, params);

		// ���ؽ������
		return (PayRefundQueryOutput) getOutput(BusType.PAY_REFUND_QUERY,
				respXml);
	}

	/**
	 * ����������Ʊ��ѯ
	 * 
	 * @param payRefundQueryDO
	 */
	private void checkParam(PayRefundQueryDO payRefundQueryDO) {
		// ���Ƹ�ͨ������Ϣ
		checkCftConfig("cft_refund_url");

		// �����ϸҵ���߼�����
		String sign = payRefundQueryDO.getSign();
		String partner = payRefundQueryDO.getPartner();
		Date startTime = payRefundQueryDO.getStart_time();
		Date endTime = payRefundQueryDO.getEnd_time();

		if (null == CommonUtil.trimString(sign)) {
			throw new ParameterInvalidException("ǩ������Ϊ��");
		} else if (sign.length() != 32) {
			throw new ParameterInvalidException("ǩ������Ӧ��Ϊ32λ");
		} else if (null == CommonUtil.trimString(partner)) {
			throw new ParameterInvalidException("�̻��Ų���Ϊ��");
		} else if (!partner.matches("[\\d]{10}")) {
			throw new ParameterInvalidException("�̻��Ÿ�ʽ����ȷ");
		} else if (null == startTime) {
			throw new ParameterInvalidException("��ʼʱ�䲻��Ϊ��");
		} else if (null == endTime) {
			throw new ParameterInvalidException("����ʱ�䲻��Ϊ��");
		}

		String bankType = payRefundQueryDO.getBank_type();
		if (null != CommonUtil.trimString(bankType)) {
			// ��֤���б���
			if (null == fepDAO.queryBankInfoByCode(bankType)) {
				throw new ParameterInvalidException("���б��벻�Ϸ�");
			}
		}
	}

	/**
	 * ����������ѯ�������
	 * 
	 * @param drawQuery
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void checkParam(BatchDrawQueryDO drawQuery)
			throws IllegalArgumentException, IllegalAccessException {
		// ���Ƹ�ͨ������Ϣ
		checkCftConfig("cft_url");

		// �����ϸҵ���߼�����
		Field[] fields = drawQuery.getClass().getFields();
		String fieldName;
		for (Field qf : fields) {
			fieldName = qf.getName();
			if (null == qf.get(drawQuery)
					|| null == CommonUtil.trimString(qf.get(drawQuery)
							.toString())) {
				throw new ParameterInvalidException(fieldName + "����Ϊ��");
			}
		}
	}

	/**
	 * ����������������
	 * 
	 * @param draw
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void checkParam(BatchDrawDO draw) throws IllegalArgumentException,
			IllegalAccessException {
		// ���Ƹ�ͨ������Ϣ
		checkCftConfig("cft_url");

		// �����ϸҵ���߼�����
		int totalNum = draw.getTotal_num();
		if (totalNum != draw.getUsers_set().size()) {
			throw new ParameterInvalidException("�ܼ�¼����������ϸ��¼����ͬ");
		} else if (totalNum > 20000) {
			throw new ParameterInvalidException("һ������֧�����2��ʼ�¼");
		}

		int totalAmt = 0;
		Field[] drawFields = draw.getClass().getFields();
		Set<BatchDrawUsersDO> userSet;
		Set<String> serialSet = new HashSet<String>();
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
				Map<String, String> areaCityMap;
				while (ite.hasNext()) {
					drawUser = ite.next();
					drawUserFields = drawUser.getClass().getFields();
					for (Field uf : drawUserFields) {
						fieldName = uf.getName();
						if (fieldName.matches("subbank_name|desc|recv_mobile")) {// �Ǳ�������
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

					if (!serialSet.add(drawUser.getSerial())) {
						throw new ParameterInvalidException("ͬһ�������ڵ���ϸ���Ҫ��֤Ψһ");
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
							// if
							// (!drawUser.getSubbank_name().matches(".*����.*")) {
							// throw new ParameterInvalidException(
							// "֧������ǰ����������������������");
							// }
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
			throw new ParameterInvalidException("���κų��Ȳ��ܳ���30���ַ�");
		}
	}

	/**
	 * ���Ƹ�ͨ������Ϣ
	 * 
	 * @param url
	 */
	private void checkCftConfig(String url) {
		// ����̻�key
		MERCHANT_KEY = CommonUtil.getWebConfig("mer.key");
		if (null == MERCHANT_KEY) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "�̻�keyδ����");
		}

		// ���Ƹ�ͨurl
		CFT_URL = CommonUtil.getWebConfig(url);
		if (null == CFT_URL) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "�Ƹ�ͨurlδ����");
		}

		// ���Ƹ�֤ͨ����֤����ļ�·��������
		CA_FILE = CommonUtil.getWebConfig("ca.file");
		CERT_FILE = CommonUtil.getWebConfig("mer.cert.file");
		CERT_PASSWORD = CommonUtil.getWebConfig("mer.cert.password");
		if (null == CA_FILE || null == CERT_FILE || null == CERT_PASSWORD) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "�Ƹ�ͨ��֤֤���ļ�·��������δ����");
		}

	}

	/**
	 * ���ɲ���params
	 * 
	 * @param reqXml
	 * @return
	 * @throws Exception
	 */
	private String genParams(String reqXml) throws Exception {
		// ��xml����base64�������ɲ���content
		String content = new String(
				Base64Util.encode(reqXml.getBytes(CHAR_SET)));

		// �������ɲ���abstract
		String abstct = MD5Util.getMD5(
				MD5Util.getMD5(content, CHAR_SET).toLowerCase() + MERCHANT_KEY,
				CHAR_SET).toLowerCase();

		// ���ӳɲ���params
		StringBuffer params = new StringBuffer("content=")
				.append(URLEncoder.encode(content, CHAR_SET))
				.append("&abstract=").append(abstct);

		return params.toString();
	}

	/**
	 * ���ɲ���params-��Ʊ��ѯ
	 * 
	 * @param payRefundQuery
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	private String genParams(PayRefundQueryDO payRefundQuery)
			throws IllegalArgumentException, IllegalAccessException,
			UnsupportedEncodingException, NoSuchAlgorithmException {
		// ����ǩ����������
		//SignUtil.getSign(payRefundQuery, MERCHANT_KEY);

		Field[] fields = payRefundQuery.getClass().getFields();
		StringBuffer sb = new StringBuffer();
		Object obj;
		for (Field pf : fields) {
			obj = pf.get(payRefundQuery);
			sb.append(pf.getName())
					.append("=")
					.append(pf.getType() == java.util.Date.class ? CommonUtil
							.formatDate((Date) obj, "yyyyMMddHHmmss")
							: URLEncoder.encode((String) obj, CHAR_SET))
					.append("&");
		}

		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * ��������xml
	 * 
	 * @param draw
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String setRequestXml(Object draw) throws IllegalArgumentException,
			IllegalAccessException {
		String opCode = "";
		String opName = "";
		String serviceVersionElement = "";
		if (draw instanceof BatchDrawDO) {
			draw = (BatchDrawDO) draw;
			opCode = BATCH_DRAW_OP_CODE;
			opName = BATCH_DRAW_OP_NAME;
		} else if (draw instanceof BatchDrawQueryDO) {
			draw = (BatchDrawQueryDO) draw;
			opCode = BATCH_DRAW_QUERY_OP_CODE;
			opName = BATCH_DRAW_QUERY_OP_NAME;
			serviceVersionElement = "<service_version>" + SERVICE_VERSION
					+ "</service_version>\n";
		}

		Field[] drawFields = draw.getClass().getFields();
		StringBuffer xmlStr = new StringBuffer();
		StringBuffer eleStr = new StringBuffer();

		for (Field df : drawFields) {
			eleStr.setLength(0);
			if (df.getType() == java.util.Set.class) {
				Set<BatchDrawUsersDO> userSet = (HashSet<BatchDrawUsersDO>) df
						.get(draw);
				Iterator<BatchDrawUsersDO> ite = userSet.iterator();
				xmlStr.append("<record_set>\n");
				Field[] drawUserFields;
				BatchDrawUsersDO drawUser;
				while (ite.hasNext()) {
					drawUser = ite.next();
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
						.append("<op_code>").append(opCode)
						.append("</op_code>\n").append("<op_name>")
						.append(opName).append("</op_name>\n")
						.append(serviceVersionElement)).append("</root>");

		// System.out.println(xmlStr);
		LOG.info("request xml=" + xmlStr);

		return xmlStr.toString().replaceAll("\n|\t", "");
	}

	/**
	 * ���ӲƸ�ͨ
	 * 
	 * @param url
	 * @param content
	 * @return
	 */
	private String connect(String url, String content) {
		LOG.info("request content=" + content);
		try {
			// ֤����֤
			SSLContext ctx = SSLUtil.getSSLContext(CA_FILE, CERT_FILE,
					CERT_PASSWORD);

			// ��ȡ����
			HttpsURLConnection conn = ConnectionUtil.getHttpsURLConnection(url,
					ctx);

			// ���
			BufferedOutputStream out = new BufferedOutputStream(
					conn.getOutputStream());
			ConnectionUtil.doOutput(out, content.getBytes(CHAR_SET), 1024);
			out.close();

			// ��ȡӦ��������
			InputStream is = conn.getInputStream();
			if (is != null) {
				String resContent = ConnectionUtil.InputStreamTOString(is,
						CHAR_SET);
				is.close();
				conn.disconnect();
				LOG.info("response content=" + resContent);
				return resContent;
			}
		} catch (SocketTimeoutException e) {
			throw new FepServiceRetException(FepServiceRetException.CFT_ERROR,
					"���������ڴ����У����Ժ��ѯ���");
		} catch (IOException e) {
			throw new FepServiceRetException(FepServiceRetException.CERT_ERROR,
					"֤���������֤��ͨ��");
		} catch (Exception e) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "���ӲƸ�ͨ�쳣");
		}

		return null;
	}

	/**
	 * �������ؽ��
	 * 
	 * @param xml
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws JDOMException
	 */
	private Object getOutput(BusType type, String xml) throws JDOMException,
			IOException, ParseException {
		BufferedReader br = new BufferedReader(new StringReader(xml));
		switch (type) {
		case BATCH_DRAW: {
			Map<String, String> xmlMap = XmlParseUtil.xmlToMap(br);
			return setBatchDrawOutput(xmlMap);
		}
		case BATCH_DRAW_QUERY: {
			Set<Map> eleSet = XMLUtil.parseXmlToSet(br);
			return setBatchDrawQueryOutput(eleSet);
		}
		case PAY_REFUND_QUERY: {
			Set<Map> eleSet = XMLUtil.parseXmlToSet(br);
			return setPayRefundQueryOutput(eleSet);
		}
		default:
			return null;
		}
	}

	/**
	 * ���ø�������������
	 * 
	 * @param xmlMap
	 * @return
	 * @throws ParseException
	 */
	private BatchDrawOutput setBatchDrawOutput(Map<String, String> xmlMap)
			throws ParseException {
		BatchDrawOutput output = new BatchDrawOutput();
		output.setOp_code(xmlMap.get("op_code"));
		output.setOp_name(xmlMap.get("op_name"));
		output.setOp_user(xmlMap.get("op_user"));
		output.setPackage_id(xmlMap.get("package_id"));
		output.setRetcode(xmlMap.get("retcode"));
		output.setRetmsg(xmlMap.get("retmsg"));

		if (null != xmlMap.get("op_time")) {
			output.setOp_time(CommonUtil.parseDate(xmlMap.get("op_time"),
					"yyyyMMddHHmmssSSS"));
		}

		return output;
	}

	/**
	 * ������Ʊ��ѯ�������
	 * 
	 * @param eleSet
	 * @return
	 * @throws ParseException
	 */
	private PayRefundQueryOutput setPayRefundQueryOutput(Set<Map> eleSet)
			throws ParseException {
		PayRefundQueryOutput output = new PayRefundQueryOutput();
		output.setCancel_set(new HashSet<PayRefundQueryUsersDO>());
		PayRefundQueryUsersDO refundUser;
		Map<String, String> xmlMap;
		String setName;
		String retCode = "";
		for (Iterator<Map> ite = eleSet.iterator(); ite.hasNext();) {
			xmlMap = ite.next();
			setName = xmlMap.get("set_name");

			if ("root".equals(setName)) {
				retCode = xmlMap.get("retcode");
				output.setSign_type(xmlMap.get("sign_type"));
				output.setService_version(xmlMap.get("service_version"));
				output.setInput_charset(xmlMap.get("input_charset"));
				output.setSign(xmlMap.get("sign"));
				output.setSign_key_index(xmlMap.get("sign_key_index"));
				output.setPartner(xmlMap.get("partner"));
				output.setRetcode(retCode);
				output.setRetmsg(xmlMap.get("retmsg"));

				if (null != CommonUtil.trimString(xmlMap.get("cancel_count"))) {
					output.setCancel_count(Integer.valueOf(xmlMap
							.get("cancel_count")));
				}

			}

			if ("cancel_rec".equals(setName) && retCode.matches("0|00")) {
				refundUser = new PayRefundQueryUsersDO();
				refundUser.setDraw_id(xmlMap.get("draw_id"));
				refundUser.setPackage_id(xmlMap.get("package_id"));
				refundUser.setSerial(xmlMap.get("serial"));
				refundUser.setPay_amt(Integer.valueOf(xmlMap.get("pay_amt")));
				refundUser.setBank_type(xmlMap.get("bank_type"));
				refundUser.setCancel_res(xmlMap.get("cancel_res"));

				if (null != xmlMap.get("draw_time")) {
					refundUser.setDraw_time(CommonUtil.parseDate(
							xmlMap.get("draw_time"), "yyyy-MM-dd HH:mm:ss"));
				}

				if (null != xmlMap.get("cancel_time")) {
					refundUser.setCancel_time(CommonUtil.parseDate(
							xmlMap.get("cancel_time"), "yyyy-MM-dd"));// HH:mm:ss
				}

				output.getCancel_set().add(refundUser);
			}
		}
		return output;
	}

	/**
	 * ���ò�ѯ�������
	 * 
	 * @param eleSet
	 * @return
	 * @throws ParseException
	 */
	private BatchDrawQueryOutput setBatchDrawQueryOutput(Set<Map> eleSet)
			throws ParseException {
		BatchDrawQueryOutput output = new BatchDrawQueryOutput();
		output.setOrigin_set(new HashSet<BatchDrawQueryUsersDO>());
		output.setSuccess_set(new HashSet<BatchDrawQueryUsersDO>());
		output.setTobank_set(new HashSet<BatchDrawQueryUsersDO>());
		output.setFail_set(new HashSet<BatchDrawQueryUsersDO>());
		output.setHandling_set(new HashSet<BatchDrawQueryUsersDO>());
		output.setReturn_ticket_set(new HashSet<BatchDrawQueryUsersDO>());

		BatchDrawQueryUsersDO drawUser;
		Map<String, String> xmlMap;
		String setName;
		String retCode = "";
		for (Iterator<Map> ite = eleSet.iterator(); ite.hasNext();) {
			xmlMap = ite.next();
			setName = xmlMap.get("set_name");

			if ("root".equals(setName)) {
				retCode = xmlMap.get("retcode");
				output.setOp_code(xmlMap.get("op_code"));
				output.setOp_name(xmlMap.get("op_name"));
				output.setOp_user(xmlMap.get("op_user"));
				output.setPackage_id(xmlMap.get("package_id"));
				output.setRetcode(retCode);
				output.setRetmsg(xmlMap.get("retmsg"));

				if (null != xmlMap.get("op_time")) {
					output.setOp_time(CommonUtil.parseDate(
							xmlMap.get("op_time"), "yyyyMMddHHmmssSSS"));
				}
			}

			if (retCode.matches("0|00")) {
				if ("result".equals(setName)) {
					output.setTrade_state(xmlMap.get("trade_state"));
					output.setTotal_count(Integer.valueOf(xmlMap
							.get("total_count")));
					output.setTotal_fee(Integer.valueOf(xmlMap.get("total_fee")));
					output.setSucc_count(Integer.valueOf(xmlMap
							.get("succ_count")));
					output.setSucc_fee(Integer.valueOf(xmlMap.get("succ_fee")));
					output.setFail_count(Integer.valueOf(xmlMap
							.get("fail_count")));
					output.setFail_fee(Integer.valueOf(xmlMap.get("fail_fee")));
				}

				if (setName.matches(".*_rec")) {
					drawUser = new BatchDrawQueryUsersDO();
					drawUser.setSerial(xmlMap.get("serial"));
					drawUser.setRec_bankacc(xmlMap.get("rec_bankacc"));
					drawUser.setBank_type(xmlMap.get("bank_type"));
					drawUser.setRec_name(xmlMap.get("rec_name"));
					drawUser.setPay_amt(Integer.valueOf(xmlMap.get("pay_amt")));
					drawUser.setAcc_type(xmlMap.get("acc_type"));
					drawUser.setArea(xmlMap.get("area"));
					drawUser.setCity(xmlMap.get("city"));
					drawUser.setSubbank_name(xmlMap.get("subbank_name"));
					drawUser.setDesc(xmlMap.get("desc"));

					if (null != xmlMap.get("modify_time")) {
						drawUser.setModify_time(CommonUtil.parseDate(
								xmlMap.get("modify_time"),
								"yyyy-MM-dd HH:mm:ss"));
					}

					if ("origin_rec".equals(setName)) {
						output.getOrigin_set().add(drawUser);
					} else if ("suc_rec".equals(setName)) {
						output.getSuccess_set().add(drawUser);
					} else if ("tobank_rec".equals(setName)) {
						output.getTobank_set().add(drawUser);
					} else if ("fail_rec".equals(setName)) {
						output.getFail_set().add(drawUser);
					} else if ("handling_rec".equals(setName)) {
						output.getHandling_set().add(drawUser);
					} else if ("ret_ticket_rec".equals(setName)) {
						output.getReturn_ticket_set().add(drawUser);
					}
				}
			}
		}
		return output;
	}

	public FepDAO getFepDAO() {
		return fepDAO;
	}

	public void setFepDAO(FepDAO fepDAO) {
		this.fepDAO = fepDAO;
	}

}
