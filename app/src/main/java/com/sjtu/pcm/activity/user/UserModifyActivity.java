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
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.entity.TagEntity;
import com.sjtu.pcm.entity.TagList;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserTagEntity;
import com.sjtu.pcm.entity.UserTagList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 修改个人信息类
 *
 *
 */
public class UserModifyActivity extends Activity {
	private UserTagList userTagList = null;
	private HashSet<String> userTag = new HashSet<>();

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
	private CheckBox mTag4;
	private CheckBox mTag5;
	private CheckBox mTag6;

	private MyApplication mApp;
	private UserEntity user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_modify);

		mApp = (MyApplication) getApplication();
		user = mApp.getUser();

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
		mTag4 = (CheckBox) findViewById(R.id.user_tag_4);
		mTag5 = (CheckBox) findViewById(R.id.user_tag_5);
		mTag6 = (CheckBox) findViewById(R.id.user_tag_6);
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
				new RMPHelper().execute(mApp.getUserUrl() + user.getId());
			}
		});
	}

	private void init() {
		// 初始化用户信息
		mName.setText(user.getName());
		if(user.getGender()!=null && user.getGender()== 1) {
			mGender.check(mFemale.getId());
		} else {
			mGender.check(mMale.getId());
		}

		mAddress.setText(user.getAddress());
		mMobile.setText(user.getMobile());

		new TagInitHelper().execute(mApp.getUserTagUrl() + "?User_tag.user_id=" + user.getId().toString());
	}

	class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String name = mName.getText().toString();

			RadioButton genderButton = (RadioButton) findViewById(mGender.getCheckedRadioButtonId());
			String gender = genderButton.getText().toString();

			String address = mAddress.getText().toString();
			String mobile = mMobile.getText().toString();

			user.setName(name);
			user.setMobile(mobile);
			user.setGender(gender.equals("男")? 0 :1);
			user.setAddress(address);

			String userStr = new Gson().toJson(user);
			HttpUtil.putRequest(uriAPI[0], userStr);

			//处理tag事务
			//删除以前的记录
			if (userTagList!= null && userTagList.getUser_tag()!= null
					&& userTagList.getUser_tag().size()> 0){
				for(int i = 0; i < userTagList.getUser_tag().size(); i++){
					HttpUtil.deletRequest(mApp.getUserTagUrl() + userTagList.getUser_tag().get(i).getId().toString());
				}
			}
			//添加新纪录
			String tagListResult = HttpUtil.getRequest(mApp.getTagUrl());
			if (tagListResult != null){
				Log.e("tagListResult", tagListResult);
				TagList tagList = new Gson().fromJson(tagListResult, TagList.class);
				Map<String, Long> contentToId = new HashMap<>();
				for (int i = 0; i < tagList.getTag().size(); i++){
					contentToId.put(tagList.getTag().get(i).getTag_content(), tagList.getTag().get(i).getId());
				}
				if(mTag1.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("1"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}
				if(mTag2.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("2"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}
				if(mTag3.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("3"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}
				if(mTag4.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("4"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}
				if(mTag5.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("5"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}
				if(mTag6.isChecked()){
					UserTagEntity userTagEntity = new UserTagEntity(user.getId(), contentToId.get("6"));
					String userTagStr = new Gson().toJson(userTagEntity);
					HttpUtil.postRequest(mApp.getUserTagUrl(), userTagStr);
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 更新User信息
			mApp.setUser(user);

			Toast.makeText(UserModifyActivity.this, "提交成功!", 2000).show();

			setResult(RESULT_OK);
			startActivity(new Intent(UserModifyActivity.this,
					MainActivity.class));
		}
	}

	class TagInitHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String result_array = HttpUtil.getRequest(uriAPI[0]);

			if (result_array != null){
				Log.e("result_array", result_array);
				userTagList = new Gson().fromJson(result_array, UserTagList.class);

				if (userTagList!= null && userTagList.getUser_tag()!= null
						&& userTagList.getUser_tag().size()> 0) {
					for (int i = 0; i < userTagList.getUser_tag().size(); i++){
						String userTagUri = mApp.getTagUrl() + userTagList.getUser_tag().get(i).getTag_id().toString();
						String userTagResult = HttpUtil.getRequest(userTagUri);
						if(userTagResult != null){
							TagEntity tagEntity = new Gson().fromJson(userTagResult, TagEntity.class);
							userTag.add(tagEntity.getTag_content());
						}
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			for(String tag : userTag){
				switch (tag){
					case "1":
						mTag1.setChecked(true);
						break;
					case "2":
						mTag2.setChecked(true);
						break;
					case "3":
						mTag3.setChecked(true);
						break;
					case "4":
						mTag4.setChecked(true);
						break;
					case "5":
						mTag5.setChecked(true);
						break;
					case "6":
						mTag6.setChecked(true);
						break;
				}
			}
		}
	}

}

