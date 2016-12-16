package com.sjtu.pcm.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.sjtu.pcm.MyApplication;
import com.sjtu.pcm.R;
import com.sjtu.pcm.activity.card_design.CardModifyActivity;
import com.sjtu.pcm.anim.MyViewGroup.OnOpenListener;
import com.sjtu.pcm.entity.CardEntity;
import com.sjtu.pcm.entity.CardList;
import com.sjtu.pcm.util.HttpUtil;
import com.sjtu.pcm.zxing.EncodingHandler;

/**
 * 名片设计类
 *
 * 
 */
public class CardDesign {
	private Context cDContext;
	// 当前界面的View
	private View mHome;
	// 布局控件
	private Button mMenu;
	//private Button cDRefresh;
	private Button cDEdit;

	private TextView cName;
	private TextView cCompany;
	private TextView cJob;
	private TextView cCNumber;
	private TextView cCAddress;
	private TextView cFax;
	private TextView cCEmail;

	private Button cQrCodeBtn;
	private ImageView qrImgImageView;

	private String contentString;

	private MyApplication mApp;
	private OnOpenListener mOnOpenListener;

	private TextView mTopText;

	private CardEntity cardEntity = new CardEntity();

	@SuppressLint("InflateParams")
	public CardDesign(Context context, Activity activity) {
		cDContext = context;
		// 绑定布局到当前View
		mHome = LayoutInflater.from(context).inflate(R.layout.carddesign, null);

		mApp = (MyApplication) activity.getApplication();
		Log.i("user_id", mApp.getUser().getId()+"");

		findViewById();
		setListener();
		init();
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.menu);
		cDEdit = (Button) mHome.findViewById(R.id.card_edit);
		mTopText = (TextView) mHome.findViewById(R.id.top_text);
		cName = (TextView) mHome.findViewById(R.id.card_name);
		cCompany = (TextView) mHome.findViewById(R.id.card_company);
		cJob = (TextView) mHome.findViewById(R.id.card_job);
		cCNumber = (TextView) mHome.findViewById(R.id.card_c_number);
		cCAddress = (TextView) mHome.findViewById(R.id.card_c_address);
		cFax = (TextView) mHome.findViewById(R.id.card_fax);
		cCEmail = (TextView) mHome.findViewById(R.id.card_c_email);

		cQrCodeBtn = (Button) mHome.findViewById(R.id.qr_code);
		qrImgImageView = (ImageView) mHome.findViewById(R.id.qr_image);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		mMenu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});

		// 编辑监听
		cDEdit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到编辑界面
				Intent intent = new Intent();
				intent.setClass(cDContext, CardModifyActivity.class);
				cDContext.startActivity(intent);
			}
		});

		// 生成二维码监听
		cQrCodeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					if (!contentString.equals("")) {
						//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
						qrImgImageView.setImageBitmap(qrCodeBitmap);

					}else {
						Toast.makeText(cDContext, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}

				} catch (WriterException e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		mTopText.setText("名片设计");
		Log.e("init", "cardDesign");
		new RMPHelper().execute(mApp.getCardUrl() + "?Card.user_id=" + mApp.getUser().getId());
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	/**
	 * 获取界面
	 * 
	 */
	public View getView() {
		return mHome;
	}

	private class RMPHelper extends AsyncTask<String, Void, String> {

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

			cName.setText(cardEntity.getName());
			cCompany.setText(cardEntity.getCompany());
			cJob.setText(cardEntity.getJob());
			cCNumber.setText(cardEntity.getC_number());
			cCAddress.setText(cardEntity.getC_address());
			cFax.setText(cardEntity.getC_fax());
			cCEmail.setText(cardEntity.getC_email());

			contentString = "名片\n"
					+ "姓名: " + cardEntity.getName() + "\n"
					+ "公司: " + cardEntity.getCompany() + "\n"
					+ "职位: " + cardEntity.getJob() + "\n"
					+ "公司电话: " + cardEntity.getC_number() + "\n"
					+ "公司地址: " + cardEntity.getC_address() + "\n"
					+ "传真:" + cardEntity.getC_fax() + "\n"
			   		+ "公司邮箱: " + cardEntity.getC_email() + "\n";
		}

	}

}
