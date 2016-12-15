package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.pcm.activity.MainActivity;
import com.sjtu.pcm.anim.UgcAnimations;
import com.sjtu.pcm.util.ViewUtil;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;

/**
 * 菜单界面
 * 
 * @author CaoYuting
 * 
 */
@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class Desktop {
	private Context mContext;
	// 当前界面的View
	private View mDesktop;
	private RelativeLayout mTopLayout;

	private TextView mName;
	private TextView mSig;
	private ImageView mPortrait;
	private ListView mDisplay;
	private View mUgcView;
	private RelativeLayout mUgcLayout;
	private ImageView mUgc;
	private ImageView mUgcBg;

	// 悬浮球4个按钮
	private ImageView mUgcVoice;
	private ImageView mUgcPhoto;
	private ImageView mUgcRecord;
	private ImageView mUgcLbs;

	private ImageView mSetUp;

	// 桌面适配器
	private DesktopAdapter mAdapter;

	// 判断当前的path菜单是否已经显示
	private boolean mUgcIsShowing = false;
	// 接口对象,用来修改显示的View
	private onChangeViewListener mOnChangeViewListener;
	
	private MyApplication mapp;

	public Desktop(Context context, Activity activity) {
		mContext = context;
		// 绑定布局到当前View
		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		
		mapp = (MyApplication) activity.getApplication();
		
		findViewById();
		setListener();
		init();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mTopLayout = (RelativeLayout) mDesktop
				.findViewById(R.id.desktop_top_layout);
		mName = (TextView) mDesktop.findViewById(R.id.desktop_name);
		mSig = (TextView) mDesktop.findViewById(R.id.desktop_sig);
		mPortrait = (ImageView) mDesktop.findViewById(R.id.desktop_avatar);
		mDisplay = (ListView) mDesktop.findViewById(R.id.desktop_display);

		mUgcView = mDesktop.findViewById(R.id.desktop_ugc);
		mUgcLayout = (RelativeLayout) mUgcView.findViewById(R.id.ugc_layout);
		mUgc = (ImageView) mUgcView.findViewById(R.id.ugc);
		mUgcBg = (ImageView) mUgcView.findViewById(R.id.ugc_bg);
		mUgcVoice = (ImageView) mUgcView.findViewById(R.id.ugc_voice);
		mUgcPhoto = (ImageView) mUgcView.findViewById(R.id.ugc_photo);
		mUgcRecord = (ImageView) mUgcView.findViewById(R.id.ugc_record);
		mUgcLbs = (ImageView) mUgcView.findViewById(R.id.ugc_lbs);
		mSetUp = (ImageView) mDesktop.findViewById(R.id.desktop_set_up);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		// 头布局监听
		mTopLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 切换界面View为用户首页
				if (mOnChangeViewListener != null) {
					mOnChangeViewListener.onChangeView(ViewUtil.USER);
					mAdapter.setChoose(-1);
					mAdapter.notifyDataSetChanged();

				}
			}
		});
		// Path监听
		mUgcView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 判断是否已经显示,显示则关闭并隐藏
				if (mUgcIsShowing) {
					mUgcIsShowing = false;
					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
					return true;
				}
				return false;
			}
		});
		// Path监听
		mUgc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 判断是否显示,已经显示则隐藏,否则则显示
				mUgcIsShowing = !mUgcIsShowing;
				if (mUgcIsShowing) {
					UgcAnimations.startOpenAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
				} else {
					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
				}
			}
		});
		// Path 名片设计监听
		mUgcVoice.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {

						mOnChangeViewListener.onChangeView(ViewUtil.CARDDESIGN);
						closeUgc();
					}
				});
				mUgcVoice.startAnimation(anim);
			}
		});
		// Path 名片交换监听
		mUgcPhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {

						mOnChangeViewListener.onChangeView(ViewUtil.CAREXDCHANGE);
						closeUgc();
					}
				});
				mUgcPhoto.startAnimation(anim);
			}
		});
		// Path 人脉排程监听
		mUgcRecord.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						mOnChangeViewListener.onChangeView(ViewUtil.SCHEDULE);
						closeUgc();
					}
				});
				mUgcRecord.startAnimation(anim);
			}
		});
		// Path 名片识别监听
		mUgcLbs.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						mOnChangeViewListener.onChangeView(ViewUtil.RECOGNIZE);
						closeUgc();
					}
				});
				mUgcLbs.startAnimation(anim);
			}
		});
		// 选项按钮监听
		mSetUp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 目前跳转到MainActivity
				mContext.startActivity(new Intent(mContext, MainActivity.class));
			}
		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mName.setText(mapp.getUser().getAccount());
		mSig.setText("Welcome !");

		if(mapp.getUser().getGender() == "男") {
			mPortrait.setImageResource(R.drawable.portrait_1);
		} else if(mapp.getUser().getGender()== "女"){
			mPortrait.setImageResource(R.drawable.portrait_2);
		}

		mAdapter = new DesktopAdapter(mContext);
		mDisplay.setAdapter(mAdapter);

	}

	/**
	 * 界面修改方法
	 */
	public void setOnChangeViewListener(
			onChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}

	/**
	 * 获取Path菜单显示状态
	 */
	public boolean getUgcIsShowing() {
		return mUgcIsShowing;
	}

	/**
	 * 关闭Path菜单
	 */
	public void closeUgc() {
		mUgcIsShowing = false;
		UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc, 500);
	}

	/**
	 * 获取菜单界面
	 */
	public View getView() {
		return mDesktop;
	}

	/**
	 * 切换显示界面的接口
	 */
	public interface onChangeViewListener {
		void onChangeView(int arg0);
	}

	/**
	 * 桌面适配器
	 */
	private class DesktopAdapter extends BaseAdapter {

		private Context mContext;
		private String[] mName = { "名片设计", "名片交换", "人脉排程", "名片识别", "联系我们",
				"注销登录" };
		private int[] mIcon = { R.drawable.card_design, R.drawable.card_exchange,
				R.drawable.schedule, R.drawable.recognize, R.drawable.contact,
				R.drawable.logout };
		private int[] mIconPressed = { R.drawable.card_design_h, R.drawable.card_exchange_h,
				R.drawable.schedule_h, R.drawable.recognize_h,
				R.drawable.contact_h, R.drawable.logout_h };
		private int mChoose = 0;

		private DesktopAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return 6;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		private void setChoose(int choose) {
			mChoose = choose;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.desktop_item, null);
				holder = new ViewHolder();
				holder.layout = (LinearLayout) convertView
						.findViewById(R.id.desktop_item_layout);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.desktop_item_icon);
				holder.name = (TextView) convertView
						.findViewById(R.id.desktop_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(mName[position]);
			if (position == mChoose) {
				holder.name.setTextColor(Color.parseColor("#ffffffff"));
				holder.icon.setImageResource(mIconPressed[position]);
				holder.layout.setBackgroundColor(Color.parseColor("#20000000"));
			} else {
				holder.name.setTextColor(Color.parseColor("#7fffffff"));
				holder.icon.setImageResource(mIcon[position]);
				//noinspection ResourceType
				holder.layout.setBackgroundResource(Color.parseColor("#00000000"));
			}
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (mOnChangeViewListener != null) {
						switch (position) {

						case ViewUtil.CARDDESIGN:
							mOnChangeViewListener.onChangeView(ViewUtil.CARDDESIGN);
							break;
						case ViewUtil.CAREXDCHANGE:
							mOnChangeViewListener.onChangeView(ViewUtil.CAREXDCHANGE);
							break;
						case ViewUtil.SCHEDULE:
							mOnChangeViewListener
									.onChangeView(ViewUtil.SCHEDULE);
							break;
						case ViewUtil.RECOGNIZE:
							mOnChangeViewListener
									.onChangeView(ViewUtil.RECOGNIZE);
							break;
						case ViewUtil.CONTACT:
							mOnChangeViewListener
									.onChangeView(ViewUtil.CONTACT);
							break;
						case ViewUtil.LOGOUT:
							mOnChangeViewListener.onChangeView(ViewUtil.LOGOUT);
							break;
						default:
							mOnChangeViewListener.onChangeView(ViewUtil.CARDDESIGN);
							break;
						}
						mChoose = position;
						notifyDataSetChanged();
					}

				}
			});
			return convertView;
		}

		class ViewHolder {
			LinearLayout layout;
			ImageView icon;
			TextView name;
		}
	}

}
