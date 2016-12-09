package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
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

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.user.UserModifyActivity;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

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

	private MyApplication mapp;
	private OnOpenListener mOnOpenListener;

	private ArrayList<String> resultList = new ArrayList<>();

	@SuppressLint("InflateParams")
	public User(Context context, Activity activity) {
		mContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.user, null);

		mapp = (MyApplication) activity.getApplication();

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

		// 需要setText，必须在同一个thread中
		new RMPHelper()
				.execute(mapp.getProjectUrl() + "User/"
						+ mapp.getUserId());
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

	private class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			// 获取用户信息
			HttpGet httpRequest = new HttpGet(uriAPI[0]);
			String result = "";

			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);

				InputStream inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = mapp.convertInputStreamToString(inputStream);

				Log.e("user_result", result);

				JSONObject result_json = JSONObject.fromObject(result);
				resultList.add(result_json.get("account")==null ? "" : result_json.get("account")+"");
				resultList.add(result_json.get("name")==null ? "" : result_json.get("name")+"");
				resultList.add(result_json.get("gender")==null ? "" : result_json.get("gender")+"");
				resultList.add(result_json.get("address")==null ? "" : result_json.get("address")+"");
				resultList.add(result_json.get("mobile")==null ? "" : result_json.get("mobile")+"");
				// TODO 获取标签，并添加到resultList
				resultList.add("");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			mHead_Name.setText(resultList.get(0));
			mName.setText(resultList.get(1));
			mGender.setText(resultList.get(2));

			// 修改头像
			if(resultList.get(2).equals("男")) {
				mPortrait.setImageResource(R.drawable.portrait_1);
			} else if(resultList.get(2).equals("女")){
				mPortrait.setImageResource(R.drawable.portrait_2);
			}

			mAddress.setText(resultList.get(3));
			mMobile.setText(resultList.get(4));
			mTag.setText(resultList.get(5));
		}

	}

}
