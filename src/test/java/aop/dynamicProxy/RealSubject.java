package aop.dynamicProxy;


public class RealSubject implements Subject{

	private String name;
	@Override
	public void doSomething() {
		System.out.println( " From real subject. (name is " +name+")" );
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		return "This is RealSubject! name is "+name;
	}
}


