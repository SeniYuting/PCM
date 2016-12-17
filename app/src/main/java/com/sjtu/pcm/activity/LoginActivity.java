package com.sjtu.pcm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.UserEntity;
import com.sjtu.pcm.entity.UserList;
import com.sjtu.pcm.util.HttpUtil;

import java.util.Date;

/**
 * 登录界面
 */
public class LoginActivity extends Activity {

    // 登录
    private Button mLogin;
    private EditText accountText;
    private EditText passwordText;
    private TextView register;

    private MyApplication mApp;
    private boolean isOK;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mApp = (MyApplication) getApplication();
        isOK = false;

        findViewById();
        setListener();
    }

    /**
     * 绑定界面UI
     */
    private void findViewById() {
        mLogin = (Button) findViewById(R.id.login_activity_login);
        accountText = (EditText) findViewById(R.id.login_activity_account);
        passwordText = (EditText) findViewById(R.id.login_activity_password);
        register = (TextView) findViewById(R.id.login_activity_register);
    }

    /**
     * UI事件监听
     */
    private void setListener() {
        // 登录按钮监听
        mLogin.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String account = accountText.getText().toString();
                new RMPHelper().execute(mApp.getUserUrl() + "?User.account=" + account);
            }
        });
        // 注册按钮监听
        register.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,
                        RegisterActivity.class));
                finish();

            }
        });
    }

    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    class RMPHelper extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uriAPI) {

            String password = passwordText.getText().toString();

            String result_array = HttpUtil.getRequest(uriAPI[0]);
            if (result_array != null){
                UserList userList = new Gson().fromJson(result_array, UserList.class);

                if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0
                        &&  userList.getUser().get(0).getPassword().equals(password)) {

                    Date t= new Date();
                    UserEntity user = userList.getUser().get(0);
                    user.setLastlogindate(t.toString());
                    String userStr = new Gson().toJson(user);
                    HttpUtil.putRequest(mApp.getUserUrl()+user.getId(), userStr );

                    isOK = true;

                    mApp.setUser(user);
                    startActivity(new Intent(LoginActivity.this,
                            MainActivity.class));
                    finish();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(isOK) {
                Toast.makeText(LoginActivity.this, "登录成功, Welcome!", 2000).show();
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误!", 2000).show();
            }

        }
    }

}
