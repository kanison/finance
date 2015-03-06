/**
 * 
 */
package com.tenpay.sm.test.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author torryhong
 *
 */
public class TestFileClose extends TestCase {
	public void testFileClose() throws IOException {
		java.io.FileInputStream fis = new java.io.FileInputStream("c:/1.txt");
		java.io.InputStreamReader r = new java.io.InputStreamReader(fis);
		java.io.BufferedReader br = new java.io.BufferedReader(r);
		String s = br.readLine();
		System.out.println(s);
		//br.close();
		
		int c = r.read();
		System.out.println(c);
//		int c = fis.read();
//		System.out.println(c);
	}
}
