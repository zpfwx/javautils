package com.myutil.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
//import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description: Restful HttpRequest Util
 * @Author: System
 * @Date: 2020/5/11
 **/
@Slf4j
public class HttpRequestUtil {


    private volatile static CloseableHttpClient httpClient = null;

    static {
        if (httpClient ==null){
            synchronized (CloseableHttpClient.class){
                if(httpClient ==null){
                    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
                    connectionManager.setDefaultMaxPerRoute(100);
                    connectionManager.setMaxTotal(500);
                    httpClient = HttpClients.custom()
                            .setConnectionManager(connectionManager)
                            .setConnectionManagerShared(true)
                            .build();
                }
            }
        }
    }

    /**
     * 原生字符串发送get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String doGet(String url, String token) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("DataEncoding", "UTF-8");
        httpGet.setHeader("X-Token", token);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(2000).setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("get request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("httpResponse close failed", e);
                }
            }
        }
        return null;
    }

    public static String doGetBak(String url, String token) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("DataEncoding", "UTF-8");
        httpGet.setHeader("X-Token", token);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("get request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("httpResponse close failed", e);
                }
            }
        }
        return null;
    }

    public static String doPostBak(String url, String jsonStr, Map<String,String> header) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(2000).setSocketTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.setHeader("DataEncoding", "UTF-8");
        if(header != null){
        	Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
        	while(iterator.hasNext()){
        		Entry<String, String> next = iterator.next();
        		httpPost.setHeader(next.getKey(), next.getValue());
        	}
        }
//        httpPost.setHeader("token", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("post request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("httpResponse close failed", e);
                }
            }
        }
        return null;
    }

    public static String tryPostWithTimes(String url, String jsonStr, Map<String,String> header, int times){
        while(times-- >0){
            String result = doPost(url, jsonStr, header);
            if(result != null){
                return result;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.warn("error happened when tryPostWithTimes sleep:{}", e.getMessage());
            }
        }
        return null;
    }

    public static String doPost(String url, String jsonStr, Map<String,String> header) {
        HttpPost httpPost = new HttpPost(url);
        //ConnectionRequestTimeout  httpclient 从连接池获取连接的时间
        //ConnectTimeout            建立连接的时间
        //SocketTimeout             数据包传输的时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(2000).setSocketTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.setHeader("DataEncoding", "UTF-8");
        if(header != null){
            Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String, String> next = iterator.next();
                httpPost.setHeader(next.getKey(), next.getValue());
            }
        }
//        httpPost.setHeader("token", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr, ContentType.APPLICATION_JSON));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("post request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("httpResponse close failed", e);
                }
            }
        }
        return null;
    }

    /**
     * 原生字符串发送post请求
     * 
     * @param url
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static String doPost(String url, String jsonStr) { 
    	return doPost(url, jsonStr, null);
    }

    /**
     * 原生字符串发送put请求
     * 
     * @param url
     * @param token
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static String doPut(String url, String token, String jsonStr) {

        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("DataEncoding", "UTF-8");
        httpPut.setHeader("X-Token", token);
        
        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("put request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 原生字符串发送put请求
     *
     * @param url
     * @param token
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static String doPut(String url,String tokenKey,String token, String jsonStr) {

        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("DataEncoding", "UTF-8");
        httpPut.setHeader("token", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            log.error("put request failed", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}