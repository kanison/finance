package com.zhaocb.common.signature.facade;

public interface SignatureServiceFacade {
	/**
	 * ǩ��
	 */
	public SignResult sign(SignParams signParams);

	/**
	 * ��ǩ
	 */
	public boolean verifySignature(VerifySignatureParams verifySignatureParams);

	/**
	 * ����������Key
	 */
	public void flushKey();
	
	/**
	 * ��ȡ�̻�Ĭ��ǩ������
	 */
	public SignResult getDefaultParams(String chnid);
}
