/**
 * 
 */
package com.tenpay.sm.web.schema;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * 基于Spring annotation的pojo的web模块的schema生成
 * 生成web模块的服务描述，包含类型描述的schema和允许的操作
 * @author li.hongtl
 *
 */
public interface WebModuleSchemaGenerator {
	/**
	 * 基于Spring annotation的pojo的web模块的schema生成
	 * 生成web模块的服务描述，包含类型描述的schema和允许的操作
	 * @param klass
	 * @param os
	 * @throws IOException
	 */
	public void generator(Class klass,OutputStream os) throws IOException;
	/**
	 * 基于Spring annotation的pojo的web模块的schema生成
	 * 生成web模块的服务描述，包含类型描述的schema和允许的操作
	 * @param klass
	 * @param writer
	 * @throws IOException
	 */
	public void generator(Class klass,Writer writer) throws IOException;
}
