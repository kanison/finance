/**
 * 
 */
package com.tenpay.sm.test.domain;

import java.io.Serializable;

/**
 * @author li.hongtl
 *
 */
public class IfbItem implements Serializable {
	private static final long serialVersionUID = 2631469198188916972L;
	
	private String itemNo;
	private String itemName;
	
	public IfbItem() {
	}
	
	public IfbItem(String itemNo,String itemName) {
		this.itemNo = itemNo;
		this.itemName = itemName;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the itemNo
	 */
	public String getItemNo() {
		return itemNo;
	}
	/**
	 * @param itemNo the itemNo to set
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	
	
}
