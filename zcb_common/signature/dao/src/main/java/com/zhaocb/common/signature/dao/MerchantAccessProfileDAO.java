package com.zhaocb.common.signature.dao;

import com.zhaocb.common.signature.po.DefaultSettingPO;
import com.zhaocb.common.signature.po.MerchantRequestKeyPO;
import com.zhaocb.common.signature.po.TenpayResponeKeyPO;

/**
 * �̻�����������Ϣ���ݷ�����
 * 
 * @author aixxia
 * 
 */
public interface MerchantAccessProfileDAO {

	public MerchantRequestKeyPO selectMerchantMechantRequestKeyByMerIDAndMOrderID(String mid, int keyIndex);

	public TenpayResponeKeyPO selectTenpayResponeKeyByerchantID(String merchantId);
	
	public DefaultSettingPO selectDefaultSettingByerchantID(String merchantId);
	
	public String getMerchantMD5Key(String chnid);
}
