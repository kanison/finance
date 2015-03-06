package com.app.utils;

public class HttpClientCallResult {
	private String retString;
	private int httpStatus = 0;;
	public String getRetString() {
		return retString;
	}
	public void setRetString(String retString) {
		this.retString = retString;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
