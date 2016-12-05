package com.sjtu.pcm.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.card_exchange.FriendCardView;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 名片交换类
 *
 *
 */
public class CardExchange {
	private Context cEContext;
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private ListView cEListView;


	private MyApplication mapp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;
	private List<Map<String, Object>> resultList = new ArrayList<>();

	public CardExchange(Context context, Activity activity) {
		cEContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.cardexchange, null);

		mapp = (MyApplication) activity.getApplication();
		Log.i("user_id", mapp.getUserId());

		findViewById();
		init();
		setListener();

	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.menu);
		mTopText = (TextView) mHome.findViewById(R.id.top_text);
		cEListView = (ListView) mHome.findViewById(R.id.card_exchange_list_view);
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

		cEListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				Intent intent = new Intent(cEContext, FriendCardView.class);
				Bundle bundle = new Bundle();
				Log.e("friendId", (String) resultList.get(i).get("card_exchange_list_view_id"));
				bundle.putString("friendId", (String) resultList.get(i).get("card_exchange_list_view_id"));
				bundle.putString("friendName", (String) resultList.get(i).get("card_exchange_list_view_name"));
				intent.putExtras(bundle);
				cEContext.startActivity(intent);

			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片交换");

		//现将所有用户好友数据存入resultList TODO

		//测试数据，后面将会用resultList中的数据 TODO
		Map<String, Object> map = new HashMap<>();
		map.put("card_exchange_list_view_portrait", R.drawable.portrait_1);
		map.put("card_exchange_list_view_name", "周汉辰");
		map.put("card_exchange_list_view_id", "1");
		resultList.add(map);

		//将数据加载到ListView中
		SimpleAdapter adapter = new SimpleAdapter(cEContext, resultList, R.layout.cardexchange_listview_item, new String[]{"card_exchange_list_view_portrait", "card_exchange_list_view_name"}, new int[]{R.id.card_exchange_list_view_portrait,R.id.card_exchange_list_view_name});
		cEListView.setAdapter(adapter);

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
				//这里会返回很多个对象，resultList里需要全部数据
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

	}

}
