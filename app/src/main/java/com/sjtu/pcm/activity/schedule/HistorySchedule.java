package com.sjtu.pcm.activity.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;

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
		Log.i("user_id", mApp.getUserId());

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
		);
		shListView.setAdapter(adapter);

	}

}

