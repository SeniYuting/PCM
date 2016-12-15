package com.sjtu.pcm.activity.card_design;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.entity.CardEntity;
import com.sjtu.pcm.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by Victor_Zhou on 2016-12-5.
 */

public class CardModifyActivity extends Activity {

    private Button cMCancel;
    private Button cMSave;
    private EditText cMName;
    private EditText cMCompany;
    private EditText cMJob;
    private EditText cMCNumber;
    private EditText cMCAddress;
    private EditText cMFax;
    private EditText cMCEmail;

    private MyApplication mApp;
    private ArrayList<String> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_modify);

        mApp = (MyApplication) getApplication();

        findViewById();
        setListener();
        init();
    }

    private void findViewById() {
        cMCancel = (Button) findViewById(R.id.card_modify_cancel);
        cMSave = (Button) findViewById(R.id.card_modify_save);
        cMName = (EditText) findViewById(R.id.card_modify_name);
        cMCompany = (EditText) findViewById(R.id.card_modify_company);
        cMJob = (EditText) findViewById(R.id.card_modify_job);
        cMCNumber = (EditText) findViewById(R.id.card_modify_c_number);
        cMCAddress = (EditText) findViewById(R.id.card_modify_c_address);
        cMFax = (EditText) findViewById(R.id.card_modify_fax);
        cMCEmail = (EditText) findViewById(R.id.card_modify_c_email);
    }

    private void setListener() {
        cMCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 显示退出对话框
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        cMSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 保存修改结果
                // 专门线程进行网络访问
                new Thread() {
                    @Override
                    public void run() {

                        String cardName = cMName.getText().toString();
                        String cardCompany = cMCompany.getText().toString();
                        String cardJob = cMJob.getText().toString();
                        String cardNumber = cMCNumber.getText().toString();
                        String cardAddress = cMCAddress.getText().toString();
                        String cardFax = cMFax.getText().toString();
                        String cardEmail = cMCEmail.getText().toString();

                        Log.i("cardName", cardName);
                        Log.i("cardCompany", cardCompany);
                        Log.i("cardJob", cardJob);
                        Log.i("cardNumber", cardNumber);
                        Log.i("cardAddress", cardAddress);
                        Log.i("cardFax", cardFax);
                        Log.i("cardEmail", cardEmail);

                        //网络通信，修改用户名片信息 TODO
                        try {
//                            JSONObject obj = new JSONObject();
//                            obj.put("name", cardName);
//                            obj.put("company", cardCompany);
//                            obj.put("job", cardJob);
//                            obj.put("number", cardNumber);
//                            obj.put("address", cardAddress);
//                            obj.put("fax", cardFax);
//                            obj.put("email", cardEmail);

                            CardEntity card = new CardEntity(cardName, cardCompany, cardJob,
                                    cardNumber, cardAddress, cardFax, cardEmail);

                            String uriAPI = mApp.getCardUrl();

                            HttpUtil.postRequest(uriAPI, new Gson().toJson(card));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }
                }.start();

                setResult(RESULT_OK);

                startActivity(new Intent(CardModifyActivity.this,
                        MainActivity.class));

//				finish(); // 直接关闭该页面
            }
        });
    }

    private void init() {
        // 初始化用户名片信息
        Log.i("user_id", mApp.getUser().getId() + "");
        // 需要setText，必须在同一个thread中
        new CardModifyActivity.RMPHelper().execute("http://112.74.49.183:8080/Entity/U209f9ab73161d8/PCM/Card/" + mApp.getUser().getId());

    }

    class RMPHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uriAPI) {

            // 获取用户名片信息 TODO
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

            cMName.setText(resultList.size() > 0 ? resultList.get(0) : "");
            cMCompany.setText(resultList.size() > 1 ? resultList.get(1) : "");
            cMJob.setText(resultList.size() > 2 ? resultList.get(2) : "");
            cMCNumber.setText(resultList.size() > 3 ? resultList.get(3) : "");
            cMCAddress.setText(resultList.size() > 4 ? resultList.get(4) : "");
            cMFax.setText(resultList.size() > 5 ? resultList.get(5) : "");
            cMCEmail.setText(resultList.size() > 6 ? resultList.get(6) : "");
        }
    }
}
