package com.sjtu.pcm;

import android.app.Application;

import com.sjtu.pcm.entity.UserEntity;

/**
 * application类
 * 
 * @author CaoYuting
 * 
 */
public class MyApplication extends Application {
	String project_url;
	UserEntity user;

	@Override
	public void onCreate() {
		super.onCreate();
		project_url = "http://112.74.49.183:8080/Entity/U31b61aa24b0ae/PCM/";
	}

	public String getUserUrl() {
		return project_url + "User/";
	}

	public String getCardUrl() {
		return project_url + "Card/";

	}

	public String getScheduleUrl() {
		return project_url + "Schedule/";
	}

	public String getCommentUrl() {
		return project_url + "Comment/";
	}

	public String getCardExchangeUrl() {
		return project_url + "Card_exchange/";
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
