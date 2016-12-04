package com.sjtu.pcm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.sjtu.pcm.menu.Desktop;
import com.sjtu.pcm.menu.Desktop.onChangeViewListener;
import com.sjtu.pcm.menu.User;
import com.sjtu.pcm.menu.Card;
import com.sjtu.pcm.menu.Tag;
import com.sjtu.pcm.menu.Schedule;
import com.sjtu.pcm.menu.Recognize;
import com.sjtu.pcm.menu.Contact;
import com.sjtu.pcm.anim.MyViewGroup;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.util.ViewUtil;

/**
 * 主界面
 * 
 * @author CaoyYuting
 * 
 */
public class MainActivity extends Activity implements OnOpenListener {

	private MyViewGroup mRoot; // 当前显示内容的容器(继承于ViewGroup)
	private Desktop mDesktop; // 菜单界面
	@SuppressWarnings("unused")
	private int mViewPosition;// 当前显示的View的编号

	private User mUserInfo; // 用户管理界面
	private Card mCard; // 名片管理面
	private Tag mTag; // 用户标签界面
	private Schedule mSchedule; // 人脉排程界面
	private Recognize mRecognize; // 名片识别界面
	private Contact mContact; // 联系我们界面

	private long mExitTime; // 退出时间
	private static final int INTERVAL = 2000; // 退出间隔

	public static Activity mInstance;

	protected int mScreenWidth; // 屏幕宽度

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 创建容器,并设置全屏大小
		mRoot = new MyViewGroup(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		// 创建菜单界面和内容首页界面,并添加到容器中,用于初始显示
		mDesktop = new Desktop(this, this);
		mCard = new Card(this, this);
		mRoot.addView(mDesktop.getView(), params);
		mRoot.addView(mCard.getView(), params);
		setContentView(mRoot);
		setListener();
		mInstance = this;

		// 获取屏幕宽度和高度
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		mCard.setOnOpenListener(this);
		// 监听菜单界面切换显示内容(onChangeViewListener接口在Desktop中定义)
		mDesktop.setOnChangeViewListener(new onChangeViewListener() {

			public void onChangeView(int arg0) {
				mViewPosition = arg0;
				switch (arg0) {
				case ViewUtil.USER:
					mUserInfo = new User(MainActivity.this, MainActivity.this);
					mUserInfo.setOnOpenListener(MainActivity.this);
					mRoot.close(mUserInfo.getView());
					break;
				case ViewUtil.CARD:
					mCard = new Card(MainActivity.this, MainActivity.this);
					mCard.setOnOpenListener(MainActivity.this);
					mRoot.close(mCard.getView());
					break;
				case ViewUtil.TAG:
					mTag = new Tag(MainActivity.this, MainActivity.this);
					mTag.setOnOpenListener(MainActivity.this);
					mRoot.close(mTag.getView());
					break;
				case ViewUtil.SCHEDULE:
					mSchedule = new Schedule(MainActivity.this,
							MainActivity.this);
					mSchedule.setOnOpenListener(MainActivity.this);
					mRoot.close(mSchedule.getView());
					break;
				case ViewUtil.RECOGNIZE:
					mRecognize = new Recognize(MainActivity.this,
							MainActivity.this);
					mRecognize.setOnOpenListener(MainActivity.this);
					mRoot.close(mRecognize.getView());
					break;
				case ViewUtil.CONTACT:
					mContact = new Contact(MainActivity.this, MainActivity.this);
					mContact.setOnOpenListener(MainActivity.this);
					mRoot.close(mContact.getView());
					break;
				case ViewUtil.LOGOUT:
					LogoutDialog();
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 注销对话框
	 */
	private void LogoutDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("注销登录");
		builder.setMessage("确定注销登录吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (!MainActivity.mInstance.isFinishing()) {
					MainActivity.mInstance.finish();
				}
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	/**
	 * 返回键监听
	 */
	public void onBackPressed() {
		// 如果界面的path菜单没有关闭时,先将path菜单关闭,否则则判断两次返回时间间隔,小于两秒则退出程序
		if (mRoot.getScreenState() == MyViewGroup.SCREEN_STATE_OPEN) {
			if (mDesktop.getUgcIsShowing()) {
				mDesktop.closeUgc();
			} else {
				exit();
			}
		} else {
			exit();
		}

	}

	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
	private void exit() {
		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
			Toast.makeText(this, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	public void open() {
		if (mRoot.getScreenState() == MyViewGroup.SCREEN_STATE_CLOSE) {
			mRoot.open();
		}
	}
}
