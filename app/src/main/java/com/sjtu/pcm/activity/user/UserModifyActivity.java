package com.sjtu.pcm.activity.user;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.LoginActivity;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.activity.RegisterActivity;
import com.sjtu.pcm.menu.User;

/**
 * 修改个人信息类
 * 
 * @author CaoyYuting
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
public class UserModifyActivity extends Activity {
	private Button mCancel;
	private Button mSave;
	private EditText mName;
	private EditText mGender;
	private EditText mAddress;
	private EditText mMobile;
	
	private MyApplication mapp;
	private ArrayList<String> resultList = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_modify);
		
		mapp = (MyApplication) getApplication();

		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mCancel = (Button) findViewById(R.id.modify_cannel);
		mSave = (Button) findViewById(R.id.modify_save);
		mName = (EditText) findViewById(R.id.name);
		mGender = (EditText) findViewById(R.id.gender);
		mAddress = (EditText) findViewById(R.id.address);
		mMobile = (EditText) findViewById(R.id.mobile);
	}

	private void setListener() {
		mCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 显示退出对话框
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		mSave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 保存修改结果
				// 专门线程进行网络访问
				new Thread() {
					@Override
					public void run() {
						
						String name = mName.getText().toString();
						String gender = mGender.getText().toString();
						String address = mAddress.getText().toString();
						String mobile = mMobile.getText().toString();

						Log.i("name", name);
						Log.i("gender", gender);
						Log.i("address", address);
						Log.i("mobile", mobile);
				
						String uriAPI = "http://112.74.49.183:8080/Entity/U209f9ab73161d8/PCM/User/" + mapp.getUserId();
						HttpPut httpRequest = new HttpPut(uriAPI);
						httpRequest.setHeader("Content-type",
								"application/json");

						try {
							JSONObject obj = new JSONObject();
							obj.put("account", mapp.getAccount());
							obj.put("password", mapp.getPassword());
							obj.put("name", name);
							obj.put("gender", gender);
							obj.put("address", address);
							obj.put("mobile", mobile);
							
							// 中文乱码				
							httpRequest.setEntity(new StringEntity(obj
									.toString()));
							HttpResponse httpResponse = new DefaultHttpClient()
									.execute(httpRequest);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();

				setResult(RESULT_OK);
				
				startActivity(new Intent(UserModifyActivity.this,
						MainActivity.class));
				
//				finish(); // 直接关闭该页面
			}
		});
	}

	private void init() {
		// 初始化用户信息
		Log.i("user_id", mapp.getUserId());
        // 需要setText，必须在同一个thread中
		new RMPHelper().execute("http://112.74.49.183:8080/Entity/U209f9ab73161d8/PCM/User/" + mapp.getUserId());
		
	}
	
	class RMPHelper extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... uriAPI) {
			
			// 获取用户信息
			HttpGet httpRequest = new HttpGet(uriAPI[0]);
			String result = "";

			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);

				InputStream inputStream = httpResponse.getEntity()
						.getContent();
				if (inputStream != null)
					result = mapp.convertInputStreamToString(inputStream);

				Log.e("result", result);

				JSONObject result_json = JSONObject
						.fromObject(result);
				resultList.add(result_json.get("name").toString());
				resultList.add(result_json.get("gender").toString());
				resultList.add(result_json.get("address").toString());
				resultList.add(result_json.get("mobile").toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        
			mName.setText(resultList.size()>0 ? resultList.get(0) : "");
			mGender.setText(resultList.size()>1 ? resultList.get(1) : "");
			mAddress.setText(resultList.size()>2 ? resultList.get(2) : "");
			mMobile.setText(resultList.size()>3 ? resultList.get(3) : "");
		}
		
	}
}

