package com.app.utils;

public class CachedValueContainer {
	// Ч�ʵ�һ����ʹ��get/set��װ
	public long expireTime;
	public boolean preFetching;
	public Object cachedValue;

	public boolean isValid() {
		if (expireTime <= 0 || expireTime < System.currentTimeMillis()) {
			return false;
		} else {
			return true;
		}
	}
}
