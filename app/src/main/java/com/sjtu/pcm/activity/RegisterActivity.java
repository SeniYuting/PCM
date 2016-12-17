package com.sjtu.pcm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.Date;

/**
 * 注册界面
 *
 * 
 */
public class RegisterActivity extends Activity {

	// 登录按钮
	private Button register;
	private Button cancel;
	private EditText accountText;
	private EditText passwordText;
	private MyApplication mApp;

	private boolean isOK;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		mApp = (MyApplication) getApplication();
		isOK = false;

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
			public void onClick(View v) {
				new RMPHelper().execute(mApp.getUserUrl());
			}
		});

	}

	public void onBackPressed() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String account = accountText.getText().toString();
			String password = passwordText.getText().toString();

			Log.i("account", account);
			Log.i("password", password);

			// 检查账号是否已经注册过
			String result_array = HttpUtil.getRequest(uriAPI[0] + "?User.account=" + account);
			if (result_array != null){
				UserList userList = new Gson().fromJson(result_array, UserList.class);

				if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0) {
					isOK = false;
				} else {
					isOK = true;
					// 账号没有注册过
					UserEntity user = new UserEntity(account, password);
					Date t= new Date();
					user.setCreatedate(t.toString());
					String userStr = new Gson().toJson(user);
					HttpUtil.postRequest(uriAPI[0], userStr);

					startActivity(new Intent(RegisterActivity.this,
							LoginActivity.class));
					finish();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(isOK) {
				Toast.makeText(RegisterActivity.this, "注册成功!", 2000).show();
			} else {
				Toast.makeText(RegisterActivity.this, "账号已存在，请重新注册!", 2000).show();
			}

		}
	}
}
