package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.CardExchangeEntity;
import com.sjtu.pcm.entity.CardExchangeList;
import com.sjtu.pcm.entity.TagEntity;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserTagList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 名片识别类
 *
 * 
 */
public class Recommend {
	private UserTagList tagList = null;
	private HashSet<String> userTagDescription = new HashSet<>();
	private String tag = "";
	private Context cRContext;
	// 存储用户间的相似度
	private Map<UserEntity, Integer> userSimilarityMap = new HashMap<>();
	private boolean hasTag = true;
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	private TextView cRTextView;
	private ListView cRListView;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;
	private List<Map<String, Object>> resultList = new ArrayList<>();
	private int selectNum;
	private boolean hasExchanged = false;

	private TextView mTopText;

	@SuppressLint("InflateParams")
	public Recommend(Context context, Activity activity) {
		cRContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.recommend, null);

		mApp = (MyApplication) activity.getApplication();
		Log.i("user_id", mApp.getUser().getId() + "");

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
		cRTextView = (TextView) mHome.findViewById(R.id.card_recommend_middle_text);
		cRListView = (ListView) mHome.findViewById(R.id.card_recommend_list_view);
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
		cRListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				selectNum = i;
				new UserTagHelper().execute(mApp.getUserTagUrl() + "?User_tag.user_id=" + resultList.get(selectNum).get("card_recommend_list_view_user_id"));

			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片推荐");

		new TagInitHelper().execute(mApp.getUserTagUrl() + "?User_tag.user_id=" + mApp.getUser().getId().toString());
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

	class TagInitHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String result_array = HttpUtil.getRequest(uriAPI[0]);

			if (result_array != null){
				Log.e("result_array", result_array);
				UserTagList userTagList = new Gson().fromJson(result_array, UserTagList.class);

				if (userTagList!= null && userTagList.getUser_tag()!= null
						&& userTagList.getUser_tag().size()> 0) {
					hasTag = true;
					for (int i = 0; i < userTagList.getUser_tag().size(); i++){
						String tagUserUri = mApp.getUserTagUrl() + "?User_tag.tag_id=" + userTagList.getUser_tag().get(i).getTag_id().toString();
						String tagUserResult = HttpUtil.getRequest(tagUserUri);
						if(tagUserResult != null){
							UserTagList resultList = new Gson().fromJson(tagUserResult, UserTagList.class);
							if (resultList != null && resultList.getUser_tag()!= null
									&& resultList.getUser_tag().size()> 0) {
								for(int j = 0; j < resultList.getUser_tag().size(); j++){
									if(!resultList.getUser_tag().get(j).getUser_id().toString().equals(mApp.getUser().getId().toString())){
										String userUri = mApp.getUserUrl() + resultList.getUser_tag().get(j).getUser_id().toString();
										String userStr = HttpUtil.getRequest(userUri);
										if(userStr != null){
											UserEntity userEntity = new Gson().fromJson(userStr, UserEntity.class);
											if(userSimilarityMap.containsKey(userEntity))
												userSimilarityMap.put(userEntity, userSimilarityMap.get(userEntity) + 1);
											else
												userSimilarityMap.put(userEntity, 1);
										}
									}
								}
							}
						}
					}
				} else {
					hasTag = false;
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(hasTag){
				if(userSimilarityMap.isEmpty()){
					cRTextView.setText("找不到与你兴趣相同的用户呢~");
				} else {
					cRTextView.setVisibility(View.GONE);
					for (UserEntity user : userSimilarityMap.keySet()){

						Map<String, Object> map = new HashMap<>();
						if(user.getGender() == null)
							map.put("card_recommend_list_view_portrait", R.drawable.portrait_3);
						else if(user.getGender() == 0)
							map.put("card_recommend_list_view_portrait", R.drawable.portrait_1);
						else if(user.getGender() == 1)
							map.put("card_recommend_list_view_portrait", R.drawable.portrait_2);
						map.put("card_recommend_list_view_name", user.getName() == null ? "" : user.getName());
						map.put("card_recommend_list_view_user_id", user.getId());
						map.put("Similarity", userSimilarityMap.get(user));
						resultList.add(map);

					}
					Collections.sort(resultList, new SortBySimilarity());
					//将数据加载到ListView中
					SimpleAdapter adapter = new SimpleAdapter(cRContext, resultList, R.layout.recommend_listview_item, new String[]{"card_recommend_list_view_portrait", "card_recommend_list_view_name"}, new int[]{R.id.card_recommend_list_view_portrait,R.id.card_recommend_list_view_name});
					cRListView.setAdapter(adapter);
				}
			}

		}
	}

	class SortBySimilarity implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			Map<String, Object> map1 = (Map<String, Object>)o1;
			Map<String, Object> map2 = (Map<String, Object>)o2;
			if((Integer)map1.get("Similarity") > (Integer)map2.get("Similarity"))
				return 1;
			return 0;
		}
	}

	class UserTagHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uriAPI) {

			String result_array = HttpUtil.getRequest(uriAPI[0]);

			if (result_array != null){
				Log.e("result_array", result_array);
				tagList = new Gson().fromJson(result_array, UserTagList.class);

				if (tagList!= null && tagList.getUser_tag()!= null
						&& tagList.getUser_tag().size()> 0) {
					for (int i = 0; i < tagList.getUser_tag().size(); i++){
						String userTagUri = mApp.getTagUrl() + tagList.getUser_tag().get(i).getTag_id().toString();
						String userTagResult = HttpUtil.getRequest(userTagUri);
						if(userTagResult != null){
							TagEntity tagEntity = new Gson().fromJson(userTagResult, TagEntity.class);
							userTagDescription.add(tagEntity.getTag_description());
						}
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			for(String tagDescription : userTagDescription){
				tag = tag + tagDescription + ",";
			}
			if(tag.length() > 0)
				tag = tag.substring(0, tag.length()-1);

			new AlertDialog.Builder(cRContext)
					.setTitle("是否向" + resultList.get(selectNum).get("card_recommend_list_view_name") + "发送名片？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("他的标签是：" + tag)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new CardExchangeHelper().execute();
						}
					})
					.setNegativeButton("返回", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击“返回”后的操作,这里不设置没有任何操作
						}
					}).show();
		}
	}

	class CardExchangeHelper extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... s) {
			String uri = mApp.getCardExchangeUrl() + "?Card_exchange.send_user_id=" + mApp.getUser().getId().toString() + "&Card_exchange.receive_user_id=" + resultList.get(selectNum).get("card_recommend_list_view_user_id").toString();

			String result_array = HttpUtil.getRequest(uri);

			CardExchangeList cardExchangeList = null;

			if (result_array != null){
				Log.e("result_array", result_array);
				cardExchangeList = new Gson().fromJson(result_array, CardExchangeList.class);
			}

			if (cardExchangeList!= null && cardExchangeList.getCard_exchange()!= null
					&& cardExchangeList.getCard_exchange().size()> 0) {
				hasExchanged = true;
			} else {
				CardExchangeEntity cardExchangeEntity = new CardExchangeEntity(mApp.getUser().getId(), (Long)resultList.get(selectNum).get("card_recommend_list_view_user_id"), "");

				String uriAPI = mApp.getCardExchangeUrl();

				Log.e("cardExchange post", new Gson().toJson(cardExchangeEntity));

				HttpUtil.postRequest(uriAPI, new Gson().toJson(cardExchangeEntity));

				hasExchanged = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(hasExchanged)
				Toast.makeText(cRContext, "该用户已经有您的名片了呦~", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(cRContext, "发送名片成功！", Toast.LENGTH_LONG).show();
		}
	}
}
