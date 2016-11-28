package spring.annotation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import spring.po.User;

/**
 * compare（a,b）方法:根据第一个搜索参数小于、等于或大于第二个参数分别返回负整数、零或正整数
 *@author baozhichao
 *2013-12-6 下午5:35:40
 */
public class FieldSortCom implements Comparator<SortableField>{

	/**
	 * 参数1和参数2比，返回顺序从小到大
	 * 参数2和参数1比，返回顺序从大到小
	 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
	 */
	@Override
	public int compare(SortableField s1, SortableField s2) {
//		System.out.println(s1.getMeta().order()+"--"+s2.getMeta().order());
		return s2.getMeta().order()-s1.getMeta().order();
//		return s1.getName().compareTo(s2.getName());
	}

	public static void main(String[] args) {
//		BasicHolder<Integer> b = new BasicHolder<Integer>();
//		b.set(1);
//		b.f();
		
//		BasicHolder<User> b1 = new BasicHolder<User>();
//		b1.f();
//		System.out.println(b1.get());
//		User u = new User();
//		u.setId(1l);
//		b1.set(u);
//		b1.f();
//		System.out.println(b1.get().getId());
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		System.out.println(list);
	}
}

class Test implements Comparable<SortableField>{

	@Override
	public int compareTo(SortableField o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

class BasicHolder<T>{
	T element;
	void set(T arg){ element = arg; }
	T get(){ return element; }
	void f(){ System.out.println(element.getClass().getSimpleName()); }
	

}

