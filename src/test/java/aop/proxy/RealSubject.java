package aop.proxy;


public class RealSubject extends Subject{

	@Override
	public void doSomething() {
		System.out.println( " From real subject. " );
	}

}


