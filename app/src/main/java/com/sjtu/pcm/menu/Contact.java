package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.CommentEntity;
import com.sjtu.pcm.util.HttpUtil;

/**
 * 联系我们类
 *
 * 
 */
@SuppressWarnings("ALL")
public class Contact {
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private RadioGroup mStar;
	private EditText mComment;
	private Button mSubmit;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	@SuppressLint("InflateParams")
	public Contact(Context context, Activity activity) {
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.contact, null);

		mApp = (MyApplication) activity.getApplication();

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

		mStar = (RadioGroup) mHome.findViewById(R.id.contact_star);
		mComment = (EditText) mHome.findViewById(R.id.contact_comment);
		mSubmit = (Button) mHome.findViewById(R.id.contact_submit);
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

		mSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// 专门线程进行网络访问
				new Thread() {
					@Override
					public void run() {

						RadioButton starButton = (RadioButton) mHome.findViewById(mStar.getCheckedRadioButtonId());
						String star = starButton.getText().toString();
						int starNum = -1;
						switch(star) {
							case "优":
								starNum = 0;
								break;
							case "中":
								starNum = 1;
								break;
							case "差":
								starNum = 2;
								break;
							default:
						}

						String content = mComment.getText().toString();

						Log.i("user_id", mApp.getUser().getId() + "");
						Log.i("star", star);
						Log.i("content", content);

						String uriAPI = mApp.getCommentUrl();
						try {

							CommentEntity comment = new CommentEntity(mApp.getUser().getId(), starNum+"", content);
							String commentStr = new Gson().toJson(comment);
							HttpUtil.postRequest(uriAPI, commentStr);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();

			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("联系我们");
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
