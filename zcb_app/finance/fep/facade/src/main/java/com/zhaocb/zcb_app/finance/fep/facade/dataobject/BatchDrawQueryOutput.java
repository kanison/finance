package com.zhaocb.zcb_app.finance.fep.facade.dataobject;

import java.io.Serializable;
import java.util.Set;

/**
 * �Ƹ�ͨ�����ѯ�������
 * 
 * @author zhl
 *
 */
public class BatchDrawQueryOutput extends BatchDrawOutput implements
		Serializable {

	private static final long serialVersionUID = -2872892447164410436L;

	private String trade_state;// ����״̬
	private Integer total_count;// �ܱ���
	private Integer total_fee;// �ܽ��
	private Integer succ_count;// �ɹ�����
	private Integer succ_fee;// �ɹ����
	private Integer fail_count;// ʧ�ܱ���
	private Integer fail_fee;// ʧ�ܽ��

	private Set<BatchDrawQueryUsersDO> origin_set;// ��ʼ״̬������Ϣ
	private Set<BatchDrawQueryUsersDO> success_set;// �ɹ�������Ϣ
	private Set<BatchDrawQueryUsersDO> tobank_set;// ���ύ���и�����Ϣ
	private Set<BatchDrawQueryUsersDO> fail_set;// ʧ�ܸ�����Ϣ
	private Set<BatchDrawQueryUsersDO> handling_set;// �����и�����Ϣ
	private Set<BatchDrawQueryUsersDO> return_ticket_set;// ��Ʊ������Ϣ

	public String getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public Integer getSucc_count() {
		return succ_count;
	}

	public void setSucc_count(Integer succ_count) {
		this.succ_count = succ_count;
	}

	public Integer getSucc_fee() {
		return succ_fee;
	}

	public void setSucc_fee(Integer succ_fee) {
		this.succ_fee = succ_fee;
	}

	public Integer getFail_count() {
		return fail_count;
	}

	public void setFail_count(Integer fail_count) {
		this.fail_count = fail_count;
	}

	public Integer getFail_fee() {
		return fail_fee;
	}

	public void setFail_fee(Integer fail_fee) {
		this.fail_fee = fail_fee;
	}

	public Set<BatchDrawQueryUsersDO> getOrigin_set() {
		return origin_set;
	}

	public void setOrigin_set(Set<BatchDrawQueryUsersDO> origin_set) {
		this.origin_set = origin_set;
	}

	public Set<BatchDrawQueryUsersDO> getSuccess_set() {
		return success_set;
	}

	public void setSuccess_set(Set<BatchDrawQueryUsersDO> success_set) {
		this.success_set = success_set;
	}

	public Set<BatchDrawQueryUsersDO> getTobank_set() {
		return tobank_set;
	}

	public void setTobank_set(Set<BatchDrawQueryUsersDO> tobank_set) {
		this.tobank_set = tobank_set;
	}

	public Set<BatchDrawQueryUsersDO> getFail_set() {
		return fail_set;
	}

	public void setFail_set(Set<BatchDrawQueryUsersDO> fail_set) {
		this.fail_set = fail_set;
	}

	public Set<BatchDrawQueryUsersDO> getHandling_set() {
		return handling_set;
	}

	public void setHandling_set(Set<BatchDrawQueryUsersDO> handling_set) {
		this.handling_set = handling_set;
	}

	public Set<BatchDrawQueryUsersDO> getReturn_ticket_set() {
		return return_ticket_set;
	}

	public void setReturn_ticket_set(
			Set<BatchDrawQueryUsersDO> return_ticket_set) {
		this.return_ticket_set = return_ticket_set;
	}

}
