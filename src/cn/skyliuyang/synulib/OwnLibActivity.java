package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.HashMap;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class OwnLibActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener {
	static final int NUM_ITEMS = 6;
	private ActionBar ab;
	private MyAdapter adapter;
	private ViewPager pager;
	private static Context context;
	private SharedPreferences sharedPre = null;
	private SharedPreferences.Editor editor = null;
	private static String user = null;
	private static String password = null;
	private static final int MSG_DISMISS_PROGRESS_DIALOG = 17;
	private static final int MSG_UPDATE_BORROW_LIST = 11;
	private static final int MSG_UPDATE_SHELF_LIST = 12;
	private static final int MSG_UPDATE_ORDER_LIST = 13;
	private static final int MSG_UPDATE_RECOMMENDATION_LIST = 14;
	private static final int MSG_UPDATE_SEARCH_LIST = 15;
	private static final int MSG_UPDATE_PAY_LIST = 16;
	private static final int REQUEST_CODE_OF_BARCODESEARCH = 22;

	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu subMenu = menu.addSubMenu("Overflow Item");
		subMenu.add(1, 1, 1, "条码扫描快速续借");
		subMenu.add(1, 2, 2, "新建书架");
		subMenu.add(1, 3, 3, "条码扫描检索预约");
		subMenu.add(1, 4, 4, "注销登陆");

		MenuItem subMenuItem = subMenu.getItem();
		subMenuItem
				.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_light);
		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sharedPre = getSharedPreferences("lib", Activity.MODE_PRIVATE);
		user = sharedPre.getString("user", "");
		password = sharedPre.getString("password", "");
		if ("".equals(user)) {
			Intent intent = new Intent();
			intent.putExtra("pre", "OwnLibActivity");
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		adapter = new MyAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

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
		context = this;

		ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 1; i <= 6; i++) {
			ActionBar.Tab tab = ab.newTab();
			if (1 == i) {
				tab.setText("书刊借阅");
			} else if (2 == i) {
				tab.setText("我的书架");
			} else if (3 == i) {
				tab.setText("预约信息");
			} else if (4 == i) {
				tab.setText("荐购历史");
			} else if (5 == i) {
				tab.setText("检索历史");
			} else if (6 == i) {
				tab.setText("交款历史");
			}
			tab.setTabListener(this);
			ab.addTab(tab);
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
		private List<Map<String, String>> mBorrowList = null;
		private List<Map<String, String>> mShelfList = null;
		private List<Map<String, String>> mOrderList = null;
		private List<Map<String, String>> mRecommendationList = null;
		private List<Map<String, String>> mSearchList = null;
		private List<Map<String, String>> mPayList = null;
		private ObtainBorrowThread mObtainBorrowThread = null;
		private ObtainShelfThread mObtainShelfThread = null;
		private ObtainOrderThread mObtainOrderThread = null;
		private ObtainRecommendationThread mObtainRecommendationThread = null;
		private ObtainSearchThread mObtainSearchThread = null;
		private ObtainPayThread mObtainPayThread = null;
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
			if (0 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view01_borrow, container, false);
			} else if (1 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view02_shelf, container, false);
			} else if (2 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view03_order, container, false);
			} else if (3 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view04_recommendation, container, false);
			} else if (4 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view05_search, container, false);
			} else if (5 == mNum) {
				mLinearLayout = (LinearLayout) inflater.inflate(
						R.layout.view06_pay, container, false);
			}
			return mLinearLayout;
		}

		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// 设置正在处理窗口
			dialog = new ProgressDialog(context);
			dialog.setMessage("~~o(>_<)o ~~努力加载中...");
			dialog.setCancelable(false);
			if (0 == mNum) {
				dialog.show();
				// 开始动态加载公告线程
				if (mObtainBorrowThread == null) {
					mObtainBorrowThread = new ObtainBorrowThread();
					flag = true;
					mObtainBorrowThread.start();
					mObtainBorrowThread.interrupt();
					mObtainBorrowThread = null;
				}
			} else if (1 == mNum) {
				dialog.show();
				// 开始动态加载热门搜索线程
				if (mObtainShelfThread == null) {
					mObtainShelfThread = new ObtainShelfThread();
					flag = true;
					mObtainShelfThread.start();
					mObtainShelfThread.interrupt();
					mObtainShelfThread = null;
				}
			} else if (2 == mNum) {
				dialog.show();
				// 开始动态加载热门借阅线程
				if (mObtainOrderThread == null) {
					mObtainOrderThread = new ObtainOrderThread();
					flag = true;
					mObtainOrderThread.start();
					mObtainOrderThread.interrupt();
					mObtainOrderThread = null;
				}
			} else if (3 == mNum) {
				dialog.show();
				// 开始动态加载分类目录线程
				if (mObtainRecommendationThread == null) {
					mObtainRecommendationThread = new ObtainRecommendationThread();
					flag = true;
					mObtainRecommendationThread.start();
					mObtainRecommendationThread.interrupt();
					mObtainRecommendationThread = null;
				}
			} else if (4 == mNum) {
				dialog.show();
				// 开始动态加载分类目录线程
				if (mObtainSearchThread == null) {
					mObtainSearchThread = new ObtainSearchThread();
					flag = true;
					mObtainSearchThread.start();
					mObtainSearchThread.interrupt();
					mObtainSearchThread = null;
				}
			} else if (5 == mNum) {
				dialog.show();
				// 开始动态加载分类目录线程
				if (mObtainPayThread == null) {
					mObtainPayThread = new ObtainPayThread();
					flag = true;
					mObtainPayThread.start();
					mObtainPayThread.interrupt();
					mObtainPayThread = null;
				}
			}

		}

		Handler mHandler = new Handler() {

			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case MSG_UPDATE_BORROW_LIST:
					itemsAdapter = new SimpleAdapter(getActivity(),
							mBorrowList, R.layout.list_item_ownlib_borrow,
							new String[] { "number", "name", "writer", "date1",
									"date2", "place", "url" }, new int[] {
									R.id.tv_list_item_ownlib_borrow_number,
									R.id.tv_list_item_ownlib_borrow_name,
									R.id.tv_list_item_ownlib_borrow_writer,
									R.id.tv_list_item_ownlib_borrow_date1,
									R.id.tv_list_item_ownlib_borrow_date2,
									R.id.tv_list_item_ownlib_borrow_place,
									R.id.tv_list_item_ownlib_borrow_url });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_SHELF_LIST:
					itemsAdapter = new SimpleAdapter(getActivity(), mShelfList,
							R.layout.list_item_ownlib_shelf, new String[] {
									"name", "count", "date", "remark", "url",
									"deleteurl" }, new int[] {
									R.id.tv_list_item_ownlib_shelf_name,
									R.id.tv_list_item_ownlib_shelf_count,
									R.id.tv_list_item_ownlib_shelf_date,
									R.id.tv_list_item_ownlib_shelf_remark,
									R.id.tv_list_item_ownlib_shelf_url,
									R.id.tv_list_item_ownlib_shelf_deleteurl });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_ORDER_LIST:
					itemsAdapter = new SimpleAdapter(getActivity(), mOrderList,
							R.layout.list_item_ownlib_order, new String[] {
									"number", "name", "writer", "place",
									"date1", "date2", "state" }, new int[] {
									R.id.tv_list_item_ownlib_order_number,
									R.id.tv_list_item_ownlib_order_name,
									R.id.tv_list_item_ownlib_order_writer,
									R.id.tv_list_item_ownlib_order_place,
									R.id.tv_list_item_ownlib_order_date1,
									R.id.tv_list_item_ownlib_order_date2,
									R.id.tv_list_item_ownlib_order_state });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_RECOMMENDATION_LIST:
					itemsAdapter = new SimpleAdapter(
							getActivity(),
							mRecommendationList,
							R.layout.list_item_ownlib_recommendation,
							new String[] { "name", "writer", "extra", "date",
									"state" },
							new int[] {
									R.id.tv_list_item_ownlib_recommendation_name,
									R.id.tv_list_item_ownlib_recommendation_writer,
									R.id.tv_list_item_ownlib_recommendation_extra,
									R.id.tv_list_item_ownlib_recommendation_date,
									R.id.tv_list_item_ownlib_recommendation_state });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_SEARCH_LIST:
					itemsAdapter = new SimpleAdapter(getActivity(),
							mSearchList, R.layout.list_item_ownlib_search,
							new String[] { "time", "content", "url" },
							new int[] { R.id.tv_list_item_ownlib_search_time,
									R.id.tv_list_item_ownlib_search_content,
									R.id.tv_list_item_ownlib_search_url });
					setListAdapter(itemsAdapter);
					break;
				case MSG_UPDATE_PAY_LIST:
					itemsAdapter = new SimpleAdapter(getActivity(), mPayList,
							R.layout.list_item_ownlib_pay, new String[] {
									"number", "name", "date1", "date2", "pay1",
									"pay2", "state" }, new int[] {
									R.id.tv_list_item_ownlib_pay_number,
									R.id.tv_list_item_ownlib_pay_name,
									R.id.tv_list_item_ownlib_pay_date1,
									R.id.tv_list_item_ownlib_pay_date2,
									R.id.tv_list_item_ownlib_pay_pay1,
									R.id.tv_list_item_ownlib_pay_pay2,
									R.id.tv_list_item_ownlib_pay_state });
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

		class ObtainBorrowThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mBorrowList = new OwnLibBorrowLoader().load(user,
								password);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 下载数据（耗时的操作）

					mHandler.sendEmptyMessage(MSG_UPDATE_BORROW_LIST);
					mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

					flag = false;
				}
			}
		}

		class ObtainShelfThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mShelfList = new OwnLibShelfLoader().load(user,
								password);
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

		class ObtainOrderThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mOrderList = new OwnLibOrderLoader().load(user,
								password);
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

		class ObtainRecommendationThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mRecommendationList = new OwnLibRecommendationLoader()
								.load(user, password);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 下载数据（耗时的操作）

					mHandler.sendEmptyMessage(MSG_UPDATE_RECOMMENDATION_LIST);
					mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

					flag = false;
				}
			}
		}

		class ObtainSearchThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mSearchList = new OwnLibSearchLoader().load(user,
								password);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 下载数据（耗时的操作）

					mHandler.sendEmptyMessage(MSG_UPDATE_SEARCH_LIST);
					mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

					flag = false;
				}
			}
		}

		class ObtainPayThread extends Thread {

			public void run() {
				while (flag) {

					// 逐项添加程序，并发送消息更新ListView列表。
					try {
						mPayList = new OwnLibPayLoader().load(user, password);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 下载数据（耗时的操作）

					mHandler.sendEmptyMessage(MSG_UPDATE_PAY_LIST);
					mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);

					flag = false;
				}
			}
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			if (0 == mNum) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) l
						.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("code", map.get("number"));
				intent.putExtra("url",
						"http://58.154.49.3:8080/opac/" + map.get("url"));
				intent.setClass(context, BorrowMenuActivity.class);
				startActivity(intent);
			} else if (1 == mNum) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) l
						.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("user",user);
				intent.putExtra("password",password);
				intent.putExtra("url",map.get("url"));
				intent.setClass(context, ShelfDetailActivity.class);
				startActivity(intent);
			} else if (2 == mNum) {

			} else if (3 == mNum) {

			} else if (4 == mNum) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) l
						.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("url",map.get("url"));
				intent.setClass(context, HotSearchActivity.class);
				startActivity(intent);
			}

		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case REQUEST_CODE_OF_BARCODESEARCH:
				// 返回的条形码数据
				String code = data.getStringExtra("Code");
				if (code.equals("") | code.equals(null)) {
					Toast.makeText(context, "╮(╯_╰)╭ 亲，条码没扫描到啊！",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					intent.putExtra(
							"url",
							"http://58.154.49.3:8080/opac/openlink.php?title=&publisher=&author=&isbn="
									+ code
									+ "&series=&callno=&keyword=&year=&doctype=ALL&lang_code=ALL&displaypg=20&sort=CATA_DATE&orderby=desc&showmode=list&dept=ALL");
					intent.setClass(context, HotSearchActivity.class);
					startActivity(intent);
				}
				break;
			}
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case 1:
			intent.setClass(context, QuickRenewActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent();
			intent.setClass(context, ShelfCreateActivity.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent();
			intent.setClass(context, CameraTestActivity.class);
			startActivityForResult(intent, REQUEST_CODE_OF_BARCODESEARCH);
			break;
		// case 4:
		// intent = new Intent();
		// intent.setClass(context, RecommendationActivity.class);
		// startActivity(intent);
		// break;
		case 4:
			editor = sharedPre.edit();
			editor.putString("user", "");
			editor.putString("password", "");
			editor.commit();
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		pager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
