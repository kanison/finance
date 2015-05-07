package com.zhaocb.zcb_app.finance.fep.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * ֤����֤
 * @author zhl
 *
 */
public class SSLUtil {

	private static final String JKS_CA_FILENAME = "tenpay_cacert.jks";
	private static final String JKS_CA_ALIAS = "tenpay";
	private static final String JKS_CA_PASSWORD = "";
	private static final String SunX509 = "SunX509";
	private static final String IbmX509 = "IbmX509";
	private static final String JKS = "JKS";
	private static final String PKCS12 = "PKCS12";
	private static final String TLS = "TLS";

	/**
	 * ��ȡSSLContext
	 * 
	 * @param trustFile
	 * @param trustPasswd
	 * @param keyFile
	 * @param keyPasswd
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	public static SSLContext getSSLContext(String caFileName,
			String certFileName, String keyPasswd) throws CertificateException,
			IOException, KeyStoreException, NoSuchAlgorithmException,
			UnrecoverableKeyException, KeyManagementException {

		File caFile = new File(caFileName);
		String caPath = caFile.getParent();

		File jksCAFile = new File(caPath + "/" + JKS_CA_FILENAME);
		if (!jksCAFile.isFile()) {
			X509Certificate cert = (X509Certificate) getCertificate(caFile);
			FileOutputStream out = new FileOutputStream(jksCAFile);

			// store jks file
			storeCACert(cert, JKS_CA_ALIAS, JKS_CA_PASSWORD, out);
			out.close();
		}

		FileInputStream trustStream = new FileInputStream(jksCAFile);
		FileInputStream keyStream = new FileInputStream(new File(certFileName));

		String X509 = SSLUtil.getVendorX509();

		// ca
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
		KeyStore trustKeyStore = KeyStore.getInstance(JKS);
		trustKeyStore.load(trustStream, SSLUtil.str2CharArray(JKS_CA_PASSWORD));
		tmf.init(trustKeyStore);

		final char[] kp = SSLUtil.str2CharArray(keyPasswd);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(X509);
		KeyStore ks = KeyStore.getInstance(PKCS12);
		ks.load(keyStream, kp);
		kmf.init(ks, kp);

		SecureRandom rand = new SecureRandom();
		SSLContext ctx = SSLContext.getInstance(TLS);
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

		// �ر���
		keyStream.close();
		trustStream.close();

		return ctx;
	}

	/**
	 * ��ȡCA֤����Ϣ
	 * 
	 * @param cafile
	 *            CA֤���ļ�
	 * @return Certificate
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static Certificate getCertificate(File cafile)
			throws CertificateException, IOException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(cafile);
		Certificate cert = cf.generateCertificate(in);
		in.close();
		return cert;
	}

	/**
	 * �ַ���ת����char����
	 * 
	 * @param str
	 * @return char[]
	 */
	public static char[] str2CharArray(String str) {
		if (null == str)
			return null;

		return str.toCharArray();
	}

	/**
	 * �洢ca֤���JKS��ʽ
	 * 
	 * @param cert
	 * @param alias
	 * @param password
	 * @param out
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static void storeCACert(Certificate cert, String alias,
			String password, OutputStream out) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("JKS");

		ks.load(null, null);

		ks.setCertificateEntry(alias, cert);

		// store keystore
		ks.store(out, SSLUtil.str2CharArray(password));
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return String
	 */
	public static String getVmVendor() {
		return System.getProperty("java.vm.vendor");
	}

	/**
	 * �Ƿ�˳���
	 * 
	 * @param vendorAllName
	 *            ����ȫ��
	 * @param vendorShortName
	 *            ���̼�д
	 * @return boolean
	 */
	public static boolean isVendor(String vendorAllName, String vendorShortName) {
		// ת����Сд
		vendorAllName = vendorAllName.toLowerCase();
		return -1 != vendorAllName.indexOf(vendorShortName);
	}

	/**
	 * ��ȡ����X509
	 * 
	 * @return <br/>
	 *         sun��jdk����SunX509,�����ķ���IbmX509<br/>
	 */
	public static String getVendorX509() {
		String X509 = SSLUtil.SunX509;
		if (!SSLUtil.isVendor(SSLUtil.getVmVendor(), "sun")
				&& !SSLUtil.isVendor(getVmVendor(), "oracle")) {
			X509 = SSLUtil.IbmX509;
		}

		return X509;
	}

}
