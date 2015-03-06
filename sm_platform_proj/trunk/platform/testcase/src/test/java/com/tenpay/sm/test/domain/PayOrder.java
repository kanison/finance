/**
 * 
 */
package com.tenpay.sm.test.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author li.hongtl
 *
 */
@XmlRootElement
public class PayOrder implements Serializable {
	private static final long serialVersionUID = 2089443212984735373L;
	
	private Customer payCustomer;
	private PurchaseItem[] items;
	private Map<String,Customer> itemReceivers;
	private List<String> contactList;
	private String[] memos = {"hello","I","am","JSON"};
	
	public static PayOrder getDefaultDemoPayOrder() {
		PayOrder po = new PayOrder();
		po.payCustomer = new Customer("tom","jerry","male");
		po.items = new PurchaseItem[3];
		po.items[0] = new PurchaseItem("xrd005",3,"iphone 3G",new BigDecimal("4392.88"));
		po.items[1] = new PurchaseItem("xrd004",4,"iphone 8G",new BigDecimal("4692.88"));
		po.items[2] = new PurchaseItem("xrd003",5,"iphone 16G",new BigDecimal("5392.88"));
		po.itemReceivers = new HashMap<String,Customer>();
		po.itemReceivers.put("xrd005", new Customer("kate","kate","female"));
		po.itemReceivers.put("xrd004", new Customer("mary","mary","female"));
		po.contactList = new ArrayList<String>();
		po.contactList.add("13838383838");
		po.contactList.add("13845454545");
		po.contactList.add("13812121212");
		po.contactList.add("13867676767");
		return po;
	}
	
	public void addItemReceiver(String key, Customer value) {
		if(this.itemReceivers==null) {
			this.itemReceivers = new HashMap<String,Customer>();
		}
		this.itemReceivers.put(key, value);
	}
	/**
	 * @return the contactList
	 */
	public List<String> getContactList() {
		return contactList;
	}
	/**
	 * @param contactList the contactList to set
	 */
	public void setContactList(List<String> contactList) {
		this.contactList = contactList;
	}
	/**
	 * @return the itemReceivers
	 */
	public Map<String, Customer> getItemReceivers() {
		return itemReceivers;
	}
	/**
	 * @param itemReceivers the itemReceivers to set
	 */
	public void setItemReceivers(Map<String, Customer> itemReceivers) {
		this.itemReceivers = itemReceivers;
	}
	/**
	 * @return the items
	 */
	public PurchaseItem[] getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(PurchaseItem[] items) {
		this.items = items;
	}
	/**
	 * @return the payCustomer
	 */
	public Customer getPayCustomer() {
		return payCustomer;
	}
	/**
	 * @param payCustomer the payCustomer to set
	 */
	public void setPayCustomer(Customer payCustomer) {
		this.payCustomer = payCustomer;
	}

	/**
	 * @return the memos
	 */
	public String[] getMemos() {
		return memos;
	}

	/**
	 * @param memos the memos to set
	 */
	public void setMemos(String[] memos) {
		this.memos = memos;
	}
	
	
	
}
