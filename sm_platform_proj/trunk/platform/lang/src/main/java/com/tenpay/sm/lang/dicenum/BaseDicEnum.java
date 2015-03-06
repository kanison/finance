package com.tenpay.sm.lang.dicenum;
/**
 * 
 */

import java.io.Serializable;


/**
 * @author li.hongtl
 * TODO
 * 没想好，先没做，没用
 */
@Deprecated
public class BaseDicEnum implements Serializable {
	private static final long serialVersionUID = 8542268778535415175L;
	private transient DicEnumValidateFacade dicEnumValidateFacade = new DefaultDicEnumValidateFacadeImpl();
	private String category;
	private Object value;
	private transient String message;
	
	public BaseDicEnum(String category,Object value) {
		this.category = category;
		this.value = value;
		if(dicEnumValidateFacade!=null) {
			dicEnumValidateFacade.validate(category, value);
		}
	}
	
	
	@Override
	public boolean equals(Object value) {
		if(value==null || !(value instanceof BaseDicEnum)) {
			return false;
		}
		BaseDicEnum dicEnum = (BaseDicEnum)value;
		return this.category.equals(dicEnum.category) &&
			this.value.equals(dicEnum.value);
	}
	
	@Override
	public int hashCode() {
		return this.category.hashCode() * 17 + this.value.hashCode() * 37;
	}
	
	public void setDicEnumValidateFacade(DicEnumValidateFacade dicEnumValidateFacade) {
		this.dicEnumValidateFacade = dicEnumValidateFacade;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
