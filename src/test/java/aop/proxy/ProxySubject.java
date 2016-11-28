package aop.proxy;



//代理角色：
public class ProxySubject extends Subject{
	 // 以真实角色作为代理角色的属性
	  private Subject realSubject;

	  public ProxySubject(Subject realSubject) {
		  this.realSubject = realSubject;
	  }

	  // 该方法封装了真实对象的request方法
	  public void doSomething() {
		 before();
	     realSubject.doSomething();;  // 此处执行真实对象的request方法
	     after();
	  }

	  private void before(){
		  System.out.println("proxy before");
	  }
	  
	  private void after(){
		  System.out.println("proxy after");
	  }
	  public static void main(String[] args) {
		RealSubject real = new RealSubject();
		Subject sub = new  ProxySubject(real);
		sub.doSomething();
	  }
}
