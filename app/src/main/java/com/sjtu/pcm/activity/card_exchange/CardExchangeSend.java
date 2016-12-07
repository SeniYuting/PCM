package com.sjtu.pcm.activity.card_exchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.menu.CardExchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Victor_Zhou on 2016-12-7.
 */

public class CardExchangeSend extends Activity {

    private Button cESReturn;
    private Button cESSearchButton;
    private EditText cESSearchEditText;
    private ListView cESUserListView;


    private MyApplication mApp;
    private List<Map<String, Object>> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardexchange_send);

        findViewById();
        setListener();
        init();
    }

    private void findViewById() {
        cESReturn = (Button) findViewById(R.id.card_exchange_send_return);
        cESSearchButton = (Button) findViewById(R.id.card_exchange_send_search_button);
        cESSearchEditText = (EditText) findViewById(R.id.card_exchange_send_search_text);
        cESUserListView = (ListView) findViewById(R.id.card_exchange_send_user_list_view);
    }

    private void setListener() {
        cESReturn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 显示退出对话框
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        cESSearchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName = cESSearchEditText.getText().toString();

                if ("".equals(userName)){
                    new AlertDialog.Builder(CardExchangeSend.this)
                            .setMessage("请输入用户名")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", null)
                            .show();
                    return;
                }

                // 查找所有的userName，并放入resultList中 TODO
                new CardExchangeSend.RMPHelper().execute();

                //重新为listview设置监听器
                cESUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        new AlertDialog.Builder(CardExchangeSend.this)
                                .setTitle("确认向" + resultList.get(i).get("card_exchange_send_list_view_name") + "发送名片吗？")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 点击“确认”后向该用户发送名片该用户 TODO


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

            }
        });
    }

    private void init() {
        // 初始化用户名片信息
        Log.i("friend_id", mApp.getUserId());

    }

    class RMPHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uriAPI) {

            // 获取用户名片信息
//            HttpGet httpRequest = new HttpGet(uriAPI[0]);
//            String result = "";

            try {
//                HttpResponse httpResponse = new DefaultHttpClient()
//                        .execute(httpRequest);
//
//                InputStream inputStream = httpResponse.getEntity()
//                        .getContent();
//                if (inputStream != null)
//                    result = mapp.convertInputStreamToString(inputStream);
//
//                Log.e("result", result);
//
//                JSONObject result_json = JSONObject
//                        .fromObject(result);
//                resultList.add(result_json.get("name").toString());
//                resultList.add(result_json.get("gender").toString());
//                resultList.add(result_json.get("address").toString());
//                resultList.add(result_json.get("mobile").toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            resultList = new ArrayList<>();

            //测试数据，后面将会用resultList中的数据 TODO
            Map<String, Object> map = new HashMap<>();
            map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_1);
            map.put("card_exchange_send_list_view_name", "周汉辰");
            map.put("user_id", "1");
            resultList.add(map);

            map = new HashMap<>();
            map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_2);
            map.put("card_exchange_send_list_view_name", "沈佳梅");
            map.put("user_id", "2");
            resultList.add(map);

            map = new HashMap<>();
            map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_2);
            map.put("card_exchange_send_list_view_name", "曹雨婷");
            map.put("user_id", "3");
            resultList.add(map);

            //将数据加载到ListView中
            SimpleAdapter adapter = new SimpleAdapter(CardExchangeSend.this, resultList, R.layout.cardexchange_listview_item, new String[]{"card_exchange_send_list_view_portrait", "card_exchange_send_list_view_name"}, new int[]{R.id.card_exchange_send_list_view_portrait,R.id.card_exchange_send_list_view_name});
            cESUserListView.setAdapter(adapter);

        }
    }

}
