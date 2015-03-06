package com.tenpay.sm.test.extension;
import java.io.IOException;

import com.tenpay.sm.lang.xo.XMLSerializer;
import com.tenpay.sm.test.base.ContextSupportTestCase;
import com.tenpay.sm.test.domain.PayOrder;

/**
 * 
 */

/**
 * @author li.hongtl
 *
 */
public class TestValueAndMethodExtension extends ContextSupportTestCase {
	public void testExtensionPayOrder() throws IOException, ClassNotFoundException {
		Class.forName("[Lcom.tenpay.sm.test.domain.PurchaseItem;");
		PayOrder payOrder = (PayOrder)this.getApplicationContext().getBean("payOrder");
		XMLSerializer.serialize(payOrder, System.out, "payOrder", true,"UTF-8");
		
		for(String name : this.getApplicationContext().getBeanDefinitionNames()) {
			System.out.print(name + ",");
		}
		System.out.println();
		System.out.println(this.getApplicationContext().getBeanDefinitionCount());
	}
}
