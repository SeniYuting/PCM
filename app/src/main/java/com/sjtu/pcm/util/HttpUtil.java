package com.sjtu.pcm.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HttpUtil {

    /**
     * 执行request请求
     *
     * @param url 请求的url地址
     * @return 返回请求的结果
     */
    public static String getRequest(String url) {
        HttpGet httpRequest = new HttpGet(url);
        String resp = "";
        try {
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);

            InputStream inputStream = httpResponse.getEntity()
                    .getContent();
            if (inputStream != null)
                resp = convertInputStreamToString(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static void postRequest(String url, String obj) {
        String uriAPI = url;
        HttpPost httpRequest = new HttpPost(uriAPI);
        httpRequest.setHeader("Content-type",
                "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            StringEntity entity = new StringEntity(obj, "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpRequest.setEntity(entity);
            HttpResponse res = httpClient.execute(httpRequest);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static void putRequest(String url, String obj) {
        String uriAPI = url;
        HttpPut httpRequest = new HttpPut(uriAPI);
        httpRequest.setHeader("Content-type",
                "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            StringEntity entity = new StringEntity(obj, "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpRequest.setEntity(entity);
            HttpResponse res = httpClient.execute(httpRequest);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws Exception {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
