package com.sjtu.pcm.util;

import android.content.Intent;

import com.sjtu.pcm.activity.LoginActivity;
import com.sjtu.pcm.activity.RegisterActivity;

//import net.sf.json.JSONObject;
import org.json.JSONObject;
/**
 * Created by julia on 12/6/16.
 */

public class test {

//    net.sf.json.JSONObject jobj = new net.sf.json.JSONObject();
//    jobj.put("account", "hh");
//    jobj.put("password", "hh");
//    String responese = HttpUtil.postRequest(uriAPI, jobj);
//    System.out.println("response" + responese);
//    startActivity(new Intent(RegisterActivity.this,
//                  LoginActivity.class));
//    finish();
    static void main(){
        String uriAPI = "http://112.74.49.183:8080/Entity/U31b61aa24b0ae/PCM/User";
        JSONObject obj = new JSONObject();
//        obj.put("account", "hh");
//        obj.put("password", "hh");
//        String responese = HttpUtil.postRequest(uriAPI,obj);
//        System.out.println(responese);
    }
}
