package com.zhaocb.common.authentication.facade;

public interface AuthDispatcherFacade {
	public final static int API_MASK=1;
	public final static int MCH_MASK=2;
	public final static int TENPAY_MASK=4;
	public final static int TENPAY_WEAK_MASK=8;
	public final static int LOCAL_BY_TENPAY_MASK=16;
	public final static int LOCAL_BY_MCH_MASK=32;
	public final static int WEIXIN_MASK=64;
	
	public int commonAuth(int authChannel, int needCert, Object[] args);
	
	public void signRetObj(Object obj);
}
