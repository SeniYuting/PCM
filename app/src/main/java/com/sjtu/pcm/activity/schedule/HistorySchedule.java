package com.sjtu.pcm.activity.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.ScheduleList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SeniYuting on 2016/12/7.
 */

@SuppressWarnings({ "unused", "deprecation" })
public class HistorySchedule extends Activity {
	private Button mBack;

	private ListView shListView;

	private MyApplication mApp;
	private List<Map<String, Object>> resultList = new ArrayList<>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_schedule);

		mApp = (MyApplication) getApplication();

		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mBack = (Button) findViewById(R.id.history_schedule_back);
		shListView = (ListView) findViewById(R.id.history_schedule_list_view);
	}

	private void setListener() {
		mBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		// 初始化用户信息
		Log.i("user_id", mApp.getUser().getId() + "");
		new RMPHelper().execute(mApp.getScheduleUrl() + "?Schedule.suer_id=" + mApp.getUser().getId());

	}

	class RMPHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			Log.e("url", uriAPI[0]);

			try {
				// 获取排程信息
				String result_array = HttpUtil.getRequest(uriAPI[0]);
				if (result_array != null){
					ScheduleList scheduleList = new Gson().fromJson(result_array, ScheduleList.class);

					if (scheduleList!= null && scheduleList.getSchedule()!= null
							&& scheduleList.getSchedule().size()> 0) {

						// TODO
						Log.e("scheduleList", scheduleList.getSchedule().size()+"");

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			//测试数据，后面将会用resultList中的数据 TODO
			Map<String, Object> map = new HashMap<>();
			map.put("history_schedule_portrait", R.drawable.portrait_1);
			map.put("history_schedule_name", "周汉辰");
			map.put("history_schedule_time", "2016-12-07");
			map.put("history_schedule_place", "上海交通大学");
			map.put("history_schedule_topic", "XXX话题");
			map.put("history_schedule_unote", "XXX 我的idea");
			map.put("history_schedule_pnote", "  XXX Ta的idea");
			resultList.add(map);

			map = new HashMap<>();
			map.put("history_schedule_portrait", R.drawable.portrait_2);
			map.put("history_schedule_name", "沈佳梅");
			map.put("history_schedule_time", "2016-12-07");
			map.put("history_schedule_place", "上海交通大学");
			map.put("history_schedule_topic", "XXX话题");
			map.put("history_schedule_unote", "XXX 我的idea");
			map.put("history_schedule_pnote", "  XXX Ta的idea");
			resultList.add(map);

			map = new HashMap<>();
			map.put("history_schedule_portrait", R.drawable.portrait_2);
			map.put("history_schedule_name", "曹雨婷");
			map.put("history_schedule_time", "2016-12-07");
			map.put("history_schedule_place", "上海交通大学");
			map.put("history_schedule_topic", "XXX话题");
			map.put("history_schedule_unote", "XXX 我的idea");
			map.put("history_schedule_pnote", "  XXX Ta的idea");
			resultList.add(map);

			//将数据加载到ListView中
			SimpleAdapter adapter = new SimpleAdapter(HistorySchedule.this, resultList,
					R.layout.history_schedule_item,
					new String[]{"history_schedule_portrait", "history_schedule_name", "history_schedule_time",
							"history_schedule_place", "history_schedule_topic", "history_schedule_unote",
							"history_schedule_pnote"
					},
					new int[]{R.id.history_schedule_portrait, R.id.history_schedule_name, R.id.history_schedule_time,
							R.id.history_schedule_place, R.id.history_schedule_topic, R.id.history_schedule_unote,
							R.id.history_schedule_pnote
					}
			){
				// item 响应事件
				public View getView(int position, View convertView, ViewGroup parent) {
					final int p = position;
					final View view=super.getView(position, convertView, parent);
					Button button=(Button)view.findViewById(R.id.history_schedule_item_delete);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Log.e("p", p + "");

							new AlertDialog.Builder(HistorySchedule.this).setTitle("确认删除该历史排程吗？")
									.setIcon(android.R.drawable.ic_dialog_info)
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											// 点击“确认”后删除该用户 TODO
											Log.i("history_schedule_name", resultList.get(p).get("history_schedule_name")+"");

										}
									})
									.setNegativeButton("返回", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											// 点击“返回”后的操作,这里不设置没有任何操作
										}
									}).show();
						}
					});
					return view;
				}
			};
			shListView.setAdapter(adapter);

		}
	}

}

