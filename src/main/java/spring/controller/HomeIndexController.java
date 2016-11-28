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

import spring.controller.base.BaseCRUDController;
import spring.po.User;

/**
 *@author baozhichao
 *2013-12-19 下午3:13:02
 */
@Controller
@RequestMapping("/home")
public class HomeIndexController extends BaseCRUDController<User, Long> {

	@RequestMapping(method=RequestMethod.GET,value="/index")
	public String index(){
		return "home/index";
	}
	
	@RequestMapping("/file")
	public void file(HttpServletResponse response) throws IOException{
		File f = new File("d:/test.xls");
		//response.setHeader告诉浏览器以什么方式打开 
		//new String("森林").getBytes("utf-8")  URLEncoder.encode("无可奈何", "UTF-8")
		/**
		 * content-disposition   的   HTTP   response   header   允许指定文档表示的信息。
		 * 使用这种   header   ，你就可以将文档指定成单独打开（而不是在浏览器中打开），
		 * 当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
		 * 还可以根据用户的操作来显示。如果用户要保存文档，你还可以为该文档建议一个文件名。
		 * 这个建议名称会出现在   Save   As   对话框的“文件名”栏中。   
		 * 打开位置：   
		 * 			attachment   ――   表示作为附件发送到客户端，客户端将单独打开此文件。   
		 *			inline   ――   表示将在浏览器中打开这个文件。   
		 *文件名：   
		 *			filename   ――   表示发送到客户端文件的文件名。*/
		response.setHeader("Content-Disposition", "attachment;filename="+
		 new String("朝秦暮楚".getBytes("utf-8"), "ISO8859-1"));
		
		/**Response.setContentType(MIME_TYPE) 的作用是使客户端浏览器区分不同类型的数据，
		并根据MIME_TYPE的不同，调用浏览器内不同的程序嵌入模块来处理相应的数据。
		例如WEB浏览器通过MIME_TYPE类型来判断文件是GIF图片，还是JSON字符串。*/
		response.setContentType("application/vnd.ms-excel");
		OutputStream out = response.getOutputStream();
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		while(in.available()>0){
			in.read(b);
			out.write(b);//向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
		}
		in.close();
		out.close();
		out.flush();
	}
}

