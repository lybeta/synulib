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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NewBooksActivity extends SherlockActivity {
	private ActionBar ab;
	private Intent intent;
	private ListView listView = null;
	private SimpleAdapter itemsAdapter;
	private ProgressDialog dialog;
	private List<Map<String, String>> mNewBooksList;
	private ObtainNewBooksThread myObtainNewBooksThread = null;
	private int urlNum;
	private int page;
	private boolean flag = true;
	private Context context;
	private static final int MSG_UPDATE_NEWBOOKS_LIST = 14;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		menu.add(1, 1, 1, R.string.pre_page).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(1, 2, 2, R.string.next_page).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_newbooks);

		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		context = this;
		listView = (ListView) findViewById(R.id.lv_newbooks);
		intent = getIntent();
		urlNum = Integer.parseInt(intent.getStringExtra("urlnum"));
		page = 1;
		// 设置正在处理窗口
		dialog = new ProgressDialog(this);
		dialog.setMessage("~~o(>_<)o ~~努力加载中...");
		dialog.setCancelable(false);
		dialog.show();
		// 开始动态加载热门搜索线程
		if (myObtainNewBooksThread == null) {
			myObtainNewBooksThread = new ObtainNewBooksThread();
			flag = true;
			myObtainNewBooksThread.start();
			myObtainNewBooksThread.interrupt();
			myObtainNewBooksThread = null;
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				@SuppressWarnings("unchecked")
				 HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
				 Intent intent = new Intent();
				 intent.putExtra("url",map.get("url"));
				 intent.setClass(context, BookDetailActivity.class);
				 startActivity(intent);
				
			}
		});
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case MSG_UPDATE_NEWBOOKS_LIST:

				itemsAdapter = new SimpleAdapter(NewBooksActivity.this,
						mNewBooksList, R.layout.list_item_newbooks,
						new String[] { "name", "writer","url" }, new int[] {
								R.id.tv_list_item_newbooks_name,
								R.id.tv_list_item_newbooks_writer,
								R.id.tv_list_item_newbooks_url });
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

	class ObtainNewBooksThread extends Thread {

		public void run() {
			while (flag) {
				mNewBooksList = new ArrayList<Map<String, String>>();

				// 逐项添加程序，并发送消息更新ListView列表。
				try {
					mNewBooksList = new NewBooksLoader().load(urlNum,page);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				mHandler.sendEmptyMessage(MSG_UPDATE_NEWBOOKS_LIST);

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
		case 1:
			page--;
			if(page<1) page = 1;
			// 设置正在处理窗口
			dialog = new ProgressDialog(this);
			dialog.setMessage("~~o(>_<)o ~~努力加载中...");
			dialog.setCancelable(false);
			dialog.show();
			// 开始动态加载热门搜索线程
			if (myObtainNewBooksThread == null) {
				myObtainNewBooksThread = new ObtainNewBooksThread();
				flag = true;
				myObtainNewBooksThread.start();
				myObtainNewBooksThread.interrupt();
				myObtainNewBooksThread = null;
			}
			return true;
		case 2:
			page++;
			// 设置正在处理窗口
			dialog = new ProgressDialog(this);
			dialog.setMessage("~~o(>_<)o ~~努力加载中...");
			dialog.setCancelable(false);
			dialog.show();
			// 开始动态加载热门搜索线程
			if (myObtainNewBooksThread == null) {
				myObtainNewBooksThread = new ObtainNewBooksThread();
				flag = true;
				myObtainNewBooksThread.start();
				myObtainNewBooksThread.interrupt();
				myObtainNewBooksThread = null;
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
