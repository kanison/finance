/**
 * 
 */
package com.tenpay.sm.test.wc;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

//import com.tenpay.sm.web.context.servletwrapper.ServletOutputStreamWrapper;

/**
 * @author li.hongtl
 *
 */
public class TestServletOutputStreamWrapper extends TestCase {
//	public void testServletOutputStreamWrapper() throws IOException {
//		ServletOutputStreamWrapper os = new ServletOutputStreamWrapper();
//		for(int i=0;i<10;i++) {
//			//os.write(new byte[] {30,40,50,62,30,40,122,30});
//			os.print("hello12345");
//		}
//		os.flush();
//		os.close();
//		String str = os.getContentAsString();
//		System.out.println("length:" + str.length());
//		System.out.println("content:" + str);
//	}
	
	public void testMockHttpServletResponse() throws IOException {
		MockHttpServletResponse res = new MockHttpServletResponse();
		PrintWriter pw = res.getWriter();
		for(int i=0;i<10;i++) {
			//os.write(new byte[] {30,40,50,62,30,40,122,30});
			pw.print("hello12345");
		}
		pw.flush();
		pw.close();
		String str = res.getContentAsString();
		System.out.println("length:" + str.length());
		System.out.println("content:" + str);
	}
}
