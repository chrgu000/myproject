package spring.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 *@author baozhichao
 *2014-1-15 ����4:52:15
 */
@Controller
@RequestMapping("/home/upload")
public class UploadController {

	@RequestMapping(value="/submit",method=RequestMethod.POST)
	public String submit(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/x-json");
		
		String file = request.getParameter("path");
		
//		File f1 = new File("d:/updload/test");
		File f = new File("d:/updload/test"+java.io.File.separator+file);
//		f.mkdirs();
		
		OutputStream output = new FileOutputStream(f);
		
		InputStream input = request.getInputStream();
		
		byte[] by = new byte[1024];
		while(input.available()>0){
			input.read(by);
			output.write(by);
		}
		
		FileCopyUtils.copy(input, output);
		
		input.close();
		
		output.close();
		output.flush();
		
		
		return "redirect:/home/index";
	}
	
	@RequestMapping("/test")
	public String test(){
		return "home/index";
	}
}

