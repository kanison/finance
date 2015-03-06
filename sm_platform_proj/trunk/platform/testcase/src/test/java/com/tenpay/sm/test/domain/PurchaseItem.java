/**
 * 
 */
package com.tenpay.sm.test.domain;

import java.io.Serializable;

/**
 * @author li.hongtl
 *
 */
public class PurchaseItem extends IfbItem implements Serializable {
	private static final long serialVersionUID = -6291929618627286972L;
	
	private int quantity;

	private java.math.BigDecimal price;
	
	public PurchaseItem() {
	}
	
	public PurchaseItem(String itemNo,int quantity,String itemName,java.math.BigDecimal price) {
		super(itemNo,itemName);
		this.quantity = quantity;
		this.price = price;
	}
	
	/**
	 * @return the price
	 */
	public java.math.BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
