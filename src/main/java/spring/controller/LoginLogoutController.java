package spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *@author baozhichao
 *2013-12-17 ����4:27:38
 */
@Controller
public class LoginLogoutController {

	@RequestMapping(method=RequestMethod.GET,value="/login.do")
	public void home(){
		
	}
}

