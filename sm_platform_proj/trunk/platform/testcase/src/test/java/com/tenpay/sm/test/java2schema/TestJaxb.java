/**
 * 
 */
package com.tenpay.sm.test.java2schema;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.bind.JAXBContext;

import junit.framework.TestCase;

import com.tenpay.sm.test.domain.Customer;
import com.tenpay.sm.test.domain.PayOrder;

/**
 * @author li.hongtl
 *
 */
public class TestJaxb extends TestCase {
	public void testJava2Schema() throws JAXBException, IOException {
		//final File baseDir = new File(".");



		JAXBContext context = JAXBContext.newInstance(Customer.class, PayOrder.class);
		context.generateSchema(new MySchemaOutputResolver());
	}
	class MySchemaOutputResolver extends SchemaOutputResolver {
		
	    public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
	    	StreamResult sr =  new StreamResult(
	        		System.out
	        		//new File(baseDir,suggestedFileName)
	        		);
	    	sr.setSystemId("HELLO");
	    	return sr;
	    }
	}
}
