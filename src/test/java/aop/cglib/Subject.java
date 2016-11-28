package aop.cglib;


public class Subject {

	private String name;
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


