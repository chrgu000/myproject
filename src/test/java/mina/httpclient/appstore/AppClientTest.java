package mina.httpclient.appstore;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AppClientTest {
	
	public static void main(String[] args) {
//		getVersion();//ME获取更新提示接口
//		getProvinceList();//获取省份列表
		getAppListByCP();//NAPP001 根据分类值和省份值获取app列表
//		recommendAppList();//NAPP003		获取应用推荐接口
//		needToBeUpdate();// NAPP004 <br/>获取更新接口
//		attentionApp();//NAPP006 <br/>关注APP接口
//		category();//NAPP007  获取app分类接口
//		saveAppUserComments();//NAPP008 添加评论接口
//		userCommentsList();//NAPP009 获取应用评论详情列表接口
//		focusAppList();//NAPP010 获取已安装应用接口
//		getAppNames();//模糊查询应用名称列表
//		searchAppName();//根据appName查询应用
//		topSearchQueries();//热门搜索appName列表
		System.out.println(new Date(1477455605268l));
//		appDownload();
//		t2();
	}
	
	
	public static void t2(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://10.10.80.123:8088/uflowda/tcCluster.do?method=cluster");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("project", "JiLin"));//NAPP009
		list.add(new BasicNameValuePair("template", "dement"));
		list.add(new BasicNameValuePair("start", "0"));
		list.add(new BasicNameValuePair("end", "0s"));
		
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
//			PostMethod me = new PostMethod();
//			me.set
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				String s = EntityUtils.toString(response.getEntity());
				System.out.println(s);
				
				JSONObject js = JSONObject.fromObject(s);
				System.out.println("模糊查询应用名称列表,success"+js);
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP013 error code 500");
			}else{
				System.out.println(" 模糊查询应用名称列表,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 热门搜索appName列表
	 * @author 包志超
	 * @date 2016年3月2日下午5:08:34
	 */
	public static void topSearchQueries(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001//ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP015"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<opDetail>"+
			"<baseInfo>"+
//				"<userName>sunyaping</userName>"+//15010731173
//				"<appName>日常管理</appName>"+//外部应用
			"</baseInfo>"+
			"<recordInfo>"+
				"<currentPage>1</currentPage>"+
				"<pageSize>50</pageSize>"+
				"<os>android</os>"+
			"</recordInfo>"+
		"</opDetail>";

		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("模糊查询应用名称列表,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP013 error code 500");
			}else{
				System.out.println(" 模糊查询应用名称列表,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据appName查询应用
	 * @author 包志超
	 * @date 2016年3月2日下午3:15:39
	 */
	public static void searchAppName(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://app.ctinm.com:55001//ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP014"));//NAPP009
		list.add(new BasicNameValuePair("type", "json"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<opDetail>"+
		"	<baseInfo>"+
		"		<userName>anwei</userName>"+
		"		<password>password</password>"+
		"		<machineCode>phone</machineCode>"+
		"		<simNum>12345</simNum>"+
		"	</baseInfo>"+
		"	<recordInfo>"+
		"		<topSearch>电子</topSearch>"+
		"		<os>android</os>"+
		"	</recordInfo>"+
		"</opDetail>";


		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("模糊查询应用名称列表,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP013 error code 500");
			}else{
				System.out.println(" 模糊查询应用名称列表,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据appname模糊查询应用名称列表
	 * @author 包志超
	 * @date 2016年3月1日下午5:47:58
	 */
	public static void getAppNames(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP013"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<opDetail>"+
			"<baseInfo>"+
				"<userName>sunyaping</userName>"+//15010731173
				"<appName>试</appName>"+
			"</baseInfo>"+
			"<recordInfo>"+
				"<os>android</os>"+
			"</recordInfo>"+
		"</opDetail>";

		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("模糊查询应用名称列表,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP013 error code 500");
			}else{
				System.out.println(" 模糊查询应用名称列表,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP001 <br/>根据分类值和省份值获取app列表
	 * @author 包志超
	 * @date 2016年1月14日上午10:38:48
	 */
	@SuppressWarnings({ "deprecation", "resource" })
	public static void getAppListByCP(){
		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:9011/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://me.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001//ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP001"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<opDetail>"+
			"<baseInfo>"+
				"<userName>18611693443</userName>"+//15010731173
				"<password>password</password>"+
				"<machineCode>phone</machineCode>"+
				"<simNum>12345</simNum>"+
				"<appId></appId>"+//449183b6cdd8408caecd69a90d9e253f
				"<appKey></appKey>"+
			"</baseInfo>"+
			"<recordInfo>"+
				"<currentPage>1</currentPage>"+
				"<pageSize>50</pageSize>"+
//				"<categoryValue>1</categoryValue>"+
				"<provinceValue>JT</provinceValue>"+//JT
				"<os>android</os>"+
			"</recordInfo>"+
		"</opDetail>";

		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("根据分类值和省份值获取app列表,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP001 error code 500");
			}else{
				System.out.println(" 根据分类值和省份值获取app列表更新提示,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP001 <br/>根据分类值和省份值获取app列表
	 * @author 包志超
	 * @date 2016年1月14日上午10:38:48
	 */
	public static void appDownload(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://me.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001//ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP011"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<opDetail>"+
				"<baseInfo>"+
				"<userName>15011577876</userName>"+//15010731173
				"</baseInfo>"+
				"<recordInfo>"+
//				"<categoryValue>1</categoryValue>"+
				"<appId>dfa01d90536e4f5faf9ade4459ef2f7a</appId>"+//JT
				"<os>android</os>"+
				"</recordInfo>"+
				"</opDetail>";
		
		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("根据分类值和省份值获取app列表,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP001 error code 500");
			}else{
				System.out.println(" 根据分类值和省份值获取app列表更新提示,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP003<br/> 获取应用推荐接口
	 * @author 包志超
	 * @date 2016年1月19日下午3:38:01
	 */
	public static void recommendAppList(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001//ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP003"));
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<opDetail>"+
					"<baseInfo>"+
						"<userName>188****8888</userName>"+
						"<password>password</password>"+//没用
						"<machineCode>phone</machineCode>"+//没用
						"<simNum>12345</simNum>"+//没用
					"</baseInfo>"+
					"<recordInfo>"+
						"<os>android</os>"+
					"</recordInfo>"+
				"</opDetail>";
		
		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP003<br/> 获取应用推荐接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP003<br/> 获取应用推荐接口 error code 500");
			}else{
				System.out.println(" NAPP003<br/> 获取应用推荐接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP004 <br/>获取更新接口
	 * @author 包志超
	 * @date 2016年1月19日下午4:36:18
	 */
	public static void needToBeUpdate(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP004"));
		list.add(new BasicNameValuePair("type", "xml"));
		
//		String xml = 
//				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//					"<opDetail>"+
//						"<baseInfo>"+
//							"<userName>188****8888</userName>"+
//							"<password>password</password>"+//没用
//							"<machineCode>phone</machineCode>"+//没用
//							"<simNum>12345</simNum>"+//没用
//						"</baseInfo>"+
//						"<recordInfo>"+
//							"<app appId=\"com.Qunar\" appVersion=\"92\"></app>"+
//							"<app appId=\"com.lenovo.lsf\" appVersion=\"4521512\"></app>"+
//							"<app appId=\"com.mzba.happy.laugh\" appVersion=\"64\"></app>"+
//							"<os>android</os>"+ 
//						"</recordInfo>"+ 
//					"</opDetail>";
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<opDetail>"
				+ "<baseInfo>"
				+ "<userName>15311410093</userName>"
				+ "<password>password</password>"
				+ "<machineCode>phone</machineCode>"
				+ "<simNum>12345</simNum>"
				+ "</baseInfo>"
				+ "<recordInfo>"
				+ "<os>android</os>"
				+ "<app appId=\"com.ultrapower\" appVersion=\"3\"></app>"
//				+ "<app appId=\"com.iflytek.cmcc\" appVersion=\"1719\"></app>"
//				+ "<app appId=\"com.ctsi.vipnetmanager.engineer\" appVersion=\"15\"></app>"
//				+ "<app appId=\"com.motorola.china.extensions\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.motorola.motocare\" appVersion=\"2\"></app>"
//				+ "<app appId=\"com.android.providers.telephony\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.providers.calendar\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.providers.media\" appVersion=\"700\"></app>"
//				+ "<app appId=\"com.motorola.bug2go\" appVersion=\"0\"></app>"
//				+ "<app appId=\"com.motorola.camera\" appVersion=\"50101312\"></app>"
//				+ "<app appId=\"com.android.wallpapercropper\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.motorola.multisimsettings\" appVersion=\"10000\"></app>"
//				+ "<app appId=\"com.qihoo.freewifi\" appVersion=\"359\"></app>"
//				+ "<app appId=\"com.motorola.ccc.devicemanagement\" appVersion=\"1\"></app>"
//				+ "<app appId=\"com.lenovo.music\" appVersion=\"30030310\"></app>"
//				+ "<app appId=\"com.motorola.android.fmradio\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.tencent.qqmusic\" appVersion=\"322\"></app>"
//				+ "<app appId=\"com.motorola.cdmacallsettings\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.documentsui\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.externalstorage\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.htmlviewer\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.iflytek.speechsuite\" appVersion=\"10087\"></app>"
//				+ "<app appId=\"com.android.mms.service\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.lenovo.safecenter\" appVersion=\"4903509\"></app>"
//				+ "<app appId=\"com.android.providers.downloads\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.motorola.android.settings.diag_mdlog\" appVersion=\"4\"></app>"
//				+ "<app appId=\"com.ustcinfo.tpc.oss.mobile.dzyw\" appVersion=\"23\"></app>"
//				+ "<app appId=\"com.motorola.ccc.checkin\" appVersion=\"1\"></app>"
//				+ "<app appId=\"com.motorola.ccc.mainplm\" appVersion=\"1\"></app>"
//				+ "<app appId=\"com.motorola.motgeofencesvc\" appVersion=\"10001\"></app>"
//				+ "<app appId=\"com.android.soundrecorder\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.motorola.ccc.cce\" appVersion=\"6\"></app>"
//				+ "<app appId=\"com.motorola.ccc.ota\" appVersion=\"70010\"></app>"
//				+ "<app appId=\"com.motorola.ccc.notification\" appVersion=\"1\"></app>"
//				+ "<app appId=\"com.android.defcontainer\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.qualcomm.services.location\" appVersion=\"1\"></app>"
//				+ "<app appId=\"com.android.providers.downloads.ui\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.android.pacprocessor\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.ultrapower.android.me.telecom\" appVersion=\"113\"></app>"
//				+ "<app appId=\"com.cootek.smartdialer\" appVersion=\"5750\"></app>"
//				+ "<app appId=\"com.motorola.demomode\" appVersion=\"21\"></app>"
//				+ "<app appId=\"cn.y189.youe\" appVersion=\"13\"></app>"
//				+ "<app appId=\"com.tencent.mm\" appVersion=\"720\"></app>"
//				+ "<app appId=\"com.android.certinstaller\" appVersion=\"21\"></app>"
//				+ "<app appId=\"com.motorola.trustagents\" appVersion=\"10009\"></app>"
//				+ "<app appId=\"android\" appVersion=\"21\"></app><app appId=\"com.android.contacts\" appVersion=\"21\"></app><app appId=\"com.motorola.android.provisioning\" appVersion=\"1\"></app><app appId=\"com.motorola.context\" appVersion=\"518063\"></app><app appId=\"com.android.mms\" appVersion=\"21\"></app><app appId=\"com.android.stk\" appVersion=\"21\"></app><app appId=\"com.android.launcher3\" appVersion=\"21\"></app><app appId=\"com.android.backupconfirm\" appVersion=\"21\"></app><app appId=\"com.motorola.migrate\" appVersion=\"17006\"></app><app appId=\"com.lenovo.leos.appstore\" appVersion=\"80000\"></app><app appId=\"com.motorola.incrementalupdates\" appVersion=\"1\"></app><app appId=\"com.android.provision\" appVersion=\"21\"></app><app appId=\"com.motorola.moodles\" appVersion=\"300\"></app><app appId=\"com.motorola.motocit\" appVersion=\"109\"></app><app appId=\"com.android.wallpaper.holospiral\" appVersion=\"21\"></app><app appId=\"com.android.phasebeam\" appVersion=\"1\"></app><app appId=\"com.happyelements.AndroidAnimal\" appVersion=\"30\"></app><app appId=\"com.qualcomm.qcrilmsgtunnel\" appVersion=\"21\"></app><app appId=\"com.android.providers.settings\" appVersion=\"21\"></app><app appId=\"com.android.sharedstoragebackup\" appVersion=\"21\"></app><app appId=\"com.android.printspooler\" appVersion=\"1\"></app><app appId=\"com.android.dreams.basic\" appVersion=\"21\"></app><app appId=\"com.motorola.MotGallery2\" appVersion=\"304013\"></app><app appId=\"com.android.inputdevices\" appVersion=\"21\"></app><app appId=\"com.motorola.android.nativedropboxagent\" appVersion=\"1\"></app><app appId=\"com.motorola.autopackageinstaller\" appVersion=\"21\"></app><app appId=\"com.motorola.motosignature.app\" appVersion=\"1\"></app><app appId=\"com.motorola.quicksearchbox\" appVersion=\"21\"></app><app appId=\"com.lenovo.browser\" appVersion=\"60060006\"></app><app appId=\"com.android.cellbroadcastreceiver\" appVersion=\"21\"></app><app appId=\"com.google.android.webview\" appVersion=\"111201\"></app><app appId=\"com.motorola.fmrecording\" appVersion=\"21\"></app><app appId=\"com.android.onetimeinitializer\" appVersion=\"21\"></app><app appId=\"com.ss.android.article.news\" appVersion=\"522\"></app><app appId=\"com.motorola.contacts.preloadcontacts\" appVersion=\"1\"></app><app appId=\"com.android.server.telecom\" appVersion=\"21\"></app><app appId=\"com.motorola.android.providers.settings\" appVersion=\"21\"></app><app appId=\"com.android.keychain\" appVersion=\"21\"></app><app appId=\"com.android.dialer\" appVersion=\"21\"></app><app appId=\"com.baidu.input\" appVersion=\"108\"></app><app appId=\"com.motorola.email\" appVersion=\"20028\"></app><app appId=\"com.motorola.genie\" appVersion=\"106000\"></app><app appId=\"com.tencent.mobileqq\" appVersion=\"334\"></app><app appId=\"com.motorola.setup\" appVersion=\"19\"></app><app appId=\"com.android.packageinstaller\" appVersion=\"21\"></app><app appId=\"com.motorola.contextual.smartrules2\" appVersion=\"20607\"></app><app appId=\"com.android.proxyhandler\" appVersion=\"21\"></app><app appId=\"com.android.inputmethod.latin\" appVersion=\"21\"></app><app appId=\"com.android.musicvis\" appVersion=\"21\"></app><app appId=\"com.motorola.android.providers.userpreferredsim\" appVersion=\"12107\"></app><app appId=\"com.motorola.onetimeinitializer\" appVersion=\"21\"></app><app appId=\"com.android.managedprovisioning\" appVersion=\"21\"></app><app appId=\"com.motorola.launcherconfig\" appVersion=\"10000000\"></app><app appId=\"com.android.dreams.phototable\" appVersion=\"21\"></app><app appId=\"com.android.noisefield\" appVersion=\"1\"></app><app appId=\"com.android.wallpaper.livepicker\" appVersion=\"21\"></app><app appId=\"com.motorola.motocare.internal\" appVersion=\"1\"></app><app appId=\"com.lenovo.android.calendar\" appVersion=\"2190257\"></app><app appId=\"com.qihoo.appstore\" appVersion=\"300030545\"></app><app appId=\"com.motorola.fmplayer\" appVersion=\"21\"></app><app appId=\"com.motorola.audioeffects\" appVersion=\"21\"></app><app appId=\"com.netease.mail\" appVersion=\"77\"></app><app appId=\"com.android.settings\" appVersion=\"21\"></app><app appId=\"com.carrot.iceworld\" appVersion=\"320\"></app><app appId=\"com.google.android.inputmethod.pinyin\" appVersion=\"4210115\"></app><app appId=\"com.android.calculator2\" appVersion=\"21\"></app><app appId=\"com.autonavi.minimap\" appVersion=\"558\"></app><app appId=\"com.qualcomm.location\" appVersion=\"1\"></app><app appId=\"com.lmi.motorola.rescuesecurity\" appVersion=\"74518\"></app><app appId=\"com.android.wallpaper\" appVersion=\"21\"></app><app appId=\"com.android.vpndialogs\" appVersion=\"21\"></app><app appId=\"com.lenovo.launcher\" appVersion=\"7000278\"></app><app appId=\"com.motorola.china.cdbrefresh\" appVersion=\"21\"></app><app appId=\"com.motorola.bach.modemstats\" appVersion=\"1\"></app><app appId=\"com.android.phone\" appVersion=\"21\"></app><app appId=\"com.android.shell\" appVersion=\"21\"></app><app appId=\"com.android.providers.userdictionary\" appVersion=\"21\"></app><app appId=\"com.android.location.fused\" appVersion=\"21\"></app><app appId=\"com.android.deskclock\" appVersion=\"302\"></app><app appId=\"com.android.systemui\" appVersion=\"21\"></app><app appId=\"com.motorola.autoregistration\" appVersion=\"21\"></app><app appId=\"com.rsupport.mobizen.m360\" appVersion=\"10\"></app><app appId=\"com.amap.android.location\" appVersion=\"333\"></app><app appId=\"com.motorola.china.loader\" appVersion=\"21\"></app><app appId=\"com.qihoo360.contacts\" appVersion=\"5011\"></app><app appId=\"com.android.bluetooth\" appVersion=\"21\"></app><app appId=\"com.qualcomm.timeservice\" appVersion=\"21\"></app><app appId=\"com.qualcomm.atfwd\" appVersion=\"21\"></app><app appId=\"com.android.providers.contacts\" appVersion=\"21\"></app><app appId=\"com.ct.client\" appVersion=\"5500\"></app><app appId=\"im.yixin\" appVersion=\"223\"></app><app appId=\"com.android.captiveportallogin\" appVersion=\"21\"></app><app appId=\"com.motorola.so\" appVersion=\"2010001\"></app><app appId=\"com.motorola.android.dm.service\" appVersion=\"1\"></app><app appId=\"com.hp.android.printservice\" appVersion=\"67\"></app>"
				+ "</recordInfo></opDetail>";

		list.add(new BasicNameValuePair("inputXml", xml));
		try {            
			//构建url加utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP003<br/> 获取应用推荐接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP003<br/> 获取应用推荐接口 error code 500");
			}else{
				System.out.println(" NAPP003<br/> 获取应用推荐接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP006 <br/>关注APP接口
	 * @author 包志超
	 * @date 2016年1月19日下午5:42:28
	 */
	public static void attentionApp(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://192.168.180.143:38092/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://app.ctinm.com:55001/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP006"));
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<opDetail>"+
					"<baseInfo>"+
						"<userName>18911628753</userName>"+
						"<password>password</password>"+//没用
						"<machineCode>phone</machineCode>"+//没用
						"<simNum>12345</simNum>"+//没用
					"</baseInfo>"+
					"<recordInfo>"+
						"<appId>fb150d6c949144f790d9d168d0d1a84d</appId>"+
						"<isFocus>1</isFocus>"+
						"<os>android</os>"+
					"</recordInfo>"+ 
				"</opDetail>";
		list.add(new BasicNameValuePair("inputXml", xml));
		try {            
			//构建url加utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP006 <br/>关注APP接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP006 <br/>关注APP接口 error code 500");
			}else{
				System.out.println("NAPP006 <br/>关注APP接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP007<br/> 获取app分类接口
	 * @author 包志超
	 * @date 2016年1月20日上午9:34:49
	 */
	public static void category(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP007"));
		list.add(new BasicNameValuePair("type", "xml"));
		
		try {            
			//构建url加utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP007<br/> 获取app分类接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP007<br/> 获取app分类接口 error code 500");
			}else{
				System.out.println("NAPP007<br/> 获取app分类接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP008<br/>添加评论接口
	 * @author 包志超
	 * @date 2016年1月13日上午11:19:26
	 */
	public static void saveAppUserComments(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://app.ctinm.com:55001/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP008"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<opDetail>"
					+ "<baseInfo>"
						+ "<userName>wangfeilong</userName>"
						+ "<password>password</password>"
						+ "<machineCode>phone</machineCode>"
						+ "<simNum>12345</simNum>"
					+ "</baseInfo>"
					+ "<recordInfo>"
						+ "<appId>6496179de2d847e7b538a98f8af1703a</appId>"
						+ "<comment>咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默默嫩模陌陌摸摸OK啦了说扔上去啦咯 咳咳咳我咯默</comment>"
						+ "<score>1</score>"
						+ "<fullName>王飞龙</fullName>"
						+ "<os>android</os>"
					+ "</recordInfo>"
				+ "</opDetail>";
		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP008<br/>添加评论接口成功");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP008<br/>添加评论接口 error code 500");
			}else{
				System.out.println("NAPP008<br/>添加评论接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * NAPP009<br/> 获取应用评论详情列表接口
	 * @author 包志超
	 * @date 2016年1月20日上午9:40:40
	 */
	public static void userCommentsList(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP009"));
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<opDetail>"
					+ "<baseInfo>"
						+ "<userName>wangfeilong</userName>"//没用
						+ "<password>password</password>"//没用
						+ "<machineCode>phone</machineCode>"//没用
						+ "<simNum>12345</simNum>"//没用
					+ "</baseInfo>"
					+ "<recordInfo>"
						+ "<appId>6496179de2d847e7b538a98f8af1703a</appId>"
						+ "<currentPage>1</currentPage>"
						+ "<pageSize>5</pageSize>"
						+ "<os>android</os>"
					+ "</recordInfo>"
				+ "</opDetail>";
		list.add(new BasicNameValuePair("inputXml", xml));
		try {            
			//构建url加utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP006 <br/>关注APP接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP006 <br/>关注APP接口 error code 500");
			}else{
				System.out.println("NAPP006 <br/>关注APP接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP010<br> 获取已安装应用接口
	 * @author 包志超
	 * @date 2016年1月20日上午9:51:15
	 */
	public static void focusAppList(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP010"));
		list.add(new BasicNameValuePair("type", "xml"));
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<opDetail>"
				+ "<baseInfo>"
					+ "<userName>wangfeilong</userName>"//没用
					+ "<password>password</password>"//没用
					+ "<machineCode>phone</machineCode>"//没用
					+ "<simNum>12345</simNum>"//没用
				+ "</baseInfo>"
				+ "<recordInfo>"
					+ "<currentPage>1</currentPage>"
					+ "<pageSize>5</pageSize>"
					+ "<appId>6496179de2d847e7b538a98f8af1703a</appId>"
					+ "<os>android</os>"
				+ "</recordInfo>"
			+ "</opDetail>";
		list.add(new BasicNameValuePair("inputXml", xml));
		try {            
			//构建url加utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP010<br> 获取已安装应用接口,success");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP010<br> 获取已安装应用接口error code 500");
			}else{
				System.out.println("NAPP010<br> 获取已安装应用接口,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * NAPP012<br/>获取省份列表
	 * @author 包志超
	 * @date 2016年1月13日上午11:22:20
	 */
	public static void getProvinceList(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
//		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://app.ctinm.com:55001/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://met.ultrapower.com.cn:9011/ultramobile/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP012"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("NAPP012<br/>获取省份列表成功");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("NAPP012<br/>获取省份列表 error code 500");
			}else{
				System.out.println(" NAPP012<br/>获取省份列表,faild");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * ME获取更新提示接口
	 * @author 包志超
	 * @date 2016年1月4日下午4:36:11
	 */
	public static void getVersion(){
		try {
			HttpClient client = HttpClients.createDefault();
			URI uri = new URIBuilder().setScheme("http")
//								.setHost("app.ctinm.com:55001")
								.setHost("221.130.255.35")
								.setPath("/MeOpen/mobile/getVersion")
								.setParameter("os", "android")//iphoneRong
								.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response =  client.execute(get);
			System.out.println("连接成功……");
			if(response.getStatusLine().getStatusCode() == 200){
				System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println(" ME获取更新提示,获取接口成功");
			}else if(response.getStatusLine().getStatusCode() == 500){
				System.out.println("error code 500\n"+response);
			}
			else{
				System.out.println(" ME获取更新提示,faild");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
