package com.sjtu.pcm.util;

import net.sf.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


public class HttpUtil {

    /**
     * 执行request请求
     *
     * @param url 请求的url地址
     * @return 返回请求的结果
     */
    public static String getRequest(String url) {
//    public static JSONArray getRequest(String url, String objName) {
//		String resp = null;
//		GetMethod get = new GetMethod(url);
//		get.setRequestHeader("Content-type", "application/json");
//		HttpClient httpClient = new HttpClient();
//		try {
//			httpClient.executeMethod(get);
//			if (get.getStatusCode() == 200) {
//				resp = get.getResponseBodyAsString();
//			}
//			get.releaseConnection();
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return resp;

        JSONArray result_array = new JSONArray();
        HttpGet httpRequest = new HttpGet(url);
        String resp = "";
        try {
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);

            InputStream inputStream = httpResponse.getEntity()
                    .getContent();
            if (inputStream != null)
                resp = convertInputStreamToString(inputStream);
//            net.sf.json.JSONObject result_json = net.sf.json.JSONObject.fromObject(resp);
//            result_array = result_json
//                    .getJSONArray(objName);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
//        return result_array.toString();
    }

    /**
     * 执行post请求
     *
     * @param url 请求的url地址
     * @return 返回请求的结果
     */
    public static void postRequest(String url, JSONObject obj) {
        String uriAPI = url;
        HttpPost httpRequest = new HttpPost(uriAPI);
        httpRequest.setHeader("Content-type",
                "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            StringEntity entity = new StringEntity(obj.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpRequest.setEntity(entity);
            HttpResponse res = httpClient.execute(httpRequest);
            ;
			String responseContent = null; // 响应内容
			HttpEntity httpEntity = res.getEntity();
//			responseContent = EntityUtils.toString(httpEntity, "UTF-8");
//            JsonParser jsonparer = new MyJsonParser();
//            JSONObject json = jsonparer.parse(responseContent)
//                    .getAsJsonObject();
//            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                if (json.get("errcode") != null)
//                //resultStr = json.get("errcode").getAsString();
//            } else {// 正常情况下
//                resultStr = json.get("access_token").getAsString();
//            }
//        }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
//			return httpResponse.toString();
        }
//    return resultStr;
    }


    /**
     * 执行post请求
     *
     * @param url 请求的url地址
     * @return 返回请求的结果
     */
    public static void putRequest(String url, JSONObject obj) {
        String uriAPI = url;
        HttpPut httpRequest = new HttpPut(uriAPI);
        httpRequest.setHeader("Content-type",
                "application/json");

        try {
            StringEntity entity = new StringEntity(obj.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpRequest.setEntity(entity);
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
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


    public static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return "UTF-8";
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
