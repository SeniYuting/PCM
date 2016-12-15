package com.sjtu.pcm.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.util.HttpUtil;

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

						UserEntity user = mApp.getUser();

						String uriAPI = mApp.getUserUrl() + user.getId();

						Log.e("uriAPI", uriAPI);

						user.setName(name);
						user.setMobile(mobile);
						user.setGender(gender.equals("男")? 0 :1);
						user.setAddress(address);

						String userStr = new Gson().toJson(user);

						Log.e("userStr", userStr);

						HttpUtil.putRequest(uriAPI, userStr);

						// 更新User信息
						mApp.setUser(user);

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
		Log.i("user_id", mApp.getUser().getId() + "");

		UserEntity user = mApp.getUser();

		mName.setText(user.getName());
		if(user.getGender()== 0) {
			mGender.check(mFemale.getId());
		} else {
			mGender.check(mMale.getId());
		}

		mAddress.setText(user.getAddress());
		mMobile.setText(user.getMobile());
	}

}

