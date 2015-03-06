package com.app.utils;

public class CachedValueContainer {
	// 效率第一，不使用get/set封装
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
