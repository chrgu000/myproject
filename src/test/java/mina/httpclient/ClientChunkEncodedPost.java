/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package mina.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Example how to use unbuffered chunk-encoded POST request.
 */
public class ClientChunkEncodedPost {

    public static void main(String[] args) throws Exception {
    	args = new String[]{"d:/1.txt"};
        if (args.length != 1)  {
            System.out.println("File path not given");
            System.exit(1);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost/s/test!client");
            
//            HttpParams params = new 
//            
//            httppost.setp
            
            File file = new File(args[0]);
           InputStream in = new FileInputStream(file);
           byte[] by = new byte[10];
           while(in.available()>0){
        	  in.read(by);
        	  System.out.println(new String(by,"GBK"));
           }
//            InputStreamEntity reqEntity = new InputStreamEntity(
//                    new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
//            reqEntity.setChunked(true);
            // It may be more appropriate to use FileEntity class in this particular
            // instance but we are using a more generic InputStreamEntity to demonstrate
            // the capability to stream out data from any arbitrary source
            //
             FileEntity fileEntity = new FileEntity(file, ContentType.create("plain/text", Consts.UTF_8));

//             List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//             formparams.add(new BasicNameValuePair("param1", "value1"));
//             formparams.add(new BasicNameValuePair("param2", "value2"));
//             UrlEncodedFormEntity fileEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            
//            StringEntity fileEntity = new StringEntity("important message你好",
//                    ContentType.create("plain/text", Consts.UTF_8));
//            fileEntity.setChunked(true);
//             fileEntity.setContentType("text/xml; charset=UTF-8");
            httppost.setEntity(fileEntity);
 
            System.out.println("Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                
                HttpEntity entity = response.getEntity();
                if (entity != null) {  
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    System.out.println("Response content: " + EntityUtils.toString(entity));  
                }  
                
                System.out.println("----------------------------------------");
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
