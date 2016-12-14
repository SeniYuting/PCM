package com.sjtu.pcm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.util.HttpUtil;

import org.json.JSONObject;

/**
 * 注册界面
 *
 * 
 */
@SuppressWarnings("deprecation")
public class RegisterActivity extends Activity {

	// 登录按钮
	private Button register;
	private Button cancel;
	private EditText accountText;
	private EditText passwordText;
	private MyApplication mapp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		mapp = (MyApplication) getApplication();

		findViewById();
		setListener();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		register = (Button) findViewById(R.id.register_activity_register);
		cancel = (Button) findViewById(R.id.register_activity_cancel);
		accountText = (EditText) findViewById(R.id.register_activity_account);
		passwordText = (EditText) findViewById(R.id.register_activity_password);
	}

	/**
	 * UI事件监听
	 */

	private void setListener() {
		// 取消按钮监听
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				startActivity(new Intent(RegisterActivity.this,
						LoginActivity.class));
				finish();

			}
		});
		// 注册按钮监听
		register.setOnClickListener(new OnClickListener() {
			@SuppressWarnings({ "unused" })
			public void onClick(View v) {

				// 专门线程进行网络访问
				new Thread() {
					@Override
					public void run() {
						String account = accountText.getText().toString();
						String password = passwordText.getText().toString();

						Log.i("account", account);
						Log.i("password", password);

						// 保存注册信息
						String uriAPI = mapp.getUserUrl();

						try {
//							JSONObject obj = new JSONObject();
//							obj.put("account", account);
////							obj.put("account","快啦");
//							obj.put("password", password);
//							HttpUtil.postRequest(uriAPI,obj);

							UserEntity user = new UserEntity();
							user.setAccount(account);
							user.setPassword(password);

							String userStr = new Gson().toJson(user);

							HttpUtil.postRequest(uriAPI,userStr);

							startActivity(new Intent(RegisterActivity.this,
									LoginActivity.class));
							finish();

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();

			}
		});

	}

	public void onBackPressed() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
