package com.zhaocb.zcb_app.finance.fep.finance;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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

/**
 * 财付通实现类
 * 
 * @author zhl
 *
 */
public class CftFacadeImpl implements CftFacade {

	private static final String CHAR_SET = "GB2312";

	// 财付通付款、查询操作编码和名称信息
	private static final String BATCH_DRAW_OP_CODE = "1013";
	private static final String BATCH_DRAW_OP_NAME = "batch_draw";
	private static final String BATCH_DRAW_QUERY_OP_CODE = "1014";
	private static final String BATCH_DRAW_QUERY_OP_NAME = "batch_draw_query";
	private static final String SERVICE_VERSION = "1.2";

	// 处理返回对象业务类型
	private static enum BusType {
		BATCH_DRAW, BATCH_DRAW_QUERY, PAY_REFUND_QUERY
	}

	private static String MERCHANT_KEY;// 商户key
	private static String CFT_URL;// 财付通url

	// 证书验证key和密码
	private static String CLIENT_KEY_STORE;
	private static String CLIENT_TRUST_KEY_STORE;
	private static String CLIENT_KEY_STORE_PASSWORD;
	private static String CLIENT_TRUST_KEY_STORE_PASSWORD;

	private FepDAO fepDAO;

	public BatchDrawOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception {
		// 检查参数
		checkParam(batchDrawDO);

		// 把输入对象解析成xml
		String reqXml = setRequestXml(batchDrawDO);

		// 生成参数params
		String params = genParams(reqXml);

		// 提交付款申请
		String respXml = connect(CFT_URL, params);

		// 返回解析结果
		return (BatchDrawOutput) parseResponseXml(BusType.BATCH_DRAW, respXml);
	}

