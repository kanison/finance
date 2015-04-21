package com.zhaocb.common.signature.imp;

import java.security.Key;

public interface SignKeyManager {
	public Key getPublicKey(String chnid, String signType, int keyIndex);

	public Key getPrivateKey(String chnid, String signType, int keyIndex);

	public void flushKey();
}
