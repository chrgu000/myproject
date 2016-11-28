package spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *@author baozhichao
 *2013-12-20 ����5:18:48
 */
public class AuthenticationFaildPage implements AuthenticationFailureHandler,AuthenticationSuccessHandler{

	private String faildPage;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		response.sendRedirect("/spring/portal/login");
	}

	public String getFaildPage() {
		return faildPage;
	}

	public void setFaildPage(String faildPage) {
		this.faildPage = faildPage;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0,
			HttpServletResponse response, Authentication arg2) throws IOException,
			ServletException {
		
		response.sendRedirect("/spring/home/index");
	}

}

