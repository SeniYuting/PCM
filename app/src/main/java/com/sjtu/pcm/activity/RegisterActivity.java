package com.sjtu.pcm.activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sjtu.pcm.R;

/**
 * 注册界面
 * 
 * @author CaoyYuting
 * 
 */
@SuppressWarnings("deprecation")
public class RegisterActivity extends Activity {

	// 登录按钮
	private Button register;
	private Button cancel;
	private EditText accountText;
	private EditText passwordText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

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
						String uriAPI = "http://112.74.49.183:8080/Entity/U209f9ab73161d8/PCM/User/";
						HttpPost httpRequest = new HttpPost(uriAPI);
						httpRequest.setHeader("Content-type",
								"application/json");

						try {
							JSONObject obj = new JSONObject();
							obj.put("account", account);
							obj.put("password", password);
							httpRequest.setEntity(new StringEntity(obj
									.toString()));
							HttpResponse httpResponse = new DefaultHttpClient()
									.execute(httpRequest);

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
