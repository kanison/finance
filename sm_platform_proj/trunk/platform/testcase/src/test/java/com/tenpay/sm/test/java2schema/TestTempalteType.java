/**
 * 
 */
package com.tenpay.sm.test.java2schema;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tenpay.sm.test.domain.Customer;

import junit.framework.TestCase;

/**
 * @author li.hongtl
 *
 */
public class TestTempalteType extends TestCase {
	public void ntestArray() {
		Customer[] cs = new Customer[4];
		showClass(cs.getClass());
	}
	public void testList() {
		List<Customer> cs = new ArrayList<Customer>();
		showClass(cs.getClass());
	}
	public void testMap() {
		Map<String,Customer> cs = new HashMap<String,Customer>();
		showClass(cs.getClass());
	}
	public void testParameterizedType() {
		 System.out.println(List.class);
		   Type type = StringList.class.getGenericSuperclass();
		    System.out.println(type); // java.util.ArrayList<java.lang.String>
		    ParameterizedType pt = (ParameterizedType) type;
		    System.out.println(pt.getActualTypeArguments()[0]);
	}
	
	private void showClass(Class klass) {
//		System.out.println(klass);
//		System.out.println(klass.getName());
//		System.out.println(klass.getCanonicalName());
//		System.out.println(klass.getComponentType());
		System.out.println(klass.getGenericSuperclass());
		Type[] gis = klass.getGenericInterfaces();
		for(Type gi : gis) {
			System.out.println("Type: " + gi);
		}
		System.out.println(klass.getTypeParameters().length);
		for(TypeVariable<Class> tv : klass.getTypeParameters()) {
			//System.out.println(tv.getGenericDeclaration());
			System.out.println(tv.getName());
			System.out.println(tv.getBounds().length);
			for(Type type : tv.getBounds()) {
				if(type instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType)type;
					System.out.println("Bounds.getOwnerType" + pt.getOwnerType());
					System.out.println("Bounds.getRawType" + pt.getRawType());
					for(Type t : pt.getActualTypeArguments()) {
						System.out.println("Bounds.getActualTypeArguments: " + t);
					}
				}
				System.out.println("Bounds.toString(): " + type);
				System.out.println("Bounds.getClass(): " + type.getClass());
			}
		}
		System.out.println("-------------");
	}
	
	class StringList extends ArrayList<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4948734734334688297L;
	}
}
