package com.sjtu.pcm.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.user.UserModifyActivity;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.TagEntity;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserTagList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.HashSet;

/**
 * 用户信息类
 *
 * 
 */
@SuppressWarnings("deprecation")
public class User {
	private UserTagList userTagList = null;
	private HashSet<String> userTagDescription = new HashSet<>();
	private Context mContext;

	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private Button mRefresh;
	private Button mUserWrite;
	private TextView mHead_Name;
	private TextView mHead_Declaration;
	private ImageView mPortrait;

	private TextView mName;
	private TextView mGender;
	private TextView mAddress;
	private TextView mMobile;
	private TextView mTag;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	public User(Context context, Activity activity) {
		mContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.user, null);

		mApp = (MyApplication) activity.getApplication();

		findViewById();
		setListener();
		init();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.user_info_menu);
		mUserWrite = (Button) mHome.findViewById(R.id.user_write);
		mRefresh = (Button) mHome.findViewById(R.id.user_refresh);

		mHead_Name = (TextView) mHome.findViewById(R.id.user_info_head_name);
		mHead_Declaration = (TextView) mHome.findViewById(R.id.declaration);
		mPortrait = (ImageView) mHome.findViewById(R.id.user_info_portrait);

		mName = (TextView) mHome.findViewById(R.id.user_name);
		mGender = (TextView) mHome.findViewById(R.id.user_gender);
		mAddress = (TextView) mHome.findViewById(R.id.user_address);
		mMobile = (TextView) mHome.findViewById(R.id.user_mobile);
		mTag = (TextView) mHome.findViewById(R.id.user_tag);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		mMenu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});

		// 刷新监听
		mRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 暂不处理
			}
		});

		// 编辑监听
		mUserWrite.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到编辑界面
				Intent intent = new Intent();
				intent.setClass(mContext, UserModifyActivity.class);
				mContext.startActivity(intent);
			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mHead_Declaration.setText("welcome!");

		UserEntity user = mApp.getUser();

		mHead_Name.setText(user.getAccount());
		mName.setText(user.getName());
		mGender.setText(user.getGender()==null ? "" : (user.getGender()==0 ? "男" : "女"));

		// 头像
		if(user.getGender()!=null) {
			if(user.getGender()== 0) {
				mPortrait.setImageResource(R.drawable.portrait_1);
			} else if(user.getGender()== 1){
				mPortrait.setImageResource(R.drawable.portrait_2);
			}
		} else {
			mPortrait.setImageResource(R.drawable.portrait_3);
		}

		mAddress.setText(user.getAddress());
		mMobile.setText(user.getMobile());

		new TagInitHelper().execute(mApp.getUserTagUrl() + "?User_tag.user_id=" + mApp.getUser().getId().toString());
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	/**
	 * 获取界面
	 * 
	 */
	public View getView() {
		return mHome;
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
							userTagDescription.add(tagEntity.getTag_description());
						}
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			String tag = "";
			for(String tagDescription : userTagDescription){
				tag = tag + tagDescription + ",";
			}
			if(tag.length() > 0)
				tag = tag.substring(0, tag.length()-1);
			mTag.setText(tag);
		}
	}
}
