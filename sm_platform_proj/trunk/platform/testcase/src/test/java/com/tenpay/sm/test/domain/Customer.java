/**
 * 
 */
package com.tenpay.sm.test.domain;

import java.io.Serializable;

import com.tenpay.sm.lang.json.JSONException;
import com.tenpay.sm.lang.json.JSONObject;
import com.tenpay.sm.lang.json.JSONString;

/**
 * @author li.hongtl
 *
 */
public class Customer implements Serializable , JSONString {
	private static final long serialVersionUID = -7709981284697956816L;
	
	private PurchaseItem lastPurchaseItem;
	private String firstName;
	private String lastName;
	private String gender;
	private int age = 28;
	
	public Customer(){
	}
	
	public Customer(String firstName,String lastName,String gender){
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * ×ª»»³ÉJSONObject
	 * @return
	 * 
	 */
	public String toJSONString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("firstName", firstName);
			jo.put("lastName", lastName);
			jo.put("gender", gender);
			jo.put("name", firstName + " " + lastName);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return jo.toString();
	}

	public PurchaseItem getLastPurchaseItem() {
		return lastPurchaseItem;
	}

	public void setLastPurchaseItem(PurchaseItem lastPurchaseItem) {
		this.lastPurchaseItem = lastPurchaseItem;
	}
	
	
}
