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

public class OrderActivity extends SherlockActivity {
	private Intent intent;
	private ListView listView = null;
	private SimpleAdapter itemsAdapter;
	private ProgressDialog dialog;
	private static String orderResult = null;
	private Context context = null;
	private List<Map<String, String>> orderList;
	private ObtainOrderThread myObtainOrderThread = null;
	private String marcno = null;
	private String user = null;
	private String password = null;
	private String number = null;
	private String location = null;
	private String red = null;
	private boolean flag = true;
	private static final int MSG_UPDATE_ORDER_LIST = 14;
	private static final int MSG_SHOW_ORDER_RESULT = 15;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_order);
		context = this;

		listView = (ListView) findViewById(R.id.lv_order);
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
		if (myObtainOrderThread == null) {
			myObtainOrderThread = new ObtainOrderThread();
			flag = true;
			myObtainOrderThread.start();
			myObtainOrderThread.interrupt();
			myObtainOrderThread = null;
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
				number = map.get("number");
				location = map.get("location");
				red = map.get("red");
				if("".equals(red)){
					Toast.makeText(context, "(+﹏+)~亲，不能预约了~",Toast.LENGTH_SHORT).show();
					return;
				}
				OrderThread orderThread = new OrderThread();
				orderThread.start();
				orderThread.interrupt();
				
			}
		});
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case MSG_UPDATE_ORDER_LIST:

				itemsAdapter = new SimpleAdapter(OrderActivity.this,
						orderList, R.layout.list_item_order,
						new String[] { "number","place", "location","red"}, new int[] {
								R.id.tv_list_item_order_number,
								R.id.tv_list_item_order_place,
								R.id.tv_list_item_order_location,
								R.id.tv_list_item_order_red});
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

	class ObtainOrderThread extends Thread {

		public void run() {
			while (flag) {
				orderList = new ArrayList<Map<String, String>>();

				try {
					orderList = new OrderLoader().load(user, password, marcno);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				mHandler.sendEmptyMessage(MSG_UPDATE_ORDER_LIST);

				mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

				flag = false;
			}
		}
	}
	Handler outerHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SHOW_ORDER_RESULT:
				Toast.makeText(context, orderResult,Toast.LENGTH_SHORT).show();
			}
		}
	};
	class OrderThread extends Thread {

		public void run() {
				try {
					orderResult = new Order(user, password, marcno, number, location).doOrder();
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
				outerHandler.sendEmptyMessage(MSG_SHOW_ORDER_RESULT);
		}
	}

}

