package com.sjtu.pcm.activity.card_exchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.CardEntity;
import com.sjtu.pcm.entity.CardExchangeEntity;
import com.sjtu.pcm.entity.CardExchangeList;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserList;
import com.sjtu.pcm.util.HttpUtil;

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
    private int selectNum;
    private UserList userList;
    private boolean hasExchanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardexchange_send);

        mApp = (MyApplication) getApplication();

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

        cESSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
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

                // 查找所有的userName，并放入resultList中
                new RMPHelper().execute(mApp.getUserUrl() + "?User.account=(like)" + userName);

                //重新为listview设置监听器
                cESUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        selectNum = i;
                        new AlertDialog.Builder(CardExchangeSend.this)
                                .setTitle("确认向" + resultList.get(selectNum).get("card_exchange_send_list_view_name") + "发送名片吗？")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(mApp.getUser().getId().toString().equals(resultList.get(selectNum).get("user_id").toString())){
                                            Toast.makeText(CardExchangeSend.this, "不可以给自己发送名片呦~", Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            new CardExchangeHelper().execute();
                                        }

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
        Log.i("friend_id", mApp.getUser().getId() + "");

    }

    class RMPHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uriAPI) {
            String result_array = HttpUtil.getRequest(uriAPI[0]);

            if (result_array != null){
                Log.e("result_array", result_array);
                userList = new Gson().fromJson(result_array, UserList.class);

                if (userList!= null && userList.getUser()!= null
                        && userList.getUser().size()> 0) {
                    Log.e("cardList", userList.getUser().size()+"");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            resultList = new ArrayList<>();

            if(userList.getUser() != null){
                for (int i = 0; i < userList.getUser().size(); i++) {

                    Log.e("user", new Gson().toJson(userList.getUser().get(i)));

                    Map<String, Object> map = new HashMap<>();
                    if(userList.getUser().get(i).getGender() == null)
                        map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_3);
                    else if(userList.getUser().get(i).getGender() == 0)
                        map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_1);
                    else if(userList.getUser().get(i).getGender() == 1)
                        map.put("card_exchange_send_list_view_portrait", R.drawable.portrait_2);
                    map.put("card_exchange_send_list_view_name", userList.getUser().get(i).getName() == null ? "" : userList.getUser().get(i).getName());
                    map.put("user_id", userList.getUser().get(i).getId());
                    resultList.add(map);

                }
            } else {
                Log.e("result_array", "null user");
                new AlertDialog.Builder(CardExchangeSend.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("未搜索到您输入的用户")
                        .setPositiveButton("确定", null)
                        .show();
            }

            //将数据加载到ListView中
            SimpleAdapter adapter = new SimpleAdapter(CardExchangeSend.this, resultList, R.layout.cardexchange_send_listview_item, new String[]{"card_exchange_send_list_view_portrait", "card_exchange_send_list_view_name"}, new int[]{R.id.card_exchange_send_list_view_portrait,R.id.card_exchange_send_list_view_name});
            cESUserListView.setAdapter(adapter);

        }
    }

    class CardExchangeHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... s) {
            String uri = mApp.getCardExchangeUrl() + "?Card_exchange.send_user_id=" + mApp.getUser().getId().toString() + "&Card_exchange.receive_user_id=" + resultList.get(selectNum).get("user_id").toString();

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
                CardExchangeEntity cardExchangeEntity = new CardExchangeEntity(mApp.getUser().getId(), (Long)resultList.get(selectNum).get("user_id"), "");

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
                Toast.makeText(CardExchangeSend.this, "该用户已经有您的名片了呦~", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(CardExchangeSend.this, "发送名片成功！", Toast.LENGTH_LONG).show();
        }
    }

}
