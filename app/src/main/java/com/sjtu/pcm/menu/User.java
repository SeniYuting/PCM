package com.sjtu.pcm.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.user.UserModifyActivity;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.UserEntity;

/**
 * 用户信息类
 *
 * 
 */
@SuppressWarnings("deprecation")
public class User {
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
		mGender.setText(user.getGender().toString());

		// 修改头像
		if(user.getGender()== 0) {
			mPortrait.setImageResource(R.drawable.portrait_1);
		} else if(user.getGender()== 1){
			mPortrait.setImageResource(R.drawable.portrait_2);
		}

		mAddress.setText(user.getAddress());
		mMobile.setText(user.getMobile());
		mTag.setText("tag");
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
}
