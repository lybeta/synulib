package cn.skyliuyang.synulib;

import java.io.IOException;

import org.apache.http.ParseException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AnnouncementActivity  extends SherlockActivity {
	private TextView tv_announcement_content;
	private Intent intent = null;
	private String url = null;
	private ProgressDialog dialog;
	public Handler handler;
	private String content = null;
	private static Context context;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;
	private static final int MSG_UPDATE_ANNOUNCEMENT_CONTENT = 18;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_announcement);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		tv_announcement_content = (TextView) findViewById(R.id.tv_announcement_content);
		intent = getIntent();
		url = intent.getStringExtra("url");
		dialog = new ProgressDialog(context);
		dialog.setMessage("~~o(>_<)o ~~努力加载中...");
		dialog.setCancelable(false);
		dialog.show();
		
		handler = new Handler() {

			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case MSG_UPDATE_ANNOUNCEMENT_CONTENT:
					tv_announcement_content.setText(content);
					
					break;
				case MSG_DISMISS_PROGRESS_DIALOG:
					// 关闭正在处理窗口
					dialog.dismiss();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		new ObtainAnnouncementThread().start();
	}
	class ObtainAnnouncementThread extends Thread {

		public void run() {

				// 逐项添加程序，并发送消息更新ListView列表。
				try {
					content = new AnnouncementContentLoader().load(url);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				handler.sendEmptyMessage(MSG_UPDATE_ANNOUNCEMENT_CONTENT);
				handler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

		}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
