package com.tenpay.sm.lang.util;

import org.apache.commons.beanutils.PropertyUtils;
import java.util.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: 基于bean的属性比较的Comparator，
 * 可以忽略字母大小写
 * 支持 PropertyUtils.getNestedProperty 所支持的属性格式
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author 洪桃李
 * @version 1.0
 */
public class PropertyComparator
    implements Comparator {
  public PropertyComparator(String property) {
    this.property = property;
  }

  public PropertyComparator(String property,boolean ignoreStringCase) {
    this.property = property;
    this.ignoreStringCase = ignoreStringCase;
  }

  protected boolean ignoreStringCase = false;
  protected String property;

  public int compare(Object o1, Object o2) {
    try {
      Comparable p1 = (Comparable) PropertyUtils.getNestedProperty(o1, this.property);
      Comparable p2 = (Comparable) PropertyUtils.getNestedProperty(o2, this.property);
      if (p1 == null && p2 == null) {
        return 0;
      }
      else if (p1 == null) {
        return -1;
      }
      else if (p2 == null) {
        return 1;
      }
      else {
        if(this.ignoreStringCase && (p1 instanceof String) && (p2 instanceof String)) {
          return ((String)p1).compareToIgnoreCase((String)p2);
        }
        return p1.compareTo(p2);
      }
    }
    catch (Exception ex) {//log it
      throw new RuntimeException(ex.getMessage(),ex);
    }
  }


//  public static void main(String args[]) throws Exception {
//    hong.xxx.po.Job job = new hong.xxx.po.Job();
//    job.setMax_lvl( (short) 2);
//    Object obj = PropertyUtils.getProperty(job, "max_lvl");
//    System.out.println(obj);
//    System.out.println(obj.getClass());
//    Short s = null;
//  }
  public boolean isIgnoreStringCase() {
    return ignoreStringCase;
  }
  public String getProperty() {
    return property;
  }
}
