package com.zzyycc.management.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className HttpUtils
 * @createTime 2022/4/8 17:39
 * @description
 */
public class HttpUtils {



    //无参方式
    public void get(String url) {
        getWithParams(url, new HashMap<>());
    }

    //有参方式
    public void getWithParams(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            // 创建Get请求
            url = joinParam(url, params);
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(2000) //服务器响应超时时间
                    .setConnectTimeout(2000) //连接服务器超时时间
                    .build();
            httpGet.setConfig(requestConfig);
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String joinParam(String url, Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");

        int counter = 0;
        for (Map.Entry<String,Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key == null) {
                continue;
            }

            if (counter == 0) {
                urlBuilder.append(key).append("=").append(value);
            } else {
                urlBuilder.append("&").append(key).append("=").append(value);
            }
            counter++;
        }

        return urlBuilder.toString();
    }



    /**
     * 发送post请求
     * @param url
     * @param soapRequestXml
     * @return
     * @throws Exception
     */
    public static String sendPostSoapXml(String url, String soapRequestXml) throws Exception {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000)
                .build();
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = null;

        try {
            //设置post请求方式为SOAP+XML
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            StringEntity data = new StringEntity(soapRequestXml, Charset.forName("UTF-8"));
            httpPost.setEntity(data);

            //发送post请求
            response = closeableHttpClient.execute(httpPost);
            if (response == null || response.getStatusLine() == null) {
                throw new RuntimeException("发送POST请求失败，返回结果为空！");
            }

            //返回状态是否200
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                //得到请求结果
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    String responseData = EntityUtils.toString(entityRes, "UTF-8");
                    return responseData;
                }
            } else {
                throw new Exception("发送POST-SOAP+XML请求出错：" + response);
            }
        } catch (Exception e) {
            throw new Exception("发送POST请求出现异常,"+e.getMessage());
        } finally {
            try {
                // 关闭连接释放资源
                if (response != null) {
                    response.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }


    /**
     * 发送post请求
     * @param url
     * @param requestJson
     * @return
     * @throws Exception
     */
    public static String sendPostJson(String url, String requestJson) throws Exception {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000)
                .build();
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = null;

        try {
            //设置post请求方式为JSON
            httpPost.addHeader("Authorization", "Basic" + encryptBASE64("username", "password"));
            StringEntity data = new StringEntity(requestJson, "UTF-8");
            data.setContentType("application/json; charset=UTF-8");
            httpPost.setEntity(data);

            //发送post请求
            response = closeableHttpClient.execute(httpPost);
            if (response == null || response.getStatusLine() == null) {
                throw new Exception("发送POST请求失败，返回结果为空！");
            }

            //返回状态是否200
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                //得到请求结果
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    String responsetData = EntityUtils.toString(entityRes, "UTF-8");
                    return responsetData;
                }
            } else {
                throw new Exception("发送POST-JSON请求出错：" + response);
            }
        } catch (Exception e) {
            throw new Exception("发送POST请求出现异常,"+e.getMessage());
        } finally {
            try {
                // 关闭连接释放资源
                if (response != null) {
                    response.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static String encryptBASE64(String username, String password) {
        byte[] key = (username+":"+password).getBytes();
        return  new String(Base64.encodeBase64(key));
    }

}
