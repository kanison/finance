/**
 * 
 */
package com.tenpay.sm.test.jaxb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.tenpay.sm.test.domain.Customer;
import com.tenpay.sm.test.domain.PayOrder;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestMarshal extends TestCase {
	public void testMarshal() throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(PayOrder.class);
		//JAXBContext context = JAXBContext.newInstance("xx",PayOrder.class.getClassLoader());
		Marshaller marshaller = context.createMarshaller(); 
		marshaller.marshal( PayOrder.getDefaultDemoPayOrder(), System.out );
		marshaller.marshal( PayOrder.getDefaultDemoPayOrder(), new FileOutputStream(new File("PayOrder.xml")));
		System.out.println();
	}
	public void testUnMarshal() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PayOrder.class);
		Unmarshaller u = context.createUnmarshaller();
		JAXBElement<PayOrder> root = u.unmarshal(new StreamSource(new File("PayOrder.xml")),PayOrder.class);
		PayOrder foo = root.getValue();	
		System.out.println(foo);
	}
	public void testMarshalNonElement() throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(PayOrder.class);
		Marshaller marshaller = context.createMarshaller(); 
		marshaller.marshal(new JAXBElement(new QName("MyNS","MyCustomer"), Customer.class,
				PayOrder.getDefaultDemoPayOrder().getPayCustomer()), System.out );
		
		marshaller.marshal(new JAXBElement(new QName("MyNS","MyCustomer"), Customer.class,
				PayOrder.getDefaultDemoPayOrder().getPayCustomer()), new FileOutputStream(new File("Customer.xml")));
		System.out.println();
	}
}
