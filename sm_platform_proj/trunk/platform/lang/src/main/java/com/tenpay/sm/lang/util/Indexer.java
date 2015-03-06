package com.tenpay.sm.lang.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 获取数组或List的指定下标的元素的方法
 * 主要用在模板中，对应一个宏或一个el function等
 * @author li.hongtl
 *
 */
public class Indexer {
	
	/**
	 * 获取数组或List的指定下标的元素的方法
	 * @param obj
	 * @param index
	 * @return
	 */
	public static Object get(Object obj,int index) {
		if(obj==null) {
			return null;
		}
		if(obj instanceof List) {
			return ((List)obj).get(index);
		}
		if(obj.getClass().isArray()) {
			return Array.get(obj,index);
		}
		return null;
	}
	
	
}
