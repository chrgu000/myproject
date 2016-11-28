package aop.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理处理器：java动态代理 <br/>
 * Proxy 实现 AOP 功能总结：<br/>
 * <strong>&nbsp;&nbsp;目标对象必须实现接口。</strong><br/>
 * <strong>&nbsp;&nbsp;调用Proxy.newProxyInstance()方法，返回创建的代理对象。</strong><br/>
 * <strong>&nbsp;&nbsp;由于该方法需要一个实现了InvocationHandler接口的对象，所以我们还要重写该接口的invoke()方法。</strong><br/>
 * <strong>&nbsp;&nbsp;我们的限制条件就可以放在这个invoke()方法中，当满足条件，就调用method.invoke()真正的调用目标对象的方法，否则，不做任何事情，直接过滤。</strong>
 * @author baozc
 * @date 2016年10月24日 下午1:59:25
 */
public class DynamicProxy implements InvocationHandler {
	
	private Object sub;
    public DynamicProxy() {}

    /**
     * 创建RealSubject的代理类<br/><br/>
     * 在该方法内，调用Proxy.newProxyInstance()方法创建代理对象。<br/>&nbsp;&nbsp;
     * 第一个参数是目标对象的类加载器，<br/>&nbsp;&nbsp;
     * 第二个参数是目标对象实现的接口，<br/>&nbsp;&nbsp;
     * 第三个参数传入一个InvocationHandler实例，该参数和回调有关系。<br/><br/>
     * 每当<strong>通过代理类</strong>调用目标对象的方法的时候，就会回调该InvocationHandler实例的方法，也就是public Object invoke()方法，<br/>
     * 我们就可以把限制的条件放在这里，条件符合的时候，就可以调用method.invoke()方法真正的调用目标对象的方法，<br/>
     * 否则，则可以在这里过滤掉不符合条件的调用。<br/>
     * @param obj
     * @return RealSubject的代理类
     * @author 包志超
     * @date 2016年10月24日下午1:54:51
     */
    public Object createStudentProxy(Object obj) {
      sub = obj;
      return Proxy.newProxyInstance(obj.getClass().getClassLoader(),    
    		  obj.getClass().getInterfaces(), this);   
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println( " before calling "  + method);
		RealSubject s = (RealSubject)sub;    
        Object object = null;    
        if(s.getName() != null)    
            object = method.invoke(sub, args);    
        else    
            System.out.println("名字为空，代理类已经拦截！");    
        System.out.println( " after calling "  + method);
        return object;    
	}

	public static void main(String[] args) {
		RealSubject real = new RealSubject();
		real.setName("DynamicProxy");
		
		Object obj = new DynamicProxy().createStudentProxy(real);
		((RealSubject)obj).doSomething();
		System.out.println(obj.toString());
	}
}
