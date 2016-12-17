package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
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
import com.sjtu.pcm.entity.CardExchangeEntity;
import com.sjtu.pcm.entity.CardExchangeList;
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
	private TextView mNofriend;
	private DatePicker mDate;
	private EditText mPlace;
	private EditText mTopic;
	private EditText mUNote;
	private EditText mPNote;
	private Button mSubmit;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	ArrayList<String> friend_list = new ArrayList<>();  // 好友姓名信息
	ArrayList<Long> friend_id_list = new ArrayList<>();  // 好友ID信息

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
		mNofriend = (TextView) mHome.findViewById(R.id.schedule_no_friend);
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

		new RMPPartnerHelper().execute(mApp.getCardExchangeUrl() + "?Card_exchange.send_user_id=" + mApp.getUser().getId(),
				mApp.getCardExchangeUrl() + "?Card_exchange.receive_user_id=" + mApp.getUser().getId(),
				mApp.getUserUrl());

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

	class RMPPartnerHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String receive_array = HttpUtil.getRequest(uriAPI[0]);
			if (receive_array != null){
				CardExchangeList ceList1 = new Gson().fromJson(receive_array, CardExchangeList.class);

				if (ceList1!= null && ceList1.getCard_exchange()!= null && ceList1.getCard_exchange().size()> 0) {

					for(int i=0; i<ceList1.getCard_exchange().size(); i++) {

						CardExchangeEntity ceEntity1 = ceList1.getCard_exchange().get(i);
						Long partner_id1 = ceEntity1.getReceive_user_id();

						if(!friend_id_list.contains(partner_id1)) {

							// 获得好友姓名
							String result_array1 = HttpUtil.getRequest(uriAPI[2] + "?User.id=" + partner_id1);
							if (result_array1 != null){
								UserList userList = new Gson().fromJson(result_array1, UserList.class);
								if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0) {
									friend_id_list.add(partner_id1);
									friend_list.add(userList.getUser().get(0).getAccount());
								}
							}

						}
					}

				}
			}

			String send_array = HttpUtil.getRequest(uriAPI[1]);
			if (send_array != null){
				CardExchangeList ceList2 = new Gson().fromJson(send_array, CardExchangeList.class);

				if (ceList2!= null && ceList2.getCard_exchange()!= null && ceList2.getCard_exchange().size()> 0) {

					for(int i=0; i<ceList2.getCard_exchange().size(); i++) {

						CardExchangeEntity ceEntity2 = ceList2.getCard_exchange().get(i);
						Long partner_id2 = ceEntity2.getReceive_user_id();

						if(!friend_id_list.contains(partner_id2)) {

							// 获得好友姓名
							String result_array2 = HttpUtil.getRequest(uriAPI[2] + "?User.id=" + partner_id2);
							if (result_array2 != null){
								UserList userList = new Gson().fromJson(result_array2, UserList.class);
								if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0) {
									friend_id_list.add(partner_id2);
									friend_list.add(userList.getUser().get(0).getAccount());
								}
							}

						}
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

			if(friend_id_list.size()==0) {
				// 没有好友，不能进行人脉排程
				mPartner.setVisibility(View.INVISIBLE);
				mNofriend.setVisibility(View.VISIBLE);
				mSubmit.setClickable(false);
				mSubmit.setBackgroundColor(Color.parseColor("#B0B0B0"));
			}
		}
	}

	class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

//			String partner = (String)mPartner.getSelectedItem();
			Long partner_id = friend_id_list.get(mPartner.getSelectedItemPosition());
			String date = mDate.getYear() + "-" + (mDate.getMonth() + 1) + "-" + mDate.getDayOfMonth();
			String place = mPlace.getText().toString();
			String topic = mTopic.getText().toString();
			String uNote = mUNote.getText().toString();
			String pNote = mPNote.getText().toString();

			Log.i("selected_item_pos", mPartner.getSelectedItemPosition()+"");
			Log.i("user_id", mApp.getUser().getId() + "");
			Log.i("date", date);
			Log.i("place", place);
			Log.i("topic", topic);
			Log.i("uNote", uNote);
			Log.i("pNote", pNote);

			// 保存人脉排程信息
			ScheduleEntity schedule = new ScheduleEntity(mApp.getUser().getId(), partner_id, date, place, topic, uNote, pNote);
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
