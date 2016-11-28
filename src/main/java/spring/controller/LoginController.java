package spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *@author baozhichao
 *2013-12-19 ����3:09:17
 */
@Controller
@RequestMapping("/portal")
public class LoginController {

	@RequestMapping(method=RequestMethod.GET,value="/login")
	public String login(){
//		int a = 1/0;
		return "login/login";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public String index(){
		return "1";
	}
	
	@RequestMapping("/photo")
	public void photo(HttpServletResponse response) throws IOException{
		File f = new File("C:\\Users\\Administrator\\Desktop\\a.jpg");
		
		OutputStream out = response.getOutputStream();
		
		InputStream in = new FileInputStream(f);
		byte[] by = new byte[1024];
		while(in.available()>0){
			in.read(by);
			out.write(by);
		}
		
		in.close();
		out.close();
		out.flush();
	}
}

