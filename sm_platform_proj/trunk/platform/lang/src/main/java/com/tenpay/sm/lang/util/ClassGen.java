package com.tenpay.sm.lang.util;
import java.util.*;

import javassist.*;
import javassist.compiler.CompileError;
import javassist.LoaderClassPath;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: 具备属性的bean的字节码生成器
 * 使用javassist实现
 * 要指定父类和属性类型和属性名的列表
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author 洪桃李
 * @version 1.0
 */
public class ClassGen {
/**
 * 父类
 */
  protected Class superClass = Object.class;
  /**
   * 属性列表，属性名-->属性类型 <String,Class>
   */
  protected Map properties = new HashMap();
  /**
   * 字节码保存的路径，可以为空，只加载进入当前的ClassLoader，不保存
   */
  protected String saveClassPath;
  /**
   * 生成的类的全名，包含路径
   */
  protected String name;

  public ClassGen(String name) {
    this.name = name;
  }

  /**
   * 类生成
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
        //log.error("输出字节码出错！" + ex.getMessage(),ex);
    	  throw ex;
      }
      return cc.toClass();
    }
    catch (RuntimeException ex) {
      //log.error("动态生成类出错！" + ex.getMessage(),ex);
      throw ex;
    }
    catch(Exception ex) {
      //log.error("动态生成类出错！" + ex.getMessage(),ex);
      throw new RuntimeException("动态生成类出错！" + ex.getMessage(),ex);
    }
  }

  protected void validateProperty(String property){
    if(property.equals("")) {
      throw new RuntimeException("属性名不能为空！");
    }
    if(property.length()==1 && property.toUpperCase().equals(property)) {
      throw new RuntimeException("属性名不能为单个大写字母! " + property);
    }
    if(property.length()>1 &&
       property.substring(0,1).toUpperCase().equals(property.substring(0,1)) &&
       !property.substring(1,2).toUpperCase().equals(property.substring(1,2))) {
      throw new RuntimeException("属性名不能首字母大写，第二个字母小写! " + property);
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
   * 增加属性
   * @param property
   * @param clazz
   */
  public void addProperty(String property,Class clazz) {
    this.properties.put(property,clazz);
  }
  /**
   * 获取属性的类型
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
