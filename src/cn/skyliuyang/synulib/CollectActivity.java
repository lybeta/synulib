package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class CollectActivity extends SherlockActivity {
	private Intent intent;
	private ListView listView = null;
	private SimpleAdapter itemsAdapter;
	private ProgressDialog dialog;
	private static String collectResult = null;
	private Context context = null;
	private List<Map<String, String>> shelfList;
	private ObtainShelfThread myObtainShelfThread = null;
	private String marcno = null;
	private String user = null;
	private String password = null;
	private String name = null;
	private String classid = null;
	private boolean flag = true;
	private static final int MSG_UPDATE_SHELF_LIST = 14;
	private static final int MSG_SHOW_COLLECT_RESULT = 15;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_collect);
		context = this;

		listView = (ListView) findViewById(R.id.lv_collect);
		intent = getIntent();
		user = intent.getStringExtra("user");
		password = intent.getStringExtra("password");
		marcno = intent.getStringExtra("marcno");
		
		// 设置正在处理窗口
		dialog = new ProgressDialog(this);
		dialog.setMessage("~~o(>_<)o ~~努力加载中...");
		dialog.setCancelable(false);
		dialog.show();
		// 开始动态加载热门搜索线程
		if (myObtainShelfThread == null) {
			myObtainShelfThread = new ObtainShelfThread();
			flag = true;
			myObtainShelfThread.start();
			myObtainShelfThread.interrupt();
			myObtainShelfThread = null;
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
				classid = map.get("classid");
				CollectThread collectThread = new CollectThread();
				collectThread.start();
				collectThread.interrupt();
				
			}
		});
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case MSG_UPDATE_SHELF_LIST:

				itemsAdapter = new SimpleAdapter(CollectActivity.this,
						shelfList, R.layout.list_item_collect,
						new String[] { "name","classid"}, new int[] {
								R.id.tv_list_item_collect_name,
								R.id.tv_list_item_collect_classid});
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

	class ObtainShelfThread extends Thread {

		public void run() {
			while (flag) {
				shelfList = new ArrayList<Map<String, String>>();

				try {
					shelfList = new OwnLibShelfLoader().load(user, password);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				mHandler.sendEmptyMessage(MSG_UPDATE_SHELF_LIST);

				mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

				flag = false;
			}
		}
	}
	Handler outerHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SHOW_COLLECT_RESULT:
				Toast.makeText(context, collectResult,Toast.LENGTH_SHORT).show();
			}
		}
	};
	class CollectThread extends Thread {

		public void run() {
				try {
					collectResult = new Collect(user, password, marcno, classid).doCollect();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				outerHandler.sendEmptyMessage(MSG_SHOW_COLLECT_RESULT);
		}
	}

}

