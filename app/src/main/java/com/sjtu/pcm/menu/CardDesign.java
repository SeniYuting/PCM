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
import android.widget.TextView;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.card_design.CardModifyActivity;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;

import java.util.ArrayList;

/**
 * 名片设计类
 *
 * 
 */
public class CardDesign {
	private Context cDContext;
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	//private Button cDRefresh;
	private Button cDEdit;

	private TextView cName;
	private TextView cCompany;
	private TextView cJob;
	private TextView cCNumber;
	private TextView cCAddress;
	private TextView cFax;
	private TextView cCEmail;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	private ArrayList<String> resultList = new ArrayList<>();

	@SuppressLint("InflateParams")
	public CardDesign(Context context, Activity activity) {
		cDContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.carddesign, null);

		mApp = (MyApplication) activity.getApplication();
		Log.i("user_id", mApp.getUserId());

		findViewById();
		setListener();
		init();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.menu);
		cDEdit = (Button) mHome.findViewById(R.id.card_edit);
		mTopText = (TextView) mHome.findViewById(R.id.top_text);
		cName = (TextView) mHome.findViewById(R.id.card_name);
		cCompany = (TextView) mHome.findViewById(R.id.card_company);
		cJob = (TextView) mHome.findViewById(R.id.card_job);
		cCNumber = (TextView) mHome.findViewById(R.id.card_c_number);
		cCAddress = (TextView) mHome.findViewById(R.id.card_c_address);
		cFax = (TextView) mHome.findViewById(R.id.card_fax);
		cCEmail = (TextView) mHome.findViewById(R.id.card_c_email);
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

		// 编辑监听
		cDEdit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到编辑界面
				Intent intent = new Intent();
				intent.setClass(cDContext, CardModifyActivity.class);
				cDContext.startActivity(intent);
			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片设计");

		new RMPHelper()
				.execute();
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

			// 获取用户名片信息 TODO
//			HttpGet httpRequest = new HttpGet(uriAPI[0]);
//			String result = "";

			try {
//				HttpResponse httpResponse = new DefaultHttpClient()
//						.execute(httpRequest);
//
//				InputStream inputStream = httpResponse.getEntity().getContent();
//				if (inputStream != null)
//					result = mApp.convertInputStreamToString(inputStream);
//
//				Log.e("user_result", result);
//
//				JSONObject result_json = JSONObject.fromObject(result);
//				resultList.add(result_json.get("account").toString());
//				resultList.add(result_json.get("name").toString());
//				resultList.add(result_json.get("gender").toString());
//				resultList.add(result_json.get("address").toString());
//				resultList.add(result_json.get("mobile").toString());

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			cName.setText(resultList.size()>0 ? resultList.get(0) : "");
			cCompany.setText(resultList.size()>1 ? resultList.get(1) : "");
			cJob.setText(resultList.size()>2 ? resultList.get(2) : "");
			cCNumber.setText(resultList.size()>3 ? resultList.get(3) : "");
			cCAddress.setText(resultList.size()>4 ? resultList.get(4) : "");
			cFax.setText(resultList.size()>5 ? resultList.get(5) : "");
			cCEmail.setText(resultList.size()>6 ? resultList.get(6) : "");
		}

	}

}
