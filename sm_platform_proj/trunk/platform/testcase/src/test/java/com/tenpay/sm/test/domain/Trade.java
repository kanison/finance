/**
 * 
 */
package com.tenpay.sm.test.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author li.hongtl
 *
 */
public class Trade implements Serializable {
	private static final long serialVersionUID = 7096267454591179946L;
	private String tradeno;
	private String goodsTitle;
	private BigDecimal tradefee;
	private BigDecimal chargefee;
	private String whoPayCharge;
	
	/**
	 * @return the chargefee
	 */
	public BigDecimal getChargefee() {
		return chargefee;
	}
	/**
	 * @param chargefee the chargefee to set
	 */
	public void setChargefee(BigDecimal chargefee) {
		this.chargefee = chargefee;
	}
	/**
	 * @return the goodsTitle
	 */
	public String getGoodsTitle() {
		return goodsTitle;
	}
	/**
	 * @param goodsTitle the goodsTitle to set
	 */
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	/**
	 * @return the tradefee
	 */
	public BigDecimal getTradefee() {
		return tradefee;
	}
	/**
	 * @param tradefee the tradefee to set
	 */
	public void setTradefee(BigDecimal tradefee) {
		this.tradefee = tradefee;
	}
	/**
	 * @return the tradeno
	 */
	public String getTradeno() {
		return tradeno;
	}
	/**
	 * @param tradeno the tradeno to set
	 */
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	/**
	 * @return the whoPayCharge
	 */
	public String getWhoPayCharge() {
		return whoPayCharge;
	}
	/**
	 * @param whoPayCharge the whoPayCharge to set
	 */
	public void setWhoPayCharge(String whoPayCharge) {
		this.whoPayCharge = whoPayCharge;
	}
	
}
