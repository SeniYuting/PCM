package com.sjtu.pcm.activity.contact;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;

/**
 * 联系我们类
 *
 * 
 */
public class ContactActivity extends Activity {
	private EditText mStar;
	private EditText mComment;
	private Button mSubmit;
	
	private MyApplication mapp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		
		mapp = (MyApplication) getApplication();

		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mStar = (EditText) findViewById(R.id.contact_star);
		mComment = (EditText) findViewById(R.id.contact_comment);
		mSubmit = (Button) findViewById(R.id.contact_submit);
	}

	private void setListener() {
		mSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String star = mStar.getText().toString();
				String comment = mComment.getText().toString();

				Log.i("star", star);
				Log.i("comment", comment);

				// TODO
			}
		});
	}

	private void init() {
		// 获取用户user_id
		Log.i("user_id", mapp.getUserId());
	}
}

