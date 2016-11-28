package spring.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import spring.po.Role;
import spring.po.User;
import spring.security.PwdAuthenticationToken;
import spring.service.UserService;

/**
 *@author baozhichao
 *2013-12-19 下午5:09:47
 */
public class UserServiceImpl implements UserService,AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		PwdAuthenticationToken token = (PwdAuthenticationToken)auth;
		
		if(token.getUserName()!=null && token.getPassword()!=null){
			if(!token.getUserName().equals("admin")){
				token.setErrCode("1");
				return null;
			}
			
			if(!token.getPassword().equals("123456")){
				token.setErrCode("2");
				return null;
			}
			
			User user = new User();
			user.setName(token.getName());
			user.setPwd(token.getPassword());
			//认证成功
			user.setAuthenticated(true);
			
			/**写入权限*/
			Role role = Role.getRoleUser();
			System.out.println(role.getAuthority());
			Set<GrantedAuthority> accesses = new HashSet<GrantedAuthority>();
			accesses.add(role);
			
			user.setAccesses(accesses);
			
			return user;
		}
		return null;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return authentication.equals(PwdAuthenticationToken.class);
	}

}

