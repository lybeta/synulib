package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BookDetailActivity extends SherlockActivity {
	private TextView tv_bookdetail_name = null;
	private TextView tv_bookdetail_writer = null;
	private TextView tv_bookdetail_publisher = null;
	private TextView tv_bookdetail_isbn = null;
	private Button btn_bookdetail_order = null;
	private Button btn_bookdetail_collect = null;
	private ListView lv_bookdetail = null;
	private SharedPreferences sharedPre = null;
	private static String user = null;
	private static String password = null;
	private Intent intent = null;
	private Book book = null;
	private String url = null;
	private SimpleAdapter itemsAdapter;
	private ProgressDialog dialog;
	public Handler handler;
	private List<Map<String, String>> list = null;
	private static Context context;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;
	private static final int MSG_UPDATE_BOOKDETAIL = 18;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_bookdetail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		tv_bookdetail_name = (TextView) findViewById(R.id.tv_bookdetail_name);
		tv_bookdetail_writer = (TextView) findViewById(R.id.tv_bookdetail_writer);
		tv_bookdetail_publisher = (TextView) findViewById(R.id.tv_bookdetail_publisher);
		tv_bookdetail_isbn = (TextView) findViewById(R.id.tv_bookdetail_isbn);
		sharedPre = getSharedPreferences("lib", Activity.MODE_PRIVATE);
		
		btn_bookdetail_order = (Button) findViewById(R.id.btn_bookdetail_order);
		btn_bookdetail_order.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				user = sharedPre.getString("user", "");
				password = sharedPre.getString("password", "");
				if ("".equals(user)) {
					Intent intent = new Intent();
					intent.setClass(context, LoginActivity.class);
					startActivity(intent);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(context, OrderActivity.class);
				intent.putExtra("user", user);
				intent.putExtra("password", password);
				intent.putExtra("marcno", url.substring(46));
				startActivity(intent);
			}
		});
		btn_bookdetail_collect = (Button) findViewById(R.id.btn_bookdetail_collect);
		btn_bookdetail_collect.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				user = sharedPre.getString("user", "");
				password = sharedPre.getString("password", "");
				if ("".equals(user)) {
					Intent intent = new Intent();
					intent.setClass(context, LoginActivity.class);
					startActivity(intent);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(context, CollectActivity.class);
				intent.putExtra("user", user);
				intent.putExtra("password", password);
				intent.putExtra("marcno", url.substring(46));
				startActivity(intent);
			}
		});
		
		lv_bookdetail = (ListView) findViewById(R.id.lv_bookdetail);
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
				case MSG_UPDATE_BOOKDETAIL:
					tv_bookdetail_name.setText(book.getName());
					tv_bookdetail_writer.setText(book.getWriter());
					tv_bookdetail_publisher.setText(book.getPublisher());
					tv_bookdetail_isbn.setText(book.getIsbn());
					list = book.getBookList();
					itemsAdapter = new SimpleAdapter(context,
							list, R.layout.list_item_bookdetail,
							new String[] { "number1", "number2","place1","place2","state" }, new int[] {
									R.id.tv_list_item_bookdetail_number1,
									R.id.tv_list_item_bookdetail_number2,
									R.id.tv_list_item_bookdetail_place1,
									R.id.tv_list_item_bookdetail_place2,
									R.id.tv_list_item_bookdetail_state});
					lv_bookdetail.setAdapter(itemsAdapter);
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
		new ObtainBookDetailThread().start();
	}
	class ObtainBookDetailThread extends Thread {

		public void run() {

				// 逐项添加程序，并发送消息更新ListView列表。
				try {
					book = new BookDetailLoader().load(url);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 下载数据（耗时的操作）

				handler.sendEmptyMessage(MSG_UPDATE_BOOKDETAIL);
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
