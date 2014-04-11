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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

public class HotBooksActivity extends SherlockFragmentActivity implements
		ActionBar.OnNavigationListener {
	static final int NUM_ITEMS = 4;
	private ActionBar ab;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private static Context context;
	private Intent intent;
	private static int hotBooksNum;
	private static final int MSG_UPDATE_HOTBOOKSONE_LIST = 11;
	private static final int MSG_UPDATE_HOTBOOKSTWO_LIST = 12;
	private static final int MSG_UPDATE_HOTBOOKSTHREE_LIST = 13;
	private static final int MSG_UPDATE_HOTBOOKSFOUR_LIST = 14;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		intent = getIntent();
		hotBooksNum = Integer.parseInt(intent.getStringExtra("hotbooksnum"));
		context = this;
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				Log.d("k", "onPageSelected - " + arg0);
				// activity从1到2滑动，2被加载后掉用此方法
				ab.setSelectedNavigationItem(arg0);
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.d("k", "onPageScrolled - " + arg0);
				// 从1到2滑动，在1滑动前调用
			}

			public void onPageScrollStateChanged(int arg0) {
				Log.d("k", "onPageScrollStateChanged - " + arg0);
				// 状态有三个0空闲，1是增在滑行中，2目标加载完毕
				/**
				 * Indicates that the pager is in an idle, settled state. The
				 * current page is fully in view and no animation is in
				 * progress.
				 */
				// public static final int SCROLL_STATE_IDLE = 0;
				/**
				 * Indicates that the pager is currently being dragged by the
				 * user.
				 */
				// public static final int SCROLL_STATE_DRAGGING = 1;
				/**
				 * Indicates that the pager is in the process of settling to a
				 * final position.
				 */
				// public static final int SCROLL_STATE_SETTLING = 2;

			}
		});

		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ab.setDisplayShowTitleEnabled(false);
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.hot_books_strings,
				R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		ab.setListNavigationCallbacks(list, (OnNavigationListener) context);

		mPager.setCurrentItem(hotBooksNum);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Intent intent = new Intent(this, MainActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getCount() {
			return NUM_ITEMS;
		}

		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(position);
		}
	}

	public static class ArrayListFragment extends SherlockListFragment {
		int mNum;
		private SimpleAdapter itemsAdapter;
		private ProgressDialog dialog;
		private List<Map<String, String>> mHotBooksList;
		private ObtainHotSearchThread myObtainHotBooksThread = null;
		private boolean flag = true;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			LinearLayout mLinearLayout = null;
			mLinearLayout = (LinearLayout) inflater.inflate(
					R.layout.view_hotbooks, container, false);

			return mLinearLayout;
		}

		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// 设置正在处理窗口
			dialog = new ProgressDialog(context);
			dialog.setMessage("~~o(>_<)o ~~努力加载中...");
			dialog.setCancelable(false);

			dialog.show();
			// 开始动态加载热门搜索线程
			if (myObtainHotBooksThread == null) {
				myObtainHotBooksThread = new ObtainHotSearchThread();
				flag = true;
				myObtainHotBooksThread.start();
				myObtainHotBooksThread.interrupt();
				myObtainHotBooksThread = null;
			}

		}

		Handler mHandler = new Handler() {

			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {

				case MSG_UPDATE_HOTBOOKSONE_LIST:

					itemsAdapter = new SimpleAdapter(
							getActivity(),
							mHotBooksList,
							R.layout.list_item_hotbooksone,
							new String[] { "name", "number", "holding", "rate","url" },
							new int[] { R.id.tv_list_item_hotbooksone_name,
									R.id.tv_list_item_hotbooksone_number,
									R.id.tv_list_item_hotbooksone_holding,
									R.id.tv_list_item_hotbooksone_rate,
									R.id.tv_list_item_hotbooksone_url });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_HOTBOOKSTWO_LIST:

					itemsAdapter = new SimpleAdapter(
							getActivity(),
							mHotBooksList,
							R.layout.list_item_hotbookstwo,
							new String[] { "name", "writer", "number", "times","url" },
							new int[] { R.id.tv_list_item_hotbookstwo_name,
									R.id.tv_list_item_hotbookstwo_writer,
									R.id.tv_list_item_hotbookstwo_number,
									R.id.tv_list_item_hotbookstwo_times,
									R.id.tv_list_item_hotbookstwo_url });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_HOTBOOKSTHREE_LIST:

					itemsAdapter = new SimpleAdapter(
							getActivity(),
							mHotBooksList,
							R.layout.list_item_hotbooksthree,
							new String[] { "name", "writer", "number", "times","url" },
							new int[] { R.id.tv_list_item_hotbooksthree_name,
									R.id.tv_list_item_hotbooksthree_writer,
									R.id.tv_list_item_hotbooksthree_number,
									R.id.tv_list_item_hotbooksthree_times,
									R.id.tv_list_item_hotbooksthree_url });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_HOTBOOKSFOUR_LIST:

					itemsAdapter = new SimpleAdapter(
							getActivity(),
							mHotBooksList,
							R.layout.list_item_hotbooksfour,
							new String[] { "name", "writer", "number", "times","url" },
							new int[] { R.id.tv_list_item_hotbooksfour_name,
									R.id.tv_list_item_hotbooksfour_writer,
									R.id.tv_list_item_hotbooksfour_number,
									R.id.tv_list_item_hotbooksfour_times,
									R.id.tv_list_item_hotbooksfour_url });
					setListAdapter(itemsAdapter);
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

		class ObtainHotSearchThread extends Thread {

			public void run() {
				while (flag) {
					mHotBooksList = new ArrayList<Map<String, String>>();

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						if (0 == mNum) {
							mHotBooksList = new HotBooksOneLoader().load();
						} else if (1 == mNum) {
							mHotBooksList = new HotBooksTwoLoader().load();
						} else if (2 == mNum) {
							mHotBooksList = new HotBooksThreeLoader().load();
						} else if (3 == mNum) {
							mHotBooksList = new HotBooksFourLoader().load();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 下载数据（耗时的操作）

					if (0 == mNum) {
						mHandler.sendEmptyMessage(MSG_UPDATE_HOTBOOKSONE_LIST);
					} else if (1 == mNum) {
						mHandler.sendEmptyMessage(MSG_UPDATE_HOTBOOKSTWO_LIST);
					} else if (2 == mNum) {
						mHandler.sendEmptyMessage(MSG_UPDATE_HOTBOOKSTHREE_LIST);
					} else if (3 == mNum) {
						mHandler.sendEmptyMessage(MSG_UPDATE_HOTBOOKSFOUR_LIST);
					}

					mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

					flag = false;
				}
			}
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) l
					.getItemAtPosition(position);
			Intent intent = new Intent();
			intent.putExtra("url", "http://58.154.49.3:8080/opac/"+map.get("url"));
			intent.setClass(context, BookDetailActivity.class);
			startActivity(intent);

		}
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		mPager.setCurrentItem(itemPosition);
		return false;
	}
}
