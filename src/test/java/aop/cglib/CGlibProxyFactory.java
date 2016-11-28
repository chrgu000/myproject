package aop.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGlibProxyFactory implements MethodInterceptor{
	
	private Object object;  
	/**
	 * 我们使用一个Enhancer类来创建代理对象，不再使用Proxy。<br/>
	 * 使用Enhancer类，需要为其实例指定一个父类，也就是我们 的目标对象。<br/>
	 * 这样，我们新创建出来的对象就是目标对象的子类，有目标对象的一样。<br/>
	 * 除此之外，还要指定一个回调函数，这个函数就和Proxy的 invoke()类似。<br/><br/>
	 * 
	 * 总体来说，使用CGlib的方法和使用Proxy的方法差不多，只是Proxy创建出来的代理对象和目标对象都实现了同一个接口。<br/>
	 * 而CGlib的方法则是直接继承了目标对象。<br/>
	 * @param object
	 * @return
	 * @author 包志超
	 * @date 2016年10月24日下午3:16:16
	 */
    public Object createStudent(Object object){    
        this.object = object;    
        Enhancer enhancer = new Enhancer();    
        enhancer.setSuperclass(object.getClass());    
        enhancer.setCallback(this);    
        return enhancer.create();    
    }    
    public Object getObject() {    
        return object;    
    }    
    public void setObject(Object object) {    
        this.object = object;    
    }    
    @Override    
    public Object intercept(Object proxy, Method method, Object[] args,    
            MethodProxy methodProxy) throws Throwable {    
        Subject sub = (Subject)object;    
        Object result = null;    
        if(sub.getName() != null)    
            result = methodProxy.invoke(object, args);    
        else    
            System.out.println("方法已经被拦截...");    
        return result;    
    }   
    
    public static void main(String[] args) {    
    	Subject stu1 = (Subject)(new CGlibProxyFactory().createStudent(new Subject()));  
    	Subject sub = new Subject();
    	sub.setName("CGlibProxy");
    	Subject stu2 = (Subject)(new CGlibProxyFactory().createStudent(sub));    
        stu1.doSomething();    
        stu2.doSomething();    
    }    
}
