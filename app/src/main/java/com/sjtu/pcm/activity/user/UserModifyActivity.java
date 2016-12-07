package com.sjtu.pcm.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 修改个人信息类
 *
 *
 */
@SuppressWarnings({ "unused", "deprecation" })
public class UserModifyActivity extends Activity {
	private Button mCancel;
	private Button mSave;
	private EditText mName;
	private RadioGroup mGender;
	private RadioButton mMale;
	private RadioButton mFemale;
	private EditText mAddress;
	private EditText mMobile;
	private CheckBox mTag1;
	private CheckBox mTag2;
	private CheckBox mTag3;

	private MyApplication mApp;
	private ArrayList<String> resultList = new ArrayList<>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_modify);

		mApp = (MyApplication) getApplication();

		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mCancel = (Button) findViewById(R.id.modify_cannel);
		mSave = (Button) findViewById(R.id.modify_save);
		mName = (EditText) findViewById(R.id.name);
		mGender = (RadioGroup) findViewById(R.id.gender);
		mMale = (RadioButton) findViewById(R.id.male);
		mFemale = (RadioButton) findViewById(R.id.female);
		mAddress = (EditText) findViewById(R.id.address);
		mMobile = (EditText) findViewById(R.id.mobile);
		mTag1 = (CheckBox) findViewById(R.id.user_tag_1);
		mTag2 = (CheckBox) findViewById(R.id.user_tag_2);
		mTag3 = (CheckBox) findViewById(R.id.user_tag_3);

		// TODO tag相关
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

						RadioButton genderButton = (RadioButton) findViewById(mGender.getCheckedRadioButtonId());
						String gender = genderButton.getText().toString();

						String address = mAddress.getText().toString();
						String mobile = mMobile.getText().toString();

						Log.i("name", name);
						Log.i("gender", gender);
						Log.i("address", address);
						Log.i("mobile", mobile);

						Log.i("url", mApp.getProjectUrl());

						String uriAPI = mApp.getProjectUrl() + "User/" + mApp.getUserId();
						HttpPut httpRequest = new HttpPut(uriAPI);
						httpRequest.setHeader("Content-type",
								"application/json");

						try {
							JSONObject obj = new JSONObject();
							obj.put("account", mApp.getAccount());
							obj.put("password", mApp.getPassword());
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
		Log.i("user_id", mApp.getUserId());
        // 需要setText，必须在同一个thread中
		new RMPHelper().execute(mApp.getProjectUrl() + "User/" + mApp.getUserId());

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
					result = mApp.convertInputStreamToString(inputStream);

				Log.e("result", result);

				JSONObject result_json = JSONObject
						.fromObject(result);
				resultList.add(result_json.get("name")==null ? "" : result_json.get("name")+"");
				resultList.add(result_json.get("gender")==null ? "" : result_json.get("gender")+"");
				resultList.add(result_json.get("address")==null ? "" : result_json.get("address")+"");
				resultList.add(result_json.get("mobile")==null ? "" : result_json.get("mobile")+"");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);

			mName.setText(resultList.size()>0 ? resultList.get(0) : "");
			if(resultList.size()>1) {
				String gender = resultList.get(1);
				if(gender.equals("女")) {
					mGender.check(mFemale.getId());
				} else {
					mGender.check(mMale.getId());
				}
			}
			mAddress.setText(resultList.size()>2 ? resultList.get(2) : "");
			mMobile.setText(resultList.size()>3 ? resultList.get(3) : "");
		}

	}
}

