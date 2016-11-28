package spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *@author baozhichao
 *2013-12-16 обнГ2:31:17
 */
@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/method")
	public String test(Model model){
		model.addAttribute("test", "Hello World!");
		return "test";
	}
}

