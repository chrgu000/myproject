package spring.controller.base;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

/**
 *@author baozhichao
 *2014-1-13 обнГ2:04:10
 */
public abstract class BaseController implements ServletContextAware{

	ServletContext servletContext;
	
	@Override
	public void setServletContext(ServletContext sc) {
		servletContext=sc;
	}


	
}

