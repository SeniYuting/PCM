package com.sjtu.pcm;

import android.app.Application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * application类
 * 
 * @author CaoYuting
 * 
 */
public class MyApplication extends Application {
	String project_url;
	String user_id;
	String account;
	String password; // 应加密
	UserEntity user;

	@Override
	public void onCreate() {
		super.onCreate();
		project_url = "http://112.74.49.183:8080/Entity/U31b61aa24b0ae/PCM/";
		user_id = "";
		account = "";
		password = "";
		gender = -1;
	}

	public String getProjectUrl() {
		return project_url;
	}

	public String getUserUrl() {
		return project_url+"User/";
	}

	public String getCardUrl() {
		return project_url+"Card/";

	}
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}


	public String getUserId() {
		return user_id;
	}

	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String convertInputStreamToString(InputStream inputStream)
			throws Exception {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}
}
