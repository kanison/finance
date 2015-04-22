/**
 * 
 */
package com.zhaocb.common.aop.aspect.checklogin.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.zhaocb.common.aop.aspect.checklogin.LoginSessionData;

/**
 * @author eniacli
 * 
 */
public class SessionClientUtil {
	private static Logger logger = Logger.getLogger(SessionClientUtil.class);
	private static int SESSION_VET_1 = 0x01;
	private static int SESSION_CMD_GET = 0x02;
	private static int MAX_LENGTH_OF_SEND_BUFFER = 1536;
	private static int SESSION_KEY_LEN_LIMIT = 24;
	private static byte[] dumyBytes = new byte[64];

	/**
	 * 组装给消息服务的数据包
	 * 
	 * @param sessionName
	 *            , sessionKey
	 * @return
	 * @throws IOException
	 */
	// {
	// char m_cVersion; //协议的版本号
	// char m_sApp[10];
	// char m_sOperator[10];
	// DWORD m_dwCmd; //请求的服务类型
	// DWORD m_dwCmdLen; //请求命令的有效长度
	// char m_sCommand[1524]; //请求命令
	// }RequirePackage;
	public static byte[] packDataBytes(String sessionName, String sessionKey)
			throws IOException {

		if (sessionName == null || sessionKey == null) {
			logger.warn("packDataBytes params error: " + sessionName
					+ sessionKey);
			return null;
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream(
				MAX_LENGTH_OF_SEND_BUFFER);
		DataOutput dataWriter = new DataOutputStream(bout);
		dataWriter.writeByte(SESSION_VET_1);
		writeString(dataWriter, sessionName, 10);
		writeString(dataWriter, "session lib", 10);
		dataWriter.writeInt(SESSION_CMD_GET);
		dataWriter.writeInt(SESSION_KEY_LEN_LIMIT);
		writeString(dataWriter, sessionKey, SESSION_KEY_LEN_LIMIT);
		return bout.toByteArray();
	}

	// {
	// char m_cVersion; //协议的版本号
	// char m_sApp[10];
	// char m_sOperator[10];
	// DWORD m_dwCmd; //请求的服务类型
	// DWORD m_dwTag; //成功与否
	// DWORD m_dwFailType; //失败的类型
	// DWORD m_dwContentLen; //返回内容有效长度
	// char m_sContent[1524]; //返回内容
	// }ReturnPackage
	public static byte[] unpackDataBytes(byte[] receivedBytes)
			throws IOException {
		if (receivedBytes == null) {
			logger.warn("unpackDataBytes param error!");
			return null;
		}
		ByteArrayInputStream binput = new ByteArrayInputStream(receivedBytes);
		DataInput dataReader = new DataInputStream(binput);
		dataReader.skipBytes(21);
		int cmd = dataReader.readInt();
		int ret = dataReader.readInt();
		int failureType = dataReader.readInt();
		if (cmd != SESSION_CMD_GET || ret != 0) {
			logger.warn("unpackDataBytes failure: " + failureType);
			return null;
		}
		int contentLength = dataReader.readInt();
		byte[] sessionContent = new byte[contentLength];
		dataReader.readFully(sessionContent);
		return sessionContent;
	}

	// {
	// DWORD m_dwStatus;
	// char m_sReserv[16];
	// char m_sContent[MAX_SESSION_CONTENT_LEN];
	// }SessionContent;
	public static LoginSessionData parseLoginSessionData(byte[] sessionContent)
			throws IOException {
		if (sessionContent == null) {
			logger.warn("parseLoginSessionData param error!");
			return null;
		}
		ByteArrayInputStream binput = new ByteArrayInputStream(sessionContent);
		DataInput dataReader = new DataInputStream(binput);
		dataReader.skipBytes(20);
		LoginSessionData loginSessionData = new LoginSessionData();
		loginSessionData.setUinType(dataReader.readUnsignedByte()
				| dataReader.readUnsignedByte() << 8
				| dataReader.readUnsignedByte() << 16
				| dataReader.readUnsignedByte() << 24);
		loginSessionData.setUin(readString(dataReader, 68));
		loginSessionData.setUid(dataReader.readUnsignedByte()
				| dataReader.readUnsignedByte() << 8
				| dataReader.readUnsignedByte() << 16
				| dataReader.readUnsignedByte() << 24);
		loginSessionData.setLoginTime(readString(dataReader, 16));
		loginSessionData.setLoginIp(readString(dataReader, 16));
		loginSessionData.setStatus(dataReader.readUnsignedByte()
				| dataReader.readUnsignedByte() << 8
				| dataReader.readUnsignedByte() << 16
				| dataReader.readUnsignedByte() << 24);
		return loginSessionData;

	}

	private static void writeString(DataOutput dataWriter, String str,
			int length) throws IOException {
		if (str == null) {
			dataWriter.write(dumyBytes, 0, length);
		} else {
			byte[] bytes = str.getBytes("GBK");
			int bytesLen = bytes.length;
			if (bytesLen >= length) {
				dataWriter.write(bytes, 0, length);
			} else {
				dataWriter.write(bytes);
				dataWriter.write(dumyBytes, 0, length - bytesLen);
			}
		}
	}

	private static String readString(DataInput dataReader, int length)
			throws IOException {
		byte[] tmpBytes = new byte[length];
		dataReader.readFully(tmpBytes);
		return new String(tmpBytes).trim();
	}
}
