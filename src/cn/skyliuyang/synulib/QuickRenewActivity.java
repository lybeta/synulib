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

public class QuickRenewActivity extends SherlockActivity{
	private static String codeOfQuickRenew = null;
	private static String quickRenewResult = null;
	private ProgressDialog quickRenewDialog = null;
	private Context context = null;
	private Button btn_quickrenew;
	private static final int REQUEST_CODE_OF_QUICKRENEW = 23;
	private static final int MSG_SHOW_QUICKRENEW_RESULT = 24;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_quickrenew);
		context = this;
		btn_quickrenew = (Button) findViewById(R.id.btn_quickrenew);
		btn_quickrenew.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, CameraTestActivity.class);
				startActivityForResult(intent, REQUEST_CODE_OF_QUICKRENEW);
			}
		});
		
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		
		case REQUEST_CODE_OF_QUICKRENEW:
			// 返回的条形码数据
			codeOfQuickRenew = data.getStringExtra("Code");
			if (codeOfQuickRenew.equals("") | codeOfQuickRenew.equals(null)) {
				Toast.makeText(context, "╮(╯_╰)╭ 亲，条码没扫描到啊！",
						Toast.LENGTH_SHORT).show();
			} else if (codeOfQuickRenew.length()>9){
				Toast.makeText(context, "╮(╯_╰)╭ 亲，要扫描图书馆自己贴的条码啊！",
						Toast.LENGTH_SHORT).show();
			}else {
				quickRenewDialog = new ProgressDialog(context);
				quickRenewDialog.setMessage("O(∩_∩)O~快速续借中...");
				quickRenewDialog.show();
				QuickRenewThread quickRenewThread = new QuickRenewThread();
				quickRenewThread.start();
				quickRenewThread.interrupt();
			}
		}
		
	}
	
	Handler outerHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SHOW_QUICKRENEW_RESULT:
				quickRenewDialog.dismiss();
				Toast.makeText(context, quickRenewResult,Toast.LENGTH_SHORT).show();
			}
		}
	};
	class QuickRenewThread extends Thread {

		public void run() {
				quickRenewResult = new QuickRenew(codeOfQuickRenew).doQuickRenew();
				outerHandler.sendEmptyMessage(MSG_SHOW_QUICKRENEW_RESULT);
		}
	}
	
}
