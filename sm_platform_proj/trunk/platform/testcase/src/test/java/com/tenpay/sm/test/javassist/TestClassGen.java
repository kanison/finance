/**
 * 
 */
package com.tenpay.sm.test.javassist;

import javassist.compiler.CompileError;

import com.tenpay.sm.lang.util.ClassGen;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestClassGen extends TestCase {
	  public void testClassGen() throws CompileError, InstantiationException, IllegalAccessException {
//		  ClassPool cp = ClassPool.getDefault();
//		    CtClass cc = cp.makeClass("Tom");
//		    Javac jc = new Javac(cc);
//		    CtMember cm = jc.compile("class StringList extends ArrayList<String> {} ");
		  //cm.
		  
	    System.out.println(System.getProperty("user.home"));
	    ClassGen gen = new ClassGen("Customer");
	    gen.addProperty("abc",int.class);
	    gen.addProperty("def",boolean.class);
	    gen.addProperty("k",String.class);
	    gen.setSaveClassPath("E:\\classes");
	    
	    Class clazz = gen.generate();
	    Object obj = clazz.newInstance();
	    System.out.println(obj instanceof java.util.List);
	  }
}
