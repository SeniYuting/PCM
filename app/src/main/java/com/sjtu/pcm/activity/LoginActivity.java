package com.sjtu.pcm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.entity.UserList;
import com.sjtu.pcm.util.HttpUtil;

/**
 * 登录界面
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {

    // 登录
    private Button mLogin;
    private EditText accountText;
    private EditText passwordText;
    private TextView register;

    private MyApplication mApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mApp = (MyApplication) getApplication();

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
                // 专门线程进行网络访问
                new Thread() {

                    @Override
                    public void run() {
                        String account = accountText.getText().toString();
                        String password = passwordText.getText().toString();

                        // 获取用户信息
                        String uriAPI = mApp.getUserUrl() + "?User.account=" + account;

                        try {

                            String result_array = HttpUtil.getRequest(uriAPI);
                            if (result_array != null){
                                UserList userList = new Gson().fromJson(result_array, UserList.class);

                                if (userList!= null && userList.getUser()!= null && userList.getUser().size()> 0
                                &&  userList.getUser().get(0).getPassword().equals(password)) {

                                    mApp.setUser(userList.getUser().get(0));

                                    startActivity(new Intent(LoginActivity.this,
                                            MainActivity.class));

                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

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
}
