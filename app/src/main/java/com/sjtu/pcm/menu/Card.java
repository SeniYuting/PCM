package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sjtu.pcm.R;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;

/**
 * 名片管理类
 * 
 * @author CaoyYuting
 * 
 */
public class Card {
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private Button mCardDesign;
	private Button mCardChange;

	private OnOpenListener mOnOpenListener;

	private TextView mTopText;
	
	// 是否是名片设计
	private boolean mIsCardDesign = true;

	@SuppressLint("InflateParams")
	public Card(Context context, Activity activity) {
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.card, null);

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
		
		mCardDesign = (Button) mHome.findViewById(R.id.card_design);
		mCardChange = (Button) mHome.findViewById(R.id.card_change);
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
		
		mCardDesign.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!mIsCardDesign) {
					mIsCardDesign = true;
					mCardDesign
							.setBackgroundResource(R.drawable.bottomtabbutton_leftred);
					mCardChange
							.setBackgroundResource(R.drawable.bottomtabbutton_rightwhite);

				}
			}
		});
		mCardChange.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mIsCardDesign) {
					mIsCardDesign = false;
					mCardDesign
							.setBackgroundResource(R.drawable.bottomtabbutton_leftwhite);
					mCardChange
							.setBackgroundResource(R.drawable.bottomtabbutton_rightred);
				}
			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片管理");
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
