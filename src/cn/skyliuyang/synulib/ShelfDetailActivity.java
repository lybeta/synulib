package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class ShelfDetailActivity extends SherlockActivity {
	private ActionBar ab;
	private Intent intent;
	private ListView listView = null;
	private SimpleAdapter itemsAdapter;
	private ProgressDialog dialog;
	private Context context;
	private List<Map<String, String>> shelfDetailList;
	private ObtainShelfDetailThread myObtainShelfDetailThread = null;
	private String url = null;
	private String user = null;
	private String password = null;
	private boolean flag = true;
	private static final int MSG_UPDATE_SHELFDETAIL_LIST = 14;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_shelf_detail);
		context = this;
		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.lv_shelf_detail);
		intent = getIntent();
		user = intent.getStringExtra("user");
		password = intent.getStringExtra("password");
		url = intent.getStringExtra("url");
		// 设置正在处理窗口
		dialog = new ProgressDialog(this);
		dialog.setMessage("~~o(>_<)o ~~努力加载中...");
		dialog.setCancelable(false);
		dialog.show();
		// 开始动态加载热门搜索线程
		if (myObtainShelfDetailThread == null) {
			myObtainShelfDetailThread = new ObtainShelfDetailThread();
			flag = true;
			myObtainShelfDetailThread.start();
			myObtainShelfDetailThread.interrupt();
			myObtainShelfDetailThread = null;
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) arg0
						.getItemAtPosition(arg2);
				Intent intent = new Intent();
				intent.putExtra("url", map.get("url"));
				intent.setClass(context, BookDetailActivity.class);
				startActivity(intent);

			}
		});
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case MSG_UPDATE_SHELFDETAIL_LIST:

				itemsAdapter = new SimpleAdapter(ShelfDetailActivity.this,
						shelfDetailList, R.layout.list_item_shelf_detail,
						new String[] { "name", "writer", "publisher", "date",
								"number", "url" }, new int[] {
								R.id.tv_list_item_shelf_detail_name,
								R.id.tv_list_item_shelf_detail_writer,
								R.id.tv_list_item_shelf_detail_publisher,
								R.id.tv_list_item_shelf_detail_date,
								R.id.tv_list_item_shelf_detail_number,
								R.id.tv_list_item_shelf_detail_url });
				listView.setAdapter(itemsAdapter);
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

	class ObtainShelfDetailThread extends Thread {

		public void run() {
			while (flag) {
				shelfDetailList = new ArrayList<Map<String, String>>();

				try {
					shelfDetailList = new ShelfDetailLoader().load(user,
							password, url);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				mHandler.sendEmptyMessage(MSG_UPDATE_SHELFDETAIL_LIST);

				mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

				flag = false;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
