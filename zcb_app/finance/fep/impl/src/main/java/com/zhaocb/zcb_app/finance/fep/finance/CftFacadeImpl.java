package com.zhaocb.zcb_app.finance.fep.finance;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

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

	private static final String CHAR_SET = "GB2312";
	private static final String BATCH_DRAW_OP_CODE = "1013";
	private static final String BATCH_DRAW_OP_NAME = "batch_draw";

	private static final String BATCH_DRAW_QUERY_OP_CODE = "1014";
	private static final String BATCH_DRAW_QUERY_OP_NAME = "batch_draw_query";

	private static enum BusType {
		BATCH_DRAW, BATCH_DRAW_QUERY, REFUND_QUERY
	};

	private static String MERCHANT_KEY;
	private static String CFT_URL;
	private static String CLIENT_KEY_STORE;
	private static String CLIENT_TRUST_KEY_STORE;
	private static String CLIENT_KEY_STORE_PASSWORD;
	private static String CLIENT_TRUST_KEY_STORE_PASSWORD;

	private FepDAO fepDAO;

	public CftCommOutput batchDraw(BatchDrawDO batchDrawDO) throws Exception {
		// 检查输入参数
		checkParam(batchDrawDO);

		// 把输入对象解析成xml
		String reqXml = setRequestXml(batchDrawDO);

		// 对xml进行base64编码生成参数content
		String content = Base64Util.encodeBase64(reqXml.getBytes(CHAR_SET));

		// 加密生成参数abstract
		String abstct = MD5Util.getMD5(
				MD5Util.getMD5(content, CHAR_SET).toLowerCase() + MERCHANT_KEY,
				CHAR_SET).toLowerCase();

		// 连接参数params
		StringBuffer params = new StringBuffer("content=").append(content)
				.append("&abstract=").append(abstct);

		// 提交付款申请
		String respXml = connect(CFT_URL, params.toString());

		// 返回解析结果
		return (CftCommOutput) parseResponseXml(BusType.BATCH_DRAW, respXml);
	}

	public String batchDrawQuery(BatchDrawQueryDO batchDrawQueryDO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 检查参数
	 * 
	 * @param draw
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void checkParam(BatchDrawDO draw) throws IllegalArgumentException,
			IllegalAccessException {
		// 检查商户key
		MERCHANT_KEY = CommonUtil.getWebConfig("mer_key");
		if (null == MERCHANT_KEY) {
			throw new CommonException(CommonException.SYSTEM_ERROR, "商户key未配置");
		}

		// 检查财付通url
		CFT_URL = CommonUtil.getWebConfig("cft_url");
		if (null == CFT_URL) {
			throw new CommonException(CommonException.SYSTEM_ERROR, "财付通url未配置");
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
			throw new CommonException(CommonException.SYSTEM_ERROR,
					"财付通验证证书文件路径或密码未配置");
		}

		// 检查付款明细参数
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
				Map areaCityMap;
				while (ite.hasNext()) {
					drawUser = (BatchDrawUsersDO) ite.next();
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
							if (!drawUser.getSubbank_name().matches(".*银行.*")) {
								throw new ParameterInvalidException(
										"支行名称前面必须包括完整的银行名称");
							}
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
			throw new ParameterInvalidException("批次号长度限制为30个字符");
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
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console
					.openConnection();
			conn.setSSLSocketFactory(ctx.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes(CHAR_SET));

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
				return new String(outStream.toByteArray(), CHAR_SET);
			}
		} catch (Exception e) {
			/*
			 * return "<?xml version=\"1.0\" encoding=\"GB2312\" ?>" + "<root>"
			 * + "<charset>GB2312</charset>" + "<op_code>1013</op_code>" +
			 * "<op_name>batch_draw</op_name>" + "<op_user>提交人ID</op_user>" +
			 * "<op_time>操作时间（yyyyMMddHHmmssSSS）</op_time>" +
			 * "<package_id>包序列ID（YYYYMMDDXXX）</package_id>" +
			 * "<result>(本接口result恒为空)</result>" +
			 * "<retcode>返回码：0或00-提交成功，其他见5.11的说明。对于返回非0或00的错误码，商户必须调用查询接口确认批次状态。</retcode>"
			 * + "<retmsg>错误内容描述</retmsg>" + "</root>";
			 */

			throw new CommonException(CommonException.SYSTEM_ERROR, "连接财付通异常");
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
			ctx = SSLContext.getInstance("SSL");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream(CLIENT_KEY_STORE),
					CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream(CLIENT_TRUST_KEY_STORE),
					CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

			kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		} catch (Exception e) {
			throw new CommonException(CommonException.SYSTEM_ERROR, "验证证书失败");
		}

		return ctx;
	}

	/**
	 * 解析返回结果
	 * 
	 * @param xml
	 * @return
	 */
	private Object parseResponseXml(BusType type, String xml) {
		switch (type) {
		case BATCH_DRAW: {
			Map<String, String> xmlMap = XmlParseUtil
					.xmlToMap(new BufferedReader(new StringReader(xml)));

			CftCommOutput output = new CftCommOutput();
			output.setRetcode(xmlMap.get("retcode"));
			output.setRetmsg(xmlMap.get("retmsg"));
			return output;
		}
		case BATCH_DRAW_QUERY: {

		}
		case REFUND_QUERY: {

			return null;
		}
		default:
			break;
		}

		return null;
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
