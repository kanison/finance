package com.tenpay.sm.lang.util;

import java.io.Serializable;
import java.io.IOException;
import java.io.*;

/**
 *
 * <p>Title: xx���</p>
 *
 * <p>Description: �������л��ͷ����л���¡�������ȹ���</p>
 *
 * <p>Copyright: Copyright (c) ������ 2005</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 2.0
 */
public class Cloner {
	/**
	 * ��¡
	 * @param object
	 * @return
	 */
    public static Serializable clone(Serializable object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            ois.close();
            return (Serializable) obj;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }
}
