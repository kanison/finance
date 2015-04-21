package com.zhaocb.common.signature.dao.ibatis;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.zhaocb.common.signature.dao.MerchantAccessProfileDAO;
import com.zhaocb.common.signature.po.DefaultSettingPO;
import com.zhaocb.common.signature.po.MD5KeyPO;
import com.zhaocb.common.signature.po.MerchantRequestKeyPO;
import com.zhaocb.common.signature.po.TenpayResponeKeyPO;

/**
 * 商户接入配置信息数据访问类Ibatis实现
 * 
 * @author aixxia
 * 
 */
public class MerchantAccessProfileDAOIbatis extends SqlMapClientDaoSupport
		implements MerchantAccessProfileDAO {
	
	public MerchantRequestKeyPO selectMerchantMechantRequestKeyByMerIDAndMOrderID(
			String mid, int keyIndex) {
		MerchantRequestKeyPO para = new MerchantRequestKeyPO();
		para.setMerchantID(mid);
		para.setRequestKeyIndex(keyIndex);
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (MerchantRequestKeyPO) client.queryForObject(
				"selectMerchantRequestKeyByMerchantIdAndKeyIndex", para);
	}

	public String getMerchantMD5Key(String chnid){
		// 默认版本是1
		return getMerchantMD5Key(chnid,1);
	}
	
	public String getMerchantMD5Key(String chnid,int keyIndex){
		MD5KeyPO para = new MD5KeyPO();
		para.setSpId(chnid);
		para.setKeyIndex(keyIndex);
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		MD5KeyPO md5KeyPO = (MD5KeyPO)client.queryForObject(
				"querySpMD5SignKey", para);
		return md5KeyPO.getMd5Key();
	}
	
	public TenpayResponeKeyPO selectTenpayResponeKeyByerchantID(
			String merchantId) {
		TenpayResponeKeyPO para = new TenpayResponeKeyPO();
		para.setMerchantID(merchantId);
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (TenpayResponeKeyPO) client.queryForObject(
				"selectTenpayResponeKeyByMerchantIdAndKeyIndex", para);
	}

	public DefaultSettingPO selectDefaultSettingByerchantID(String merchantId) {
		SqlMapClientTemplate client = getSqlMapClientTemplate();
		return (DefaultSettingPO) client.queryForObject(
				"selectDefaultSettingByerchantID", merchantId);
	}
}
