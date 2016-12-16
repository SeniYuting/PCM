package com.sjtu.pcm.activity.card_design;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.entity.CardEntity;
import com.sjtu.pcm.entity.CardList;
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
    private CardEntity cardEntity = new CardEntity();

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

        cMName.setOnEditorActionListener(onEditorActionListener);
        cMCompany.setOnEditorActionListener(onEditorActionListener);
        cMJob.setOnEditorActionListener(onEditorActionListener);
        cMCNumber.setOnEditorActionListener(onEditorActionListener);
        cMCAddress.setOnEditorActionListener(onEditorActionListener);
        cMFax.setOnEditorActionListener(onEditorActionListener);
        cMCEmail.setOnEditorActionListener(onEditorActionListener);

        cMSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 保存修改结果
                // 专门线程进行网络访问
                new CardModifyHelper().execute();

            }
        });
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            return(keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER);
        }
    };

    private void init() {
        // 初始化用户名片信息
        Log.i("user_id", mApp.getUser().getId() + "");
        // 需要setText，必须在同一个thread中
        Log.e("init", "cardModify");
        new RMPHelper().execute(mApp.getCardUrl() + "?Card.user_id=" + mApp.getUser().getId());

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

            cMName.setText(cardEntity.getName());
            cMCompany.setText(cardEntity.getCompany());
            cMJob.setText(cardEntity.getJob());
            cMCNumber.setText(cardEntity.getC_number());
            cMCAddress.setText(cardEntity.getC_address());
            cMFax.setText(cardEntity.getC_fax());
            cMCEmail.setText(cardEntity.getC_email());

        }
    }

    class CardModifyHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... s) {

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


//                            JSONObject obj = new JSONObject();
//                            obj.put("name", cardName);
//                            obj.put("company", cardCompany);
//                            obj.put("job", cardJob);
//                            obj.put("number", cardNumber);
//                            obj.put("address", cardAddress);
//                            obj.put("fax", cardFax);
//                            obj.put("email", cardEmail);

            if(cardEntity.getId() == 0){

                CardEntity card = new CardEntity(mApp.getUser().getId(), cardName, cardCompany, cardJob,
                        cardNumber, cardAddress, cardFax, cardEmail);

                String uriAPI = mApp.getCardUrl();

                Log.e("card psot", new Gson().toJson(card).toString());

                HttpUtil.postRequest(uriAPI, new Gson().toJson(card));

            } else {

                CardEntity card = new CardEntity(mApp.getUser().getId(), cardName, cardCompany, cardJob,
                        cardNumber, cardAddress, cardFax, cardEmail);

                String uriAPI = mApp.getCardUrl() + cardEntity.getId();

                Log.e("card put", new Gson().toJson(card).toString());

                HttpUtil.putRequest(uriAPI, new Gson().toJson(card));

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(CardModifyActivity.this, "提交成功!", 2000).show();
            setResult(RESULT_OK);
            startActivity(new Intent(CardModifyActivity.this,
                    MainActivity.class));
            finish();
        }
    }
}
