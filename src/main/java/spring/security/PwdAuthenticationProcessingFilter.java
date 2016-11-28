package spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


/**
 *@author baozhichao
 *2013-12-19 下午3:38:45
 */
public class PwdAuthenticationProcessingFilter extends
		AbstractAuthenticationProcessingFilter {

	private String faildPage;
	/**
	 * 必须要实现无参构造
	 * spring项目名称、loginCheck访问路径
	 */
	protected PwdAuthenticationProcessingFilter() {
		super("/loginCheck");//设置访问路径
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		String id = request.getParameter("id");
		String name = request.getParameter("user_name");
		String pwd = request.getParameter("pass_word");
		
		PwdAuthenticationToken token = new PwdAuthenticationToken();
		token.setIdCode(id);
		token.setUserName(name);
		token.setPassword(pwd);
		
		Authentication auth = null;
		
		try {
			auth = this.getAuthenticationManager().authenticate(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return auth;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		AuthenticationFaildPage  faild = new AuthenticationFaildPage();
		faild.setFaildPage(faildPage);
		
		this.setAuthenticationFailureHandler(faild);
		this.setAuthenticationSuccessHandler(faild);
	}

	public String getFaildPage() {
		return faildPage;
	}

	public void setFaildPage(String faildPage) {
		this.faildPage = faildPage;
	}

	
}

