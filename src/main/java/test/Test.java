package test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *@author baozhichao
 *2014-4-23 上午11:10:19
 */
public class Test {

	public static void main(String[] args) {
		try {
			Class<?> c = Class.forName("spring.po.testEneity");
			System.out.println("*********begin*********");
			Method[] method =c.getDeclaredMethods();
			for(Method m:method){
				System.out.println(m.toString());
			}
			c.getField("");
			Field[] field =c.getDeclaredFields();
			for(Field f:field){
				System.out.println(f.toString());
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void javaTest(String bao){

	}
}