	public BatchDrawQueryOutput batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO)
			throws Exception {
		// 检查参数
		checkParam(batchDrawQueryDO);

		// 把输入对象解析成xml
		String reqXml = setRequestXml(batchDrawQueryDO);

		// 生成参数params
		String params = genParams(reqXml);

		// 提交付款查询
		String respXml = connect(CFT_URL, params);

		// 返回解析结果
		return (BatchDrawQueryOutput) parseResponseXml(
				BusType.BATCH_DRAW_QUERY, respXml);
	}

	public PayRefundQueryOutput payRefundQuery(PayRefundQueryDO payRefundQueryDO)
			throws Exception {
		// 检查参数
		checkParam(payRefundQueryDO);

		// 生成参数params
		String params = genParams(payRefundQueryDO);

		// 提交退票查询
		String respXml = connect(CFT_URL, params);

		// 返回解析结果
		return (PayRefundQueryOutput) parseResponseXml(
				BusType.PAY_REFUND_QUERY, respXml);
	}

	/**
	 * 检查参数－退票查询
	 * 
	 * @param payRefundQueryDO
	 */
	private void checkParam(PayRefundQueryDO payRefundQueryDO) {
		// 检查财付通配置信息
		checkCftConfig("cft_refund_url");

		// 检查明细业务逻辑参数
		String sign = payRefundQueryDO.getSign();
		String partner = payRefundQueryDO.getPartner();
		Date startTime = payRefundQueryDO.getStart_time();
		Date endTime = payRefundQueryDO.getEnd_time();

		if (null == CommonUtil.trimString(sign)) {
			throw new ParameterInvalidException("签名不能为空");
		} else if (sign.length() != 32) {
			throw new ParameterInvalidException("签名长度应该为32位");
		} else if (null == CommonUtil.trimString(partner)) {
			throw new ParameterInvalidException("商户号不能为空");
		} else if (!partner.matches("120[\\d]{7}")) {
			throw new ParameterInvalidException("商户号格式不正确");
		} else if (null == startTime) {
			throw new ParameterInvalidException("开始时间不能为空");
		} else if (null == endTime) {
			throw new ParameterInvalidException("结束时间不能为空");
		}

		// 验证银行编码
		if (null == fepDAO.queryBankInfoByCode(payRefundQueryDO.getBank_type())) {
			throw new ParameterInvalidException("银行编码不合法");
		}
	}

	/**
	 * 检查参数－查询付款进度
	 * 
	 * @param drawQuery
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void checkParam(BatchDrawQueryDO drawQuery)
			throws IllegalArgumentException, IllegalAccessException {
		// 检查财付通配置信息
		checkCftConfig("cft_url");

		// 检查明细业务逻辑参数
		Field[] fields = drawQuery.getClass().getFields();
		String fieldName;
		for (Field qf : fields) {
			fieldName = qf.getName();
			if (null == qf.get(drawQuery)
					|| null == CommonUtil.trimString(qf.get(drawQuery)
							.toString())) {
				throw new ParameterInvalidException(fieldName + "不能为空");
			}
		}
	}

	/**
	 * 检查参数－付款申请
	 * 
	 * @param draw
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void checkParam(BatchDrawDO draw) throws IllegalArgumentException,
			IllegalAccessException {
		// 检查财付通配置信息
		checkCftConfig("cft_url");

		// 检查明细业务逻辑参数
		int totalNum = draw.getTotal_num();
		if (totalNum != draw.getUsers_set().size()) {
			throw new ParameterInvalidException("总记录数必须与明细记录数相同");
		} else if (totalNum > 20000) {
			throw new ParameterInvalidException("一个批次支持最多2万笔记录");
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
				String acctType;// 账户类型
				String recName;// 收款姓名
				String bankType;// 银行类型
				BankInfo bankInfo;// 银行信息
				Map<String, String> areaCityMap;
				while (ite.hasNext()) {
					drawUser = ite.next();
					drawUserFields = drawUser.getClass().getFields();
					for (Field uf : drawUserFields) {
						fieldName = uf.getName();
						if (fieldName.matches("subbank_name")) {// 此处跳过,根据银行验证支行是否必填
							continue;
						}
						if (null == uf.get(drawUser)
								|| null == CommonUtil.trimString(uf.get(
										drawUser).toString())) {
							throw new ParameterInvalidException(fieldName
									+ "不能为空");
						}
					}

					if (drawUser.getSerial().length() > 32) {
						throw new ParameterInvalidException(
								"同一个批次内的明细序号不能超过32个字符");
					}

					if (!serialSet.add(drawUser.getSerial())) {
						throw new ParameterInvalidException("同一个批次内的明细序号要保证唯一");
					}

					// 验证帐户类型和收款方名称
					acctType = drawUser.getAcc_type();
					if (!acctType.matches("1|2")) {
						throw new ParameterInvalidException("账户类型只能为1或2");
					} else {
						recName = drawUser.getRec_name();
						if ("1".equals(acctType)
								&& recName.getBytes().length < 4) {
							throw new ParameterInvalidException(
									"收款方个人名称需大于等于4个字节");
						}
						if ("2".equals(acctType)
								&& recName.getBytes().length < 9) {
							throw new ParameterInvalidException(
									"收款方公司名称需大于等于4个字节");
						}
					}

					// 验证银行、地区、城市信息
					bankType = drawUser.getBank_type();
					bankInfo = fepDAO.queryBankInfoByCode(bankType);
					if (null == bankInfo) {
						throw new ParameterInvalidException("银行类型不合法");
					} else {
						// 开户省市,如果银行不验证可以填写为0
						if ("Y".equals(bankInfo.getAreaRequestPub())) {
							areaCityMap = new HashMap<String, String>();
							areaCityMap.put("areaCode", drawUser.getArea());
							areaCityMap.put("cityCode", drawUser.getCity());
							if (fepDAO.queryAreaCityByCode(areaCityMap) < 1) {
								throw new ParameterInvalidException("省市编码不合法");
							}
						} else {
							drawUser.setArea("0");
							drawUser.setCity("0");
						}

						// 支行名称,如果银行不验证可以填写为全角空格
						if ("Y".equals(bankInfo.getSubBankRequestPub())) {
							// if
							// (!drawUser.getSubbank_name().matches(".*银行.*")) {
							// throw new ParameterInvalidException(
							// "支行名称前面必须包括完整的银行名称");
							// }
						} else {
							drawUser.setSubbank_name("　");
						}
					}

					// 验证付款金额
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
					throw new ParameterInvalidException(fieldName + "不能为空");
				}
			}
		}

		if (totalAmt != draw.getTotal_amt()) {
			throw new ParameterInvalidException("总金额必须等于明细记录金额之和");
		}

		if (draw.getPackage_id().length() > 30) {
			throw new ParameterInvalidException("批次号长度不能超过30个字符");
		}
	}

	/**
	 * 检查财付通配置信息
	 */
	private void checkCftConfig(String url) {
		// 检查商户key
		MERCHANT_KEY = CommonUtil.getWebConfig("mer_key");
		if (null == MERCHANT_KEY) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "商户key未配置");
		}

		// 检查财付通url
		CFT_URL = CommonUtil.getWebConfig(url);
		if (null == CFT_URL) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "财付通url未配置");
		}

		// 检查财付通证书验证相关文件路径和密码
		CLIENT_KEY_STORE = CommonUtil.getWebConfig("kclient.keystore");
		CLIENT_TRUST_KEY_STORE = CommonUtil.getWebConfig("tclient.keystore");
		CLIENT_KEY_STORE_PASSWORD = CommonUtil.getWebConfig("kclient.password");
		CLIENT_TRUST_KEY_STORE_PASSWORD = CommonUtil
				.getWebConfig("tclient.password");
		if (null == CLIENT_KEY_STORE || null == CLIENT_TRUST_KEY_STORE
				|| null == CLIENT_KEY_STORE_PASSWORD
				|| null == CLIENT_TRUST_KEY_STORE_PASSWORD) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "财付通验证证书文件路径或密码未配置");
		}

	}

	/**
	 * 生成参数params
	 * 
	 * @param reqXml
	 * @return
	 * @throws Exception
	 */
	private String genParams(String reqXml) throws Exception {
		// 对xml进行base64编码生成参数content
		String content = Base64Util.encodeBase64(reqXml.getBytes(CHAR_SET));

		// 加密生成参数abstract
		String abstct = MD5Util.getMD5(
				MD5Util.getMD5(content, CHAR_SET).toLowerCase() + MERCHANT_KEY,
				CHAR_SET).toLowerCase();

		// 连接成参数params
		StringBuffer params = new StringBuffer("content=").append(content)
				.append("&abstract=").append(abstct);

		return URLEncoder.encode(params.toString(), CHAR_SET);
	}

	/**
	 * 生成参数params-退票查询
	 * 
	 * @param payRefundQuery
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws UnsupportedEncodingException
	 */
	private String genParams(PayRefundQueryDO payRefundQuery)
			throws IllegalArgumentException, IllegalAccessException,
			UnsupportedEncodingException {
		Field[] fields = payRefundQuery.getClass().getFields();
		StringBuffer sb = new StringBuffer();
		for (Field pf : fields) {
			sb.append(pf.getName())
					.append("=")
					.append(pf.getType() == java.util.Date.class ? CommonUtil
							.formatDate((Date) pf.get(payRefundQuery),
									"yyyyMMddHHmmss") : pf.get(payRefundQuery))
					.append("&");
		}

		return URLEncoder.encode(sb.substring(0, sb.length() - 1), CHAR_SET);
	}

	/**
	 * 设置请求xml
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

		System.out.println(xmlStr);

		return xmlStr.toString().replaceAll("\n|\t", "");
	}

	/**
	 * 连接财付通
	 * 
	 * @param url
	 * @param content
	 * @return
	 */
	private String connect(String url, String content) {
		SSLContext ctx = getSSLInstance();
		try {
			URL postUrl = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) postUrl
					.openConnection();
			conn.setSSLSocketFactory(ctx.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.connect();

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes(CHAR_SET));
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
				conn.disconnect();
				return outStream.toString(CHAR_SET);
			}
		} catch (SocketTimeoutException e) {
			throw new FepServiceRetException(FepServiceRetException.CFT_ERROR,
					"该批次正在处理中，请稍后查询结果");
		} catch (Exception e) {

			return "<?xml version=\"1.0\" encoding=\"GB2312\" ?>" + "<root>"
					+ "<charset>GB2312</charset>" + "<op_code>1013</op_code>"
					+ "<op_name>batch_draw</op_name>"
					+ "<op_user>提交人ID</op_user>"
					+ "<op_time>20150501102231888</op_time>"
					+ "<package_id>20150501786</package_id>"
					+ "<result>(本接口result恒为空)</result>"
					+ "<retcode>00</retcode>" + "<retmsg>错误内容描述</retmsg>"
					+ "</root>";

			/*
			 * return "<?xml version=\"1.0\" encoding=\"GB2312\" ?><root>" +
			 * "<op_code>1014</op_code><op_name>batch_draw_query</op_name>" +
			 * "<op_user>提交人ID</op_user><op_time>20141102232011333</op_time>" +
			 * "<package_id>20141102000</package_id><retcode>00</retcode>" +
			 * "<retmsg>错误内容描述</retmsg><result><trade_state>1</trade_state>" +
			 * "<total_count >1</total_count><total_fee>120000000</total_fee>" +
			 * "<succ_count>1</succ_count><succ_fee>50000</succ_fee>" +
			 * "<fail_count>1</fail_count><fail_fee>70</fail_fee>" +
			 * "<origin_set><origin_total>2</origin_total>" +
			 * "<origin_rec><serial>S000001</serial><rec_bankacc>62220000</rec_bankacc>"
			 * + "<bank_type>1</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>6545600</pay_amt><acc_type>2</acc_type>" +
			 * "<area>001</area><city>323</city><subbank_name>初始支行1</subbank_name>"
			 * +
			 * "<desc>初始付款说明</desc><modify_time>2015-01-22 22:10:33</modify_time>"
			 * + "</origin_rec>" +
			 * "<origin_rec><serial>S000002</serial><rec_bankacc>62220001</rec_bankacc>"
			 * + "<bank_type>2</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>332344</pay_amt><acc_type>2</acc_type>" +
			 * "<area>122</area><city>133</city><subbank_name>初始支行2</subbank_name>"
			 * +
			 * "<desc>初始付款说明</desc><modify_time>2015-01-22 22:10:33</modify_time>"
			 * + "</origin_rec>" + "</origin_set>"
			 * 
			 * + "<success_set><origin_total>2</origin_total>" +
			 * "<suc_rec><serial>S000003</serial><rec_bankacc>62250000</rec_bankacc>"
			 * + "<bank_type>1</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>6545600</pay_amt><acc_type>2</acc_type>" +
			 * "<area>367</area><city>666</city><subbank_name>成功支行1</subbank_name>"
			 * +
			 * "<desc>成功付款说明</desc><modify_time>2015-04-22 22:10:33</modify_time>"
			 * + "</suc_rec>" +
			 * "<suc_rec><serial>S000004</serial><rec_bankacc>62250001</rec_bankacc>"
			 * + "<bank_type>2</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>332344</pay_amt><acc_type>2</acc_type>" +
			 * "<area>976</area><city>777</city><subbank_name>成功支行2</subbank_name>"
			 * +
			 * "<desc>成功付款说明</desc><modify_time>2015-04-22 22:10:33</modify_time>"
			 * + "</suc_rec>" + "</success_set>"
			 * 
			 * + "<tobank_set><tobank_total>3</tobank_total>" +
			 * "<tobank_rec><serial>S000006</serial><rec_bankacc>62260000</rec_bankacc>"
			 * + "<bank_type>1</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>5555500</pay_amt><acc_type>2</acc_type>" +
			 * "<area>001</area><city>323</city><subbank_name>已提交银行支行7</subbank_name>"
			 * +
			 * "<desc>已提交银行付款说明</desc><modify_time>2015-05-27 22:10:33</modify_time>"
			 * + "</tobank_rec>" +
			 * "<tobank_rec><serial>S000007</serial><rec_bankacc>62260000</rec_bankacc>"
			 * + "<bank_type>1</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>77777700</pay_amt><acc_type>2</acc_type>" +
			 * "<area>001</area><city>323</city><subbank_name>已提交银行支行6</subbank_name>"
			 * +
			 * "<desc>已提交银行付款说明</desc><modify_time>2015-05-22 22:10:33</modify_time>"
			 * + "</tobank_rec>" +
			 * "<tobank_rec><serial>S000005</serial><rec_bankacc>62260001</rec_bankacc>"
			 * + "<bank_type>1</bank_type><rec_name>周红亮</rec_name>" +
			 * "<pay_amt>6666600</pay_amt><acc_type>2</acc_type>" +
			 * "<area>122</area><city>133</city><subbank_name>已提交银行支行5</subbank_name>"
			 * +
			 * "<desc>已提交银行付款说明</desc><modify_time>2015-05-05 22:10:33</modify_time>"
			 * + "</tobank_rec>" + "</tobank_set></result></root>";
			 */

			/*
			 * return "<?xml version=\"1.0\" encoding=\"GB2312\" ?>" + "<root>"
			 * + "<retcode>0</retcode>" + "<retmsg>错误内容描述</retmsg>" +
			 * "<sign_type>md5</sign_type>" +
			 * "<service_version>1.0</service_version>" +
			 * "<input_charset>gbk</input_charset>" +
			 * "<sign_key_index>1</sign_key_index>" +
			 * "<partner>1201111111</partner>" +
			 * "<cancel_count>2</cancel_count>" +
			 * "<sign>11111111112222222222333333333311</sign>" + "<cancel_set>"
			 * + "<cancel_rec>" + "<draw_id>P000001</draw_id>" +
			 * "<package_id>20150123888</package_id>" +
			 * "<serial>S000001</serial>" + "<pay_amt>111000</pay_amt>" +
			 * "<bank_type>1101</bank_type>" +
			 * "<draw_time>2015-04-05 11:08:23</draw_time>" +
			 * "<cancel_time>2015-04-06 12:18:21</cancel_time>" +
			 * "<cancel_res>退票原因1</cancel_res>" + "</cancel_rec>" +
			 * "<cancel_rec>" + "<draw_id>P000002</draw_id>" +
			 * "<package_id>20150123999</package_id>" +
			 * "<serial>S000002</serial>" + "<pay_amt>222000</pay_amt>" +
			 * "<bank_type>1102</bank_type>" +
			 * "<draw_time>2015-05-05 11:08:23</draw_time>" +
			 * "<cancel_time>2015-05-06 12:18:21</cancel_time>" +
			 * "<cancel_res>退票原因2</cancel_res>" + "</cancel_rec>" +
			 * "</cancel_set>" + "</root>";
			 */

			// throw new
			// FepServiceRetException(FepServiceRetException.SYSTEM_ERROR,
			// "连接财付通异常");
		}

		return null;
	}

	/**
	 * 加载验证证书
	 * 
	 * @return
	 */
	private SSLContext getSSLInstance() {
		SSLContext ctx;
		try {
			// 初始化密钥库
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance("SunX509");
			KeyStore keyStore = getKeyStore(CLIENT_KEY_STORE,
					CLIENT_KEY_STORE_PASSWORD);
			keyManagerFactory.init(keyStore,
					CLIENT_KEY_STORE_PASSWORD.toCharArray());

			// 初始化信任库
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance("SunX509");
			KeyStore trustkeyStore = getKeyStore(CLIENT_TRUST_KEY_STORE,
					CLIENT_TRUST_KEY_STORE_PASSWORD);
			trustManagerFactory.init(trustkeyStore);

			// 初始化SSL上下文
			ctx = SSLContext.getInstance("SSL");
			ctx.init(keyManagerFactory.getKeyManagers(),
					trustManagerFactory.getTrustManagers(), null);
		} catch (Exception e) {
			throw new FepServiceRetException(
					FepServiceRetException.SYSTEM_ERROR, "验证证书失败");
		}

		return ctx;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password)
			throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	/**
	 * 解析返回结果
	 * 
	 * @param xml
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws JDOMException
	 */
	private Object parseResponseXml(BusType type, String xml)
			throws ParseException, JDOMException, IOException {
		BufferedReader br = new BufferedReader(new StringReader(xml));
		switch (type) {
		case BATCH_DRAW: {
			Map<String, String> xmlMap = XmlParseUtil.xmlToMap(br);
			BatchDrawOutput output = new BatchDrawOutput();
			output.setOp_code(xmlMap.get("op_code"));
			output.setOp_name(xmlMap.get("op_name"));
			output.setOp_user(xmlMap.get("op_user"));
			output.setOp_time(CommonUtil.parseDate(xmlMap.get("op_time"),
					"yyyyMMddHHmmssSSS"));
			output.setPackage_id(xmlMap.get("package_id"));
			output.setRetcode(xmlMap.get("retcode"));
			output.setRetmsg(xmlMap.get("retmsg"));
			return output;
		}
		case BATCH_DRAW_QUERY: {
			Set<Map> eleSet = parseXmlToSet(br);
			return setBatchDrawQueryOutput(eleSet);
		}
		case PAY_REFUND_QUERY: {
			Set<Map> eleSet = parseXmlToSet(br);
			return setPayRefundQueryOutput(eleSet);
		}
		default:
			break;
		}

		return null;
	}

	/**
	 * 设置退票查询结果对象
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
		for (Iterator<Map> ite = eleSet.iterator(); ite.hasNext();) {
			xmlMap = ite.next();
			setName = xmlMap.get("set_name");

			if ("root".equals(setName)) {
				output.setSign_type(xmlMap.get("sign_type"));
				output.setService_version(xmlMap.get("service_version"));
				output.setInput_charset(xmlMap.get("input_charset"));
				output.setSign(xmlMap.get("sign"));
				output.setSign_key_index(xmlMap.get("sign_key_index"));
				output.setPartner(xmlMap.get("partner"));
				output.setRetcode(xmlMap.get("retcode"));
				output.setRetmsg(xmlMap.get("retmsg"));
				output.setCancel_count(Integer.valueOf(xmlMap
						.get("cancel_count")));
			}

			if ("cancel_rec".equals(setName)) {
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
							xmlMap.get("cancel_time"), "yyyy-MM-dd HH:mm:ss"));
				}

				output.getCancel_set().add(refundUser);
			}
		}
		return output;
	}

	/**
	 * 设置查询结果对象
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
		for (Iterator<Map> ite = eleSet.iterator(); ite.hasNext();) {
			xmlMap = ite.next();
			setName = xmlMap.get("set_name");

			if ("root".equals(setName)) {
				output.setOp_code(xmlMap.get("op_code"));
				output.setOp_name(xmlMap.get("op_name"));
				output.setOp_user(xmlMap.get("op_user"));
				output.setOp_time(CommonUtil.parseDate(xmlMap.get("op_time"),
						"yyyyMMddHHmmssSSS"));
				output.setPackage_id(xmlMap.get("package_id"));
				output.setRetcode(xmlMap.get("retcode"));
				output.setRetmsg(xmlMap.get("retmsg"));
			}

			if ("result".equals(setName)) {
				output.setTrade_state(xmlMap.get("trade_state"));
				output.setTotal_count(Integer.valueOf(xmlMap.get("total_count")));
				output.setTotal_fee(Integer.valueOf(xmlMap.get("total_fee")));
				output.setSucc_count(Integer.valueOf(xmlMap.get("succ_count")));
				output.setSucc_fee(Integer.valueOf(xmlMap.get("succ_fee")));
				output.setFail_count(Integer.valueOf(xmlMap.get("fail_count")));
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
							xmlMap.get("modify_time"), "yyyy-MM-dd HH:mm:ss"));
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
		return output;
	}

	/**
	 * 解析XML到SET
	 * 
	 * @param br
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	private Set<Map> parseXmlToSet(BufferedReader br) throws JDOMException,
			IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(br);
		Set<Map> eleSet = new HashSet<Map>();
		Element element = doc.getRootElement();
		elementLoop(element, element.getName(), new HashMap<String, String>(),
				eleSet);
		return eleSet;
	}

	/**
	 * 递归遍历xml节点
	 * 
	 * @param element
	 * @param elementName
	 * @param eleMap
	 * @param eleSet
	 */
	private void elementLoop(Element element, String elementName,
			Map<String, String> eleMap, Set<Map> eleSet) {
		List<Element> elements = element.getChildren();
		if (elements.isEmpty()) {
			if (elementName.matches("root|result|.*_rec")) {
				eleMap.put(element.getName(), element.getValue());
			}
		} else {
			Iterator<Element> iter = elements.iterator();
			eleMap = new HashMap<String, String>();
			eleMap.put("set_name", element.getName());
			eleSet.add(eleMap);
			while (iter.hasNext()) {
				element = iter.next();
				elementLoop(element, element.getParentElement().getName(),
						eleMap, eleSet);
			}
		}
	}

	/**
	 * 主机名验证
	 * 
	 * @author zhl
	 *
	 */
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
