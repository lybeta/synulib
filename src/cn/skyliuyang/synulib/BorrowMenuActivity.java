package cn.skyliuyang.synulib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class BorrowMenuActivity extends SherlockActivity {

	private Button btn_borrowmenu_renew;
	private Button btn_borrowmenu_detail;
	private String code = null;
	private String url = null;
	private String renewResult = null;
	private Intent intent;
	private Context context = null;
	private static final int MSG_SHOW_RENEW_RESULT = 24;
	private ProgressDialog renewDialog = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_borrow_menu);
		intent = getIntent();
		context = this;
		code = intent.getStringExtra("code");
		url = intent.getStringExtra("url");
		btn_borrowmenu_renew = (Button) findViewById(R.id.btn_borrow_renew);
		btn_borrowmenu_renew.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				renewDialog = new ProgressDialog(context);
				renewDialog.setMessage("O(¡É_¡É)O~Ðø½èÖÐ...");
				renewDialog.show();
				RenewThread renewThread = new RenewThread();
				renewThread.start();
				renewThread.interrupt();
				
			}
		});
		btn_borrowmenu_detail = (Button) findViewById(R.id.btn_borrow_detail);
		btn_borrowmenu_detail.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("url",url);
				intent.setClass(context, BookDetailActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	Handler outerHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SHOW_RENEW_RESULT:
				renewDialog.dismiss();
				Toast.makeText(context, renewResult,Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	};
	class RenewThread extends Thread {

		public void run() {
				renewResult = new QuickRenew(code).doQuickRenew();
				outerHandler.sendEmptyMessage(MSG_SHOW_RENEW_RESULT);
		}
	}
}
