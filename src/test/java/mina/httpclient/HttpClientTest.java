package mina.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端的方式来访问web资源
 * @author baozhichao 2014年12月10日
 *
 */
public class HttpClientTest {
	
	private Logger logger = LoggerFactory.getLogger(HttpClientTest.class);

	public static void main(String[] args) {
//		test1();
		test2();
//		test3();
//		test4();
//		test5();
//		testAppDept();
	}
	
	/**
	 * 测试1：构建复杂uri，这种方式会很方便的设置多个参数
	 * HttpClients类是client的具体一个实现类；
     * URIBuilder包含：协议，主机名，端口（可选），资源路径，和多个参数（可选）
	 *@author baozhichao
	 *2015年7月17日下午4:01:16
	 */
	private static void test1(){
		try {
			HttpClient client = HttpClients.createDefault();
			URI uri = new URIBuilder().setScheme("http")
								.setHost("localhost")
								.setPath("/app/nativeApp/param.action")
								.setParameter("", "")
								.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response =  client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				System.out.println(EntityUtils.toString(response.getEntity()));
				//以下这种方式读取流也可以，只不过用EntityUtils会更方便
		        /*InputStream is = response.getEntity().getContent();
		        ByteArrayOutputStream os = new ByteArrayOutputStream();
		        byte[] buffer = new byte[1024];
		        int len=-1;
		        while((len = is.read(buffer))!=-1){
		            os.write(buffer,0,len);
		        }
		        os.close();
		        is.close();
		        System.out.println(os.size()+new String(os.toByteArray(),"utf-8"));*/
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 为uri进行加密，并进行表单提交；
     * 许多应用需要模拟提交的HTML表单的处理，例如，在
        为了登录到Web应用程序或提交的输入数据。 HttpClient提供的实体类
        UrlEncodedFormEntity可以实现该过程；
	 *@author baozhichao
	 *2015年7月17日下午4:19:53
	 */
	private static void test2(){
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://211.139.58.237:38092/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://192.168.98.140:38095/ultramobile/mobile/call.action");
//		HttpPost post = new HttpPost("http://localhost/app/mobile/call.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serviceCode", "NAPP001"));//NAPP009
		list.add(new BasicNameValuePair("type", "xml"));
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"//001
//				+ "<opDetail>"+
//							"<baseInfo>"+
//								"<userName>1888883151-vfds</userName>"+
//								"<password>password</password>"+
//								"<machineCode>phone</machineCode>"+
//								"<simNum>12345</simNum>"+
//							"</baseInfo>"+
//							"<recordInfo>"+
//								"<currentPage>1</currentPage>"+
//								"<categoryValue>1</categoryValue>"+
//								"<pageSize>10</pageSize>"+
//								"<os>android</os>"+
//							"</recordInfo>"+
//					"</opDetail>";
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><opDetail><baseInfo><userName>test1111</userName><password>123456</password><machineCode>phone</machineCode><simNum>12345</simNum></baseInfo><recordInfo><currentPage>1</currentPage><pageSize>10</pageSize><categoryValue></categoryValue><os>android</os></recordInfo></opDetail>";
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"//009
//				+ "<opDetail>"+
//				"<baseInfo>"+
//				"<userName>1888883151</userName>"+
//				"<password>password</password>"+
//				"<machineCode>phone</machineCode>"+
//				"<simNum>12345</simNum>"+
//				"</baseInfo>"+
//				"<recordInfo>"+
//				"<appId>97c5ee4af950495b9768d5d0a1192daf</appId>"+
//				"<currentPage>1</currentPage>"+
//				"<pageSize>5</pageSize>"+
//				"</recordInfo>"+
//				"</opDetail>";
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+//03
//"<opDetail><baseInfo><userName>lixin</userName><password>password</password><machineCode>phone</machineCode><simNum>12345</simNum></baseInfo>"
//+ "<recordInfo><currentPage>1</currentPage><pageSize>6</pageSize><os>android></os></recordInfo></opDetail>";
		
//		String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//"<opDetail><baseInfo><userName>wulinying</userName><password>password</password><machineCode>phone</machineCode><simNum>12345</simNum></baseInfo><recordInfo><appId>b7e85294cade4863a7e8edbd024346aa</appId><comment>Test</comment><score>4</score></recordInfo></opDetail>";
		list.add(new BasicNameValuePair("inputXml", xml));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void testAppDept(){
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://192.168.98.140:38095/ultramobile/mobile/call.action");
		HttpPost post = new HttpPost("http://localhost/app/mobile/appDeptRelation.action");
		
		//构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String json = "{'list':[{'appKey':'1','type':'dept','deptList':[{'deptId':'1'},{'deptId':'2'}]},"
		+ "{'appKey':'ujyk','type':'user','deptList':[{'deptId':'3'},{'deptId':'4'}]}]}";
		list.add(new BasicNameValuePair("inputJson", json));
		try {
			//构建url加密实体，并以utf-8方式进行加密；
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				//org.apache.http.util.EntityUtils类可以快速处理服务器返回实体对象
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 以回调方式处理返回结果
	 * 处理响应的最简单和最方便的方法是通过使用ResponseHandler的
        接口。用户不必担心连接管理的问题。当使用一个
        ResponseHandler的时候，无论是否请求执行成功或导致异常，HttpClient将会自动释放连接。
	 *@author baozhichao
	 *2015年7月17日下午4:37:19
	 */
	private static void test3(){
		HttpClient client = HttpClients.createDefault();
		ResponseHandler<Object> handler = new ResponseHandler<Object>() {
			@Override
			public Object handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				HttpEntity entity = response.getEntity();
				if(entity == null){
					throw new ClientProtocolException("返回结果为空");
				}
				if(response.getStatusLine().getStatusCode() == 200){
					//获取返回结果的字符集 如：utf-8  gbk，并以这种字符集来读取流信息
					ContentType contentType = ContentType.getOrDefault(entity);
					Charset charset = contentType.getCharset();
					
					InputStreamReader reader = new InputStreamReader(entity.getContent(), charset);
					BufferedReader br = new BufferedReader(reader);
					StringBuffer sb = new StringBuffer();
					while(br.ready()){
						sb.append(br.readLine());
					}
					return sb.toString();
				}
				return null;
			}
		};
		
		try {
			URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPath("/app/nativeApp/param.action").setParameter("err", "err").build();
			HttpPost post = new HttpPost(uri);
			//handler回调
			Object obj = client.execute(post, handler);
			System.out.println("返回结果："+obj);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构建复杂请求体
     * RequestConfig将会保存在context上下文中，并会在连续的请求中进行传播（来自官方文档）；
	 *@author baozhichao
	 *2015年7月17日下午4:49:01
	 */
	private static void test4(){
		CloseableHttpClient client = HttpClients.createDefault();
        
        //构建请求配置
        RequestConfig config = RequestConfig.
                custom().
                setSocketTimeout(1000*10).
                setConnectTimeout(1000*10).
                build();
        //==============================================================
        ResponseHandler<Object> handler = new ResponseHandler<Object>() {//回调
            @Override
            public Object handleResponse(final HttpResponse response) throws IOException {
                 
                HttpEntity entity = response.getEntity();
                 
                if (entity == null) {
                    throw new ClientProtocolException( "返回结果为空");
                }
             
                if(response.getStatusLine().getStatusCode()==200){
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                     
                    InputStreamReader reader = new InputStreamReader(entity.getContent(), charset);
                    BufferedReader br = new BufferedReader(reader);
                    StringBuffer sb = new StringBuffer();
                    char[] buffer = new char[1024];
                    while (br.read(buffer)!=-1) {
                        sb.append(new String(buffer));
                    }
                     
                    return sb.toString();
                }
                 
                return null;
                 
            }
        };
        //===================================================================
         
        URI uri = null;
        try {
        	uri = new URIBuilder().setScheme("http").setHost("localhost").setPath("/app/nativeApp/param.action").setParameter("err", "err").build();
             
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
         
        HttpPost post = new HttpPost(uri);
         
        post.setConfig(config);//设置请求配置
         
        try {
             Object obj = client.execute(post, handler);
              
             System.out.println("返回结果："+obj);
             
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
	}

	/**
	 * 异常恢复机制
	 *@author baozhichao
	 *2015年7月17日下午4:56:54
	 */
	private static void test5(){
		 HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			
			 /**
			  *  exception异常信息；
             * executionCount：重连次数；
             * context：上下文
			  */
			@Override
			public boolean retryRequest(IOException exception, int executionCount,HttpContext context) {
				System.out.println("重连接次数："+executionCount);
				if(executionCount>=5){//如果连接次数超过5次，就不进行重复连接
					return false;
				}
				if (exception instanceof InterruptedIOException) {//io操作中断
                    return false;
                }
                if (exception instanceof UnknownHostException) {//未找到主机
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {//连接超时
                    return true;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                 
                HttpRequest request = clientContext.getRequest();
                 
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                 
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
				return false;
			}
		};
		
		CloseableHttpClient client = HttpClients.custom().setRetryHandler(retryHandler).build();
		RequestConfig config = RequestConfig.
                custom().
                setSocketTimeout(1000*10).
                setConnectTimeout(1000*10).
                build();
         
        HttpPost post = new HttpPost("http://localhost/app/nativeApp/param.action");
         
        post.setConfig(config);
        
        try {
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			System.out.println("返回结果："+EntityUtils.toString(entity));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     *  HTTP请求过滤器，在执行请求之前拦截HttpRequest 和 HttpContext；
     */
    private static void test7() throws ClientProtocolException, IOException{
         
        HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
            /**
             * 处理请求：
             * 如果是客户端：这个处理在发送请求之前执行；
             * 如果是服务器：这个处理在接收到请求体之前执行；
             */
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                request.addHeader("count", Integer.toString(count.getAndIncrement()));
            }
        };
         
         
        CloseableHttpClient httpclient = HttpClients.
                        custom().
                        addInterceptorLast(requestInterceptor).
                        build();
         
         
        AtomicInteger count = new AtomicInteger(1);
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute("count", count);
        HttpGet httpget = new HttpGet("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo");
         
        for (int i = 0; i < 10; i++) {
            CloseableHttpResponse response = httpclient.execute(httpget,context);
            try {
                 
                HttpEntity entity = response.getEntity();
//              System.out.println(EntityUtils.toString(entity));
                 
            } finally {
                response.close();
            }
        }
    }
     
     
     
     
    /**
     * 
     *  httpclient会自动处理重定向； 
     *  
     *  301 永久重定向,告诉客户端以后应从新地址访问.
        302 作为HTTP1.0的标准,以前叫做Moved Temporarily ,现在叫Found. 现在使用只是为了兼容性的  处理,包括PHP的默认Location重定向用的也是302.
        但是HTTP 1.1 有303 和307作为详细的补充,其实是对302的细化
        303：对于POST请求，它表示请求已经被处理，客户端可以接着使用GET方法去请求Location里的URI。
        307：对于POST请求，表示请求还没有被处理，客户端应该向Location里的URI重新发起POST请求。
     */
    private static void test8() throws ClientProtocolException, IOException,
            URISyntaxException {
 
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();
 
        HttpClientContext context = HttpClientContext.create();
        HttpGet httpget = new HttpGet("http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getDatabaseInfo");
        CloseableHttpResponse response = httpclient.execute(httpget, context);
        try {
            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
            System.out.println("Final HTTP location: " + location.toASCIIString());
        } finally {
            response.close();
        }
    }
     
}
