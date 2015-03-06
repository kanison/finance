/**
 * 
 */
package com.tenpay.sm.web.schema;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * ����Spring annotation��pojo��webģ���schema����
 * ����webģ��ķ�����������������������schema������Ĳ���
 * @author li.hongtl
 *
 */
public interface WebModuleSchemaGenerator {
	/**
	 * ����Spring annotation��pojo��webģ���schema����
	 * ����webģ��ķ�����������������������schema������Ĳ���
	 * @param klass
	 * @param os
	 * @throws IOException
	 */
	public void generator(Class klass,OutputStream os) throws IOException;
	/**
	 * ����Spring annotation��pojo��webģ���schema����
	 * ����webģ��ķ�����������������������schema������Ĳ���
	 * @param klass
	 * @param writer
	 * @throws IOException
	 */
	public void generator(Class klass,Writer writer) throws IOException;
}
