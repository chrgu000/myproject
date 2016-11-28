package mina.redis;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String[] args) {
//		listTest();
		// hset();
		r();
//		Jedis me = RedisJava
//				.getJedis("192.168.106.150", 7000, "meUltrapass");
//		String s ="SN_1477020669_10001722";
//		System.out.println(s.matches("^SN_\\d{11}$"));
//		String d = s.substring(s.indexOf("_")+1,s.lastIndexOf("_"));
//		System.out.println(d);
		
	}
	
	private static Jedis jedis;
	
	public static Jedis getJedis(String url, int port, String auth){
		try {
			//连接本地的 Redis 服务
			jedis = new Jedis(url, port);
			
			//设置 redis 字符串数据  需要启动redis
			if(!StringUtils.isEmpty(auth)){
				jedis.auth(auth);//当redis设置密码后，需要先验证密码，才能进行下一步操作
			}
		} catch (Exception e) {
			System.out.println("Connection to server error");
			e.printStackTrace();
			return null;
		}
		System.out.println("Connection to server sucessfully");
		return jedis;
	}

	public static void r() {
		Jedis jedis = getJedis("192.168.120.98", 6379, "metUltrapass");
		
		Jedis me = getJedis("127.0.0.1", 6379, "metUltrapass");

		int sum = 1;
		
		Set<String> keys = jedis.keys("*");
		System.out.println(keys.size());
		Iterator<String> it=keys.iterator() ;   
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-5);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
        while(it.hasNext()){   
            String key = it.next(); 
            
            //满足11位手机号，过滤垃圾数据
            if(jedis.type(key).equals("list") && key.matches("^SN_\\d{11}$")){
            	String phone = key.substring(key.indexOf("_")+1);
            	
                 int i = 1;
            	//获取SN_手机号，中所有的消息编码，通过消息编码获取所有消息信息
            	for(String _no : jedis.lrange(key, 0, -1)){
            		
//            		if(jedis.ttl(_no)==-1){//过滤超时信息
//            			continue;
//            		}
            		
            		String d = _no.substring(_no.indexOf("_")+1,_no.lastIndexOf("_"));
            		
            		if(new Date(Long.parseLong(d+"000")).before(calendar.getTime())){
            			continue;
            		}
            		
            		Map<String, String> map = jedis.hgetAll(_no);
            		
            		if(map.isEmpty()){
            			continue;
            		}
            		
            		System.out.println("--------------------------------");
            		for (Map.Entry<String, String> m : map.entrySet()) {
            			System.out.println(m.getKey() + "----" + m.getValue());
            		}
            		
            		me.rpush("pushlists", _no+";"+map.get("tophone")
            					+";"+map.get("createtime")
            					+";"+map.get("msgtype")
            					+";您有一条应用消息");
            		me.rpush("SN_"+map.get("tophone"), _no);
            		me.hmset(_no, map);
            		
            		System.out.println("储存("+phone+")第"+i+"条消息信息");
            		System.out.println("--------------------------------");
            		sum++;
            		i++;
            	}
            }
        }
        
        System.out.println("共有"+sum+"条，有效消息记录");
	}

	public static void listTest() {
		Jedis jedis = RedisJava.getJedis("127.0.0.1", 6379, "metUltrapass");

		// jedis.lpush("list", "0_test");

		System.out.println(jedis.get("test"));

		System.out.println(jedis.llen("list"));
		for (String str : jedis.lrange("list", 0, -1)) {
			System.out.println(str);
		}

		for (Map.Entry<String, String> map : jedis.hgetAll("map").entrySet()) {
			System.out.println(map.getKey() + "----" + map.getValue());
		}
	}

	public static void hset() {
		Jedis jedis = RedisJava.getJedis("127.0.0.1", 6379, "metUltrapass");

		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		jedis.hmset("map", map);
	}
}
