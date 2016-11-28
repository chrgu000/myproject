package mina.httpclient.me;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mina.httpclient.SigUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




/**
 * 
 * @author baozc
 * @date 2015年12月2日 下午3:14:17
 */
public class MeClientTest {
	public static void main(String[] args)  throws Exception{
//		sportTest();
//		rankingList();
//		getDailyView();
//		getVersion();
//		getGroupHistoryMsg();
		t();
	}//1.全新引导页已经\n2.应用数据库全面升级\n3. 修复了若干错误
	/**
	 * 分页查询群组历史消息
	 * TODO
	 * @author 包志超
	 * @date 2016年4月22日下午3:33:35
	 */
	public static void t(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://eoms.ultrapower.com.cn/ultrapmo/trainingRelease/TrainingActivityById.action?pid=4028ea1a564adb6f01565885d99d2e0c");
//		HttpPost post = new HttpPost("http://192.168.120.69:8080/ultrapmo/mework/getDailyView.action");
//		HttpPost post = new HttpPost("http://app.ctinm.com:55001//MeOpen/mobile/getVersion?os=iphone");
//		HttpPost post = new HttpPost("http://221.130.255.35:80/MeOpen/mobile/getGroupHistoryMsg");
		
		String ids = "4028ea1a564adb6f01565885d99d2e0c";
		
		try {
//			           DigestUtils.md5Hex(URLEncoder.encode(bf.toString(),Global.encode)));
			//构建请求参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ajaxValue", ids));//NAPP009
			
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("分页查询群组历史消息,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("分页查询群组历史消息 error code 500");
			}else{
				System.out.println("分页查询群组历史消息,faild---"+response.getStatusLine().getStatusCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 分页查询群组历史消息
	 * TODO
	 * @author 包志超
	 * @date 2016年4月22日下午3:33:35
	 */
	public static void getGroupHistoryMsg(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/meopen/mobile/getGroupHistoryMsg");
//		HttpPost post = new HttpPost("http://192.168.120.69:8080/ultrapmo/mework/getDailyView.action");
//		HttpPost post = new HttpPost("http://app.ctinm.com:55001//MeOpen/mobile/getVersion?os=iphone");
//		HttpPost post = new HttpPost("http://221.130.255.35:80/MeOpen/mobile/getGroupHistoryMsg");
		
		String currentPage = "1";
		String pageSize = "10";
		String groupId = "qz1461291770579";
		
		List<String> paramList = new ArrayList<String>();	
		paramList.add("groupId="+groupId);				 
		paramList.add("currentPage="+currentPage);				 
		paramList.add("pageSize="+pageSize);
		paramList.add("token=ssss");
		
		Collections.sort(paramList);
		StringBuffer bf = new StringBuffer();
		
		for (String param : paramList) {
			bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在一起
		}
		bf.append("$tr34@3wFG5^&w");//手机端默认签名参数 $tr34@3wFG5^&w
		
		try {
//			           DigestUtils.md5Hex(URLEncoder.encode(bf.toString(),Global.encode)));
			String sig=SigUtil.getSig(URLEncoder.encode(bf.toString(),"UTF-8"));
			
			//构建请求参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("groupId", groupId));//NAPP009
			list.add(new BasicNameValuePair("currentPage", currentPage));//NAPP009
			list.add(new BasicNameValuePair("pageSize", pageSize));//NAPP009
			list.add(new BasicNameValuePair("token", "sss"));//NAPP009
			list.add(new BasicNameValuePair("sig", sig));//NAPP009
			list.add(new BasicNameValuePair("s", "true"));//NAPP009
			
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("分页查询群组历史消息,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("分页查询群组历史消息 error code 500");
			}else{
				System.out.println("分页查询群组历史消息,faild---"+response.getStatusLine().getStatusCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 版本更新提示
	 * @author 包志超
	 * @date 2016年3月28日下午4:33:54
	 */
	public static void getVersion(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://192.168.120.69:8080/ultrapmo/mework/getDailyView.action");
//		HttpGet post = new HttpGet("http://app.ctinm.com:55001//MeOpen/mobile/getVersion?os=android");
//		HttpGet post = new HttpGet("http://221.130.255.35:80/MeOpen/mobile/getVersion?os=iphoneRong");
		HttpGet post = new HttpGet("http://met.ultrapower.com.cn:38096/MeOpen/mobile/getVersion?os=android");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("os", "iphone"));//NAPP009
		try {
			//构建url加密实体，并以utf-8方式进行加密；
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
//			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("版本更新提示,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("版本更新提示 error code 500");
			}else{
				System.out.println("版本更新提示,faild---"+response.getStatusLine().getStatusCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过所传日期获取所要填写的日志信息（若所传日期为当前日期，返回待办信息；若所传日期小于当前日期，返回历史日志信息；）
	 * @author 包志超
	 * @date 2016年2月26日下午1:17:07
	 */
	public static void getDailyView(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://192.168.120.69:8080/ultrapmo/mework/getDailyView.action");
		HttpGet post = new HttpGet("http://192.168.120.69:8080/ultrapmo/mework/getDailyView.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("worktime", "2015-10-05"));//NAPP009
		try {
			//构建url加密实体，并以utf-8方式进行加密；
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
//			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("日志信息,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("日志信息 error code 500");
			}else{
				System.out.println("日志信息,faild---"+response.getStatusLine().getStatusCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static String me_client_sig_key="$tr34@3wFG5^&w";	
	
	public static void rankingList() throws Exception{
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/meopen/mobile/getRanking");
		String uid = "1342";
		String steps = "1";
		String random = "fdsafdsa";
		String type = "day";
		String jsonp = "true";
		
		List<String> paramList = new ArrayList<String>();	
		paramList.add("uid="+uid);				 
		paramList.add("random="+random);
		paramList.add("steps="+steps);
		paramList.add("type="+type);
		paramList.add("jsonp="+jsonp);
		
		Collections.sort(paramList);
		StringBuffer bf = new StringBuffer();
		
		for (String param : paramList) {
			bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在一起
		}
		bf.append("$tr34@3wFG5^&w");//手机端默认签名参数
		
		String sig=SigUtil.getSig(URLEncoder.encode(bf.toString(),"UTF-8"));
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("uid", uid));//NAPP009
		list.add(new BasicNameValuePair("steps", steps));
		list.add(new BasicNameValuePair("random", random));
		list.add(new BasicNameValuePair("type", type));
		list.add(new BasicNameValuePair("jsonp", jsonp));
		list.add(new BasicNameValuePair("sig", sig));
		
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setHeader("ContentType","application/x-www-form-urlencoded;charset="+"UTF-8");
			post.setHeader("random",random);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
//			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sportTest() throws Exception{
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://me.ultrapower.com.cn:38096/MeOpen/mobile/getMyStepNumber");
//		HttpPost post = new HttpPost("http://localhost/meopen/mobile/getMyStepNumber");
		HttpPost post = new HttpPost("http://192.168.98.140:38096/MeOpen/mobile/getMyStepNumber");
		
		String uid = "1342";
		String steps = "1";
		String random = "fdsafdsa";
 //		uid:U63X9dO55aUP6AfEXbclY/a7C9RO4udJ,random:1449738749851,steps:4305,sig:70D898254D7AECED146B8E623421F4AE
//		sig:F230F332CF2B7D78B5932DC2410762B2
		uid = "b3ePbzBe32siVIr8l0Si+LdJZvv6isHlhzLjBSDau6Y=";
		random = "1451376689107";
		steps = "4363";
		
		String isPush = "false";
		/*sig签名验证*/
		List<String> paramList = new ArrayList<String>();	
		paramList.add("uid="+uid);				 
		paramList.add("random="+random);
		paramList.add("steps="+steps);
		paramList.add("isPush="+isPush);
//		paramList.add(uid);
//		paramList.add(steps);
//		paramList.add(random);
		
		Collections.sort(paramList);
		StringBuffer bf = new StringBuffer();
		
		for (String param : paramList) {
			bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在一起
		}
		bf.append("$tr34@3wFG5^&w");//手机端默认签名参数
		
		String sig=SigUtil.getSig(URLEncoder.encode(bf.toString(),"UTF-8"));
		System.out.println("sig="+sig);
		
		System.out.println("服务端："+bf);
		String s = bf.toString();
		bf = new StringBuffer();
		System.out.println();
		
//		bf.append("random=1449741730345steps=4305uid=U63X9dO55aWy2pe0BXFMF+QCLROvRu0z$tr34@3wFG5^&w");
//		System.out.println("手机端："+bf.toString());
//		System.out.println("是否一致"+s.equals(bf.toString()));
		
		
//		String s1 = "random%3D1449741730345steps%3D4305uid%3DU63X9dO55aWy2pe0BXFMF%2BQCLROvRu0z%24tr34%403wFG5%5E%26w";
//		System.out.println("手机端加密："+s1);
//		System.out.println("服务端加密："+URLEncoder.encode(bf.toString(),"UTF-8"));
//		System.out.println("是否一致："+s1.equals(URLEncoder.encode(bf.toString(),"UTF-8")));
		System.out.println();
		
//		String sig=SigUtil.getSig(URLEncoder.encode(bf.toString(),"UTF-8"));
		
//		String s2 = "01C5E0AF87D88D3C4EA9498F6CBA65CF";
//		System.out.println("手机端sig:"+s2);
		System.out.println("服务端sig:"+sig);
//		System.out.println("是否一致:"+sig.equals(s2));
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("uid", uid));//NAPP009
		list.add(new BasicNameValuePair("steps", steps));
		list.add(new BasicNameValuePair("random", random));
		list.add(new BasicNameValuePair("isPush", isPush));
		list.add(new BasicNameValuePair("sig", sig));
		
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setHeader("ContentType","application/x-www-form-urlencoded;charset="+"UTF-8");
			post.setHeader("random",random);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
//			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
