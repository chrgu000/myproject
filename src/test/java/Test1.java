import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import spring.po.testEneity;

/**
 *@author baozhichao
 *2013-12-16 ����2:06:50
 */
public class Test1 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
//		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-core.xml");
//		Object obj = ac.getBean("testEntity");
//		if(obj!=null){
//			String name = ((testEneity)obj).getName();
//			System.out.println(name);
//		}else{
//			System.out.println("obj is null");
//		}
		System.out.println(1);
		try {
			Class<?> c = Class.forName("spring.po.testEneity");
			System.out.println("*********begin*********");
			Method[] method =c.getDeclaredMethods();
			for(Method m:method){
				System.out.println(m.toString());
			}
			
			Field[] field =c.getDeclaredFields();
			for(Field f:field){
				System.out.println(f.toString());
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}

