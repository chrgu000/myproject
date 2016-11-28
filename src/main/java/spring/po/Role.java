package spring.po;

import org.springframework.security.core.GrantedAuthority;

/**
 * GrantedAuthority 授权
 *@author baozhichao
 *2013-12-19 下午5:40:56
 */
public class Role implements GrantedAuthority{

	private static final long serialVersionUID = 1L;

	public static String PREFIX="ROLE_";
	/**
	 * 获取权限
	 */
	@Override
	public String getAuthority() {
		return PREFIX+id;
	}
	/**
	 * 初始化用户权限
	 *@author baozhichao 2013-12-19下午5:46:36	
	 * @return
	 */
	public static Role getRoleUser(){
		Role ro = new Role();
		ro.setId("USER");
		ro.setName("普通用户");
		return ro;
	}
	
	private String id;
	
	private String name;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}

