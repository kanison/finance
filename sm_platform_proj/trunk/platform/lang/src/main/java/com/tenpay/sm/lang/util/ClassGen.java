package com.tenpay.sm.lang.util;
import java.util.*;

import javassist.*;
import javassist.compiler.CompileError;
import javassist.LoaderClassPath;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: �߱����Ե�bean���ֽ���������
 * ʹ��javassistʵ��
 * Ҫָ��������������ͺ����������б�
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author ������
 * @version 1.0
 */
public class ClassGen {
/**
 * ����
 */
  protected Class superClass = Object.class;
  /**
   * �����б�������-->�������� <String,Class>
   */
  protected Map properties = new HashMap();
  /**
   * �ֽ��뱣���·��������Ϊ�գ�ֻ���ؽ��뵱ǰ��ClassLoader��������
   */
  protected String saveClassPath;
  /**
   * ���ɵ����ȫ��������·��
   */
  protected String name;

  public ClassGen(String name) {
    this.name = name;
  }

  /**
   * ������
   * @return
   */
  public Class generate() {
//    ClassPool cp = new ClassPool();
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.makeClass(name);
    
    try {
      CtClass ctClass = null;
      try {
         ctClass = cp.get(this.superClass.getName());
    	  //ctClass = cp.get("java.util.ArrayList<java.lang.String>");
    	  //ctClass = cp.get("java.util.ArrayList");
      }
      catch (NotFoundException ex1) {
        cp.appendClassPath(new LoaderClassPath(this.superClass.getClassLoader()));
        ex1.printStackTrace();
        ctClass = cp.get(this.superClass.getName());
      }
      cc.setSuperclass(ctClass);

      Iterator iter = this.properties.keySet().iterator();
      while (iter.hasNext()) {
        String property = (String) iter.next();
        this.validateProperty(property);

        Class type = (Class)this.properties.get(property);
        CtField cf = new CtField(cp.get(type.getName()), property, cc);
        cf.setModifiers(Modifier.PROTECTED);
        cc.addField(cf);

        String srcGet = this.propertyGetSrc(property,type);
        CtMethod cmGet = CtNewMethod.make(srcGet, cc);
        cc.addMethod(cmGet);

        String srcSet = this.propertySetSrc(property,type);
        CtMethod cmSet = CtNewMethod.make(srcSet, cc);
        cc.addMethod(cmSet);
      }
      try {
        if (this.saveClassPath != null) {
          cc.writeFile(this.saveClassPath);
        }
        else {
          cc.writeFile();
        }
      }
      catch (IOException ex) {
        //log.error("����ֽ������" + ex.getMessage(),ex);
    	  throw ex;
      }
      return cc.toClass();
    }
    catch (RuntimeException ex) {
      //log.error("��̬���������" + ex.getMessage(),ex);
      throw ex;
    }
    catch(Exception ex) {
      //log.error("��̬���������" + ex.getMessage(),ex);
      throw new RuntimeException("��̬���������" + ex.getMessage(),ex);
    }
  }

  protected void validateProperty(String property){
    if(property.equals("")) {
      throw new RuntimeException("����������Ϊ�գ�");
    }
    if(property.length()==1 && property.toUpperCase().equals(property)) {
      throw new RuntimeException("����������Ϊ������д��ĸ! " + property);
    }
    if(property.length()>1 &&
       property.substring(0,1).toUpperCase().equals(property.substring(0,1)) &&
       !property.substring(1,2).toUpperCase().equals(property.substring(1,2))) {
      throw new RuntimeException("��������������ĸ��д���ڶ�����ĸСд! " + property);
    }
  }

  protected String propertyGetSrc(String property,Class type) {
    StringBuffer sb = new StringBuffer();
    sb.append("public ");
    sb.append(type.getName());
    sb.append(" ");
    sb.append(this.methodGetName(property,type));
    sb.append("() { return this.");
    sb.append(property);
    sb.append(";}");
    return sb.toString();
  }

  protected String propertySetSrc(String property,Class type) {
    StringBuffer sb = new StringBuffer();
    sb.append("public void ");
    sb.append(this.methodSetName(property));
    sb.append("(");
    sb.append(type.getName());
    sb.append(" value) { this.");
    sb.append(property);
    sb.append("=value;");
    sb.append("}");
    return sb.toString();
  }

  protected String methodGetName(String property,Class type) {
    if (type.equals(boolean.class)) {
      return "is" + property.substring(0, 1).toUpperCase() +
          property.substring(1);
    }
    else {
      return "get" + property.substring(0, 1).toUpperCase() +
          property.substring(1);
    }
  }
  protected String methodSetName(String property) {
    return "set" + property.substring(0,1).toUpperCase() + property.substring(1);
  }

  /**
   * ��������
   * @param property
   * @param clazz
   */
  public void addProperty(String property,Class clazz) {
    this.properties.put(property,clazz);
  }
  /**
   * ��ȡ���Ե�����
   * @param property
   * @return
   */
  public Class getPropertyType(String property) {
    return (Class)this.properties.get(property);
  }

  public Map getProperties() {
    return properties;
  }
  public Class getSuperClass() {
    return superClass;
  }
  public void setProperties(Map properties) {
    this.properties = properties;
  }
  public void setSuperClass(Class superClass) {
    this.superClass = superClass;
  }
  public String getSaveClassPath() {
    return saveClassPath;
  }
  public void setSaveClassPath(String saveClassPath) {
    this.saveClassPath = saveClassPath;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
