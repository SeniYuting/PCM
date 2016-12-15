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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.schedule.HistorySchedule;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.ScheduleEntity;
import com.sjtu.pcm.entity.UserList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.ArrayList;

/**
 * 人脉排程类
 *
 * 
 */
@SuppressWarnings("ALL")
public class Schedule {
	private Context sContext;
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private Button mHistory;

	private Spinner mPartner;
	private DatePicker mDate;
	private EditText mPlace;
	private EditText mTopic;
	private EditText mUNote;
	private EditText mPNote;
	private Button mSubmit;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	ArrayList<String> friend_list = new ArrayList<>();

	@SuppressLint("InflateParams")
	public Schedule(Context context, Activity activity) {
		sContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.schedule, null);

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
		mHistory = (Button) mHome.findViewById(R.id.schedule_history);

		mPartner = (Spinner) mHome.findViewById(R.id.schedule_friend);
		mDate = (DatePicker) mHome.findViewById(R.id.schedule_date);
		mPlace = (EditText) mHome.findViewById(R.id.schedule_place);
		mTopic = (EditText) mHome.findViewById(R.id.schedule_topic);
		mUNote = (EditText) mHome.findViewById(R.id.schedule_unote);
		mPNote = (EditText) mHome.findViewById(R.id.schedule_pnote);
		mSubmit = (Button) mHome.findViewById(R.id.schedule_submit);
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

		mHistory.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到历史排程界面
				Intent intent = new Intent();
				intent.setClass(sContext, HistorySchedule.class);
				sContext.startActivity(intent);
			}
		});

		mSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				new RMPHelper().execute(mApp.getScheduleUrl());
			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("人脉排程");

		new RMPUserHelper().execute(mApp.getUserUrl());

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

	class RMPUserHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			Log.e("url", uriAPI[0]);

			// TODO 改为获取partner信息

			String result_array = HttpUtil.getRequest(uriAPI[0]);
			if (result_array != null){
				UserList userList = new Gson().fromJson(result_array, UserList.class);

				if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0) {

					for(int i=0; i<userList.getUser().size(); i++) {
						friend_list.add(userList.getUser().get(i).getAccount());
					}

				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//适配器
			ArrayAdapter arr_adapter= new ArrayAdapter<>(sContext, android.R.layout.simple_spinner_item, friend_list);
			//设置样式
			arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//加载适配器
			mPartner.setAdapter(arr_adapter);
		}
	}

	class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String partner = (String)mPartner.getSelectedItem();
			String date = mDate.getYear() + "-" + mDate.getMonth() + "-" + mDate.getDayOfMonth();
			String place = mPlace.getText().toString();
			String topic = mTopic.getText().toString();
			String uNote = mUNote.getText().toString();
			String pNote = mPNote.getText().toString();

			Log.i("user_id", mApp.getUser().getId() + "");
			Log.i("partner", partner);
			Log.i("date", date);
			Log.i("place", place);
			Log.i("topic", topic);
			Log.i("uNote", uNote);
			Log.i("pNote", pNote);

			// 保存人脉排程信息
			// TODO 修改partner_id
			ScheduleEntity schedule = new ScheduleEntity(mApp.getUser().getId(), mApp.getUser().getId(), date, place, topic, uNote, pNote);
			String scheduleStr = new Gson().toJson(schedule);
			HttpUtil.postRequest(uriAPI[0], scheduleStr);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Toast.makeText(sContext, "提交成功!", 2000).show();
		}
	}

}
