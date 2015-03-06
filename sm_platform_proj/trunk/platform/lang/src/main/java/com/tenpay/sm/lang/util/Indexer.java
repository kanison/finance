package com.tenpay.sm.lang.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * ��ȡ�����List��ָ���±��Ԫ�صķ���
 * ��Ҫ����ģ���У���Ӧһ�����һ��el function��
 * @author li.hongtl
 *
 */
public class Indexer {
	
	/**
	 * ��ȡ�����List��ָ���±��Ԫ�صķ���
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
