package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;

/**
 * 名片交换类
 *
 *
 */
public class CardEXChange {
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;

	private MyApplication mapp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	@SuppressLint("InflateParams")
	public CardEXChange(Context context, Activity activity) {
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.cardexchange, null);

		mapp = (MyApplication) activity.getApplication();
		Log.i("user_id", mapp.getUserId());

		findViewById();
		setListener();
		init();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.menu);
		mTopText = (TextView) mHome.findViewById(R.id.top_text);
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
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片交换");
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
