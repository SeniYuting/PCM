package com.sjtu.pcm.activity.card_exchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.entity.CardEntity;
import com.sjtu.pcm.entity.CardExchangeEntity;
import com.sjtu.pcm.entity.CardList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by Victor_Zhou on 2016-12-5.
 */

public class FriendCardView extends Activity {

    private Button fCVReturn;
    private Button fCVDelete;
    private TextView fCVTopText;
    private TextView fCName;
    private TextView fCCompany;
    private TextView fCJob;
    private TextView fCCNumber;
    private TextView fCCAddress;
    private TextView fCFax;
    private TextView fCCEmail;

    private MyApplication mApp;
    private CardEntity cardEntity = new CardEntity();
    private String friendId;
    private String friendName;
    private String recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_card_view);

        mApp = (MyApplication) getApplication();

        recordId = getIntent().getExtras().getString("recordId");
        friendId = getIntent().getExtras().getString("friendId");
        friendName = getIntent().getExtras().getString("friendName");

        findViewById();
        setListener();
        init();
    }

    private void findViewById() {
        fCVReturn = (Button) findViewById(R.id.friend_card_view_return);
        fCVDelete = (Button) findViewById(R.id.friend_card_view_delete);
        fCVTopText = (TextView)findViewById(R.id.friend_card_view_top_text);
        fCName = (TextView) findViewById(R.id.friend_card_name);
        fCCompany = (TextView) findViewById(R.id.friend_card_company);
        fCJob = (TextView) findViewById(R.id.friend_card_job);
        fCCNumber = (TextView) findViewById(R.id.friend_card_c_number);
        fCCAddress = (TextView) findViewById(R.id.friend_card_c_address);
        fCFax = (TextView) findViewById(R.id.friend_card_fax);
        fCCEmail = (TextView) findViewById(R.id.friend_card_c_email);
    }

    private void setListener() {
        fCVReturn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 显示退出对话框
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        fCVDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new AlertDialog.Builder(FriendCardView.this).setTitle("确认删除该名片吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(){
                                    @Override
                                    public void run() {
                                        CardExchangeEntity cardExchangeEntity = new CardExchangeEntity(new Long(0),new Long(0),"");

                                        String uriAPI = mApp.getCardExchangeUrl() + recordId;

                                        Log.e("card put", new Gson().toJson(cardExchangeEntity).toString());

                                        HttpUtil.putRequest(uriAPI, new Gson().toJson(cardExchangeEntity));

                                        startActivity(new Intent(FriendCardView.this,
                                                MainActivity.class));
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
//				finish(); // 直接关闭该页面
            }
        });
    }

    private void init() {
        // 初始化用户名片信息
        Log.i("friend_id", friendId);

        fCVTopText.setText(friendName + "的名片");

        // 需要setText，必须在同一个thread中
        new RMPHelper().execute(mApp.getCardUrl() + "?Card.user_id=" + friendId);

    }

    class RMPHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uriAPI) {

            String result_array = HttpUtil.getRequest(uriAPI[0]);

            Log.e("result_array", result_array);

            if (result_array != null){
                CardList cardList = new Gson().fromJson(result_array, CardList.class);

                if (cardList!= null && cardList.getCard()!= null
                        && cardList.getCard().size()> 0) {

                    Log.e("cardList", cardList.getCard().size()+"");

                    cardEntity = cardList.getCard().get(0);

                    Log.e("card", new Gson().toJson(cardEntity));

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            fCName.setText(cardEntity.getName());
            fCCompany.setText(cardEntity.getCompany());
            fCJob.setText(cardEntity.getJob());
            fCCNumber.setText(cardEntity.getC_number());
            fCCAddress.setText(cardEntity.getC_address());
            fCFax.setText(cardEntity.getC_fax());
            fCCEmail.setText(cardEntity.getC_email());

        }
    }

}
