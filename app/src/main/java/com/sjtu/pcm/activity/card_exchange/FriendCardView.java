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

import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;

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

    private ArrayList<String> resultList = new ArrayList<>();
    private String friendId;
    private String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_card_view);

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
                                // 点击“确认”后删除该用户 TODO

                                startActivity(new Intent(FriendCardView.this,
                                        MainActivity.class));

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

        // 需要setText，必须在同一个thread中 TODO
        new FriendCardView.RMPHelper().execute();

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

            fCName.setText(resultList.size() > 0 ? resultList.get(0) : "");
            fCCompany.setText(resultList.size() > 1 ? resultList.get(1) : "");
            fCJob.setText(resultList.size() > 2 ? resultList.get(2) : "");
            fCCNumber.setText(resultList.size() > 3 ? resultList.get(3) : "");
            fCCAddress.setText(resultList.size() > 4 ? resultList.get(4) : "");
            fCFax.setText(resultList.size() > 5 ? resultList.get(5) : "");
            fCCEmail.setText(resultList.size() > 6 ? resultList.get(6) : "");
        }
    }

}
