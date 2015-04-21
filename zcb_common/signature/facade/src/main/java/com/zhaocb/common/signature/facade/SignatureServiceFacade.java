package com.zhaocb.common.signature.facade;

public interface SignatureServiceFacade {
	/**
	 * 签名
	 */
	public SignResult sign(SignParams signParams);

	/**
	 * 验签
	 */
	public boolean verifySignature(VerifySignatureParams verifySignatureParams);

	/**
	 * 清除被缓存的Key
	 */
	public void flushKey();
	
	/**
	 * 获取商户默认签名参数
	 */
	public SignResult getDefaultParams(String chnid);
}
