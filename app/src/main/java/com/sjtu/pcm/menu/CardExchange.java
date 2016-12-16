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

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.card_exchange.CardExchangeSend;
import com.sjtu.pcm.activity.card_exchange.FriendCardView;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.CardExchangeEntity;
import com.sjtu.pcm.entity.CardExchangeList;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.util.HttpUtil;

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
    private Button cESend;
    private ListView cEListView;
    private TextView cETextView;

    private MyApplication mApp;
    private OnOpenListener mOnOpenListener;

    private TextView mTopText;
    private List<Map<String, Object>> resultList = new ArrayList<>();

    public CardExchange(Context context, Activity activity) {
        cEContext = context;
        // 绑定布局到当前View
        mHome = LayoutInflater.from(context).inflate(R.layout.cardexchange, null);

        mApp = (MyApplication) activity.getApplication();
        Log.i("user_id", mApp.getUser().getId()+"");

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
        cESend = (Button) mHome.findViewById(R.id.card_exchange_send);
        cETextView = (TextView) mHome.findViewById(R.id.card_exchange_middle_text);
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
                Log.e("friendId",  resultList.get(i).get("card_exchange_list_view_user_id").toString());
                bundle.putString("recordId", resultList.get(i).get("card_exchange_list_view_id").toString());
                bundle.putString("friendId", resultList.get(i).get("card_exchange_list_view_user_id").toString());
                bundle.putString("friendName", (String) resultList.get(i).get("card_exchange_list_view_name"));
                intent.putExtras(bundle);
                cEContext.startActivity(intent);

            }
        });

        cESend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(cEContext, CardExchangeSend.class);
                cEContext.startActivity(intent);
            }
        });
    }

    /**
     * 界面初始化
     */
    private void init() {
        mTopText.setText("名片交换");

        new RMPHelper().execute(mApp.getCardExchangeUrl() + "?Card_exchange.receive_user_id=" + mApp.getUser().getId());

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

            String result_array = HttpUtil.getRequest(uriAPI[0]);

            Log.e("result_array", result_array);

            if (result_array != null){
                CardExchangeList cardExchangeList = new Gson().fromJson(result_array, CardExchangeList.class);

                if (cardExchangeList!= null && cardExchangeList.getCard_exchange()!= null
                        && cardExchangeList.getCard_exchange().size()> 0) {

                    Log.e("cardList", cardExchangeList.getCard_exchange().size()+"");

                    for (int i = 0; i < cardExchangeList.getCard_exchange().size(); i++) {

                        CardExchangeEntity cardExchangeEntity = cardExchangeList.getCard_exchange().get(i);

                        String result_user = HttpUtil.getRequest(mApp.getUserUrl() + cardExchangeEntity.getSend_user_id());
                        Log.e("result_user", result_user);
                        UserEntity sendUser = new Gson().fromJson(result_user, UserEntity.class);

                        Map<String, Object> map = new HashMap<>();
                        if(sendUser.getGender() == null)
                            map.put("card_exchange_list_view_portrait", R.drawable.portrait_3);
                        else if(sendUser.getGender() == 0)
                            map.put("card_exchange_list_view_portrait", R.drawable.portrait_1);
                        else if(sendUser.getGender() == 1)
                            map.put("card_exchange_list_view_portrait", R.drawable.portrait_2);
                        map.put("card_exchange_list_view_name", sendUser.getName() == null ? "" : sendUser.getName());
                        map.put("card_exchange_list_view_user_id", sendUser.getId());
                        map.put("card_exchange_list_view_id", cardExchangeEntity.getId());
                        resultList.add(map);

                    }

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //测试数据，后面将会用resultList中的数据
//            Map<String, Object> map = new HashMap<>();
//            map.put("card_exchange_list_view_portrait", R.drawable.portrait_1);
//            map.put("card_exchange_list_view_name", "周汉辰");
//            map.put("card_exchange_list_view_user_id", "1");
//            resultList.add(map);
//
//            map = new HashMap<>();
//            map.put("card_exchange_list_view_portrait", R.drawable.portrait_2);
//            map.put("card_exchange_list_view_name", "沈佳梅");
//            map.put("card_exchange_list_view_user_id", "2");
//            resultList.add(map);
//
//            map = new HashMap<>();
//            map.put("card_exchange_list_view_portrait", R.drawable.portrait_3);
//            map.put("card_exchange_list_view_name", "曹雨婷");
//            map.put("card_exchange_list_view_user_id", "3");
//            resultList.add(map);

            if(resultList.size() != 0){
                cETextView.setVisibility(View.GONE);
                //将数据加载到ListView中
                SimpleAdapter adapter = new SimpleAdapter(cEContext, resultList, R.layout.cardexchange_listview_item, new String[]{"card_exchange_list_view_portrait", "card_exchange_list_view_name"}, new int[]{R.id.card_exchange_list_view_portrait,R.id.card_exchange_list_view_name});
                cEListView.setAdapter(adapter);
            }
        }

    }

}
