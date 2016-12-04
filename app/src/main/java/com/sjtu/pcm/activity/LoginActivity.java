package com.sjtu.pcm.activity;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登录界面
 *
 * 
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {

	// 登录
	private Button mLogin;
	private EditText accountText;
	private EditText passwordText;
	private TextView register;

	private MyApplication mapp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mapp = (MyApplication) getApplication();

		findViewById();
		setListener();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mLogin = (Button) findViewById(R.id.login_activity_login);
		accountText = (EditText) findViewById(R.id.login_activity_account);
		passwordText = (EditText) findViewById(R.id.login_activity_password);
		register = (TextView) findViewById(R.id.login_activity_register);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		// 登录按钮监听
		mLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 专门线程进行网络访问
				new Thread() {

					@Override
					public void run() {
						String account = accountText.getText().toString();
						String password = passwordText.getText().toString();

						// 获取用户信息
						String uriAPI = "http://112.74.49.183:8080/Entity/U209f9ab73161d8/PCM/User/?User.account="
								+ account;
						HttpGet httpRequest = new HttpGet(uriAPI);
						String result = "";

						try {
							HttpResponse httpResponse = new DefaultHttpClient()
									.execute(httpRequest);

							InputStream inputStream = httpResponse.getEntity()
									.getContent();
							if (inputStream != null)
								result = mapp
										.convertInputStreamToString(inputStream);

							Log.e("result", result);

							JSONObject result_json = JSONObject
									.fromObject(result);
							JSONArray result_array = result_json
									.getJSONArray("User");

							String result_password = (String) result_array
									.getJSONObject(0).get("password");

							if (result_password.equals(password)) {

								String user_id = result_array.getJSONObject(0)
										.get("id") + "";
								mapp.setUserId(user_id);
								mapp.setAccount(account);
								mapp.setPassword(password); // 应加密

								startActivity(new Intent(LoginActivity.this,
										MainActivity.class));

								finish();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
		// 注册按钮监听
		register.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
				finish();

			}
		});
	}

	public void onBackPressed() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
