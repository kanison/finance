package com.tenpay.sm.lang.util;

import java.io.Serializable;
import java.io.IOException;
import java.io.*;

/**
 *
 * <p>Title: xx框架</p>
 *
 * <p>Description: 利用序列化和反序列化克隆对象的深度工具</p>
 *
 * <p>Copyright: Copyright (c) 洪桃李 2005</p>
 *
 * <p>Company: 洪桃李</p>
 *
 * @author 洪桃李
 * @version 2.0
 */
public class Cloner {
	/**
	 * 克隆
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
