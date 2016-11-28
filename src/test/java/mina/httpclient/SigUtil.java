package mina.httpclient;
 import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * sig加密
 * @author zhaochen  2013-8-22 上午09:51:01
 *
 */
public class SigUtil {
	private static Logger logger = Logger.getLogger(SigUtil.class);
	public static String getSig(List<String> paramList,String client_secret) { 
		String sigStr = "";
		Collections.sort(paramList);
		StringBuffer bf = new StringBuffer();
		for (String param : paramList) {
			bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在�?��
		}
		bf.append(client_secret); // 符串末尾追加上应用的Secret Key
		logger.info("sigStr:"+bf.toString());
		sigStr = DigestUtils.md5Hex(bf.toString());

		return sigStr;
    } 
	/**
	 * 手机端签名算�?
	 * @param params
	 * @return
	 * @author zhaochen  2014-10-13 下午03:28:09
	 */
	public static String getSig(String client_secret,String... params){
		StringBuffer strBuffer = new StringBuffer();
		if(params!=null){
			List<String> list = new ArrayList<String>();
			for(String s:params){
				list.add(s);
			}
			Collections.sort(list);
			StringBuffer bf = new StringBuffer();
			for (String param : list) {
				bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在�?��
			}
			bf.append(client_secret);
			
			try {
				logger.info(URLEncoder.encode(bf.toString(),""));
				strBuffer.append(DigestUtils.md5Hex(URLEncoder.encode(bf.toString(),"")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info(strBuffer.toString());
		return strBuffer.toString();
	}
	public static String getSig(List<String> paramList) { 
		String sigStr = "";
		Collections.sort(paramList);
		StringBuffer bf = new StringBuffer();
		for (String param : paramList) {
			bf.append(param); // 将参数键值对，以字典序升序排列后，拼接在�?��
		}
		sigStr = DigestUtils.md5Hex(bf.toString());
		return sigStr;
    } 	
	public static String getSig(String str) { 
		String sigStr = "";
		sigStr = DigestUtils.md5Hex(str);
		return sigStr;
    } 
}

