package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class ShelfCreateActivity extends SherlockActivity {

	private EditText et_shelf_create_name;
	private EditText et_shelf_create_remark;
	private Button btn_shelf_create;
	private String user = null;
	private String password = null;
	private String name = null;
	private String remark = null;
	private SharedPreferences sharedPre = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_shelf_create);

		et_shelf_create_name = (EditText) findViewById(R.id.et_shelf_create_name);
		et_shelf_create_remark = (EditText) findViewById(R.id.et_shelf_create_remark);
		btn_shelf_create = (Button) findViewById(R.id.btn_shelf_create);
		sharedPre = getSharedPreferences("lib", Activity.MODE_PRIVATE);
		user = sharedPre.getString("user", "");
		password = sharedPre.getString("password", "");

		btn_shelf_create.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				name = et_shelf_create_name.getText().toString();
				remark = et_shelf_create_remark.getText().toString();
				if ("".equals(name)) {
					Toast.makeText(getApplicationContext(),
							"o(¦á¦ä¦á)o Ç×£¬²»ÄÜÁô¿Õ°¡...", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread() {
					public void run() {
						try {
							shelfCreate(user, password, name, remark);
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
					}
				}.start();
				finish();
			}
		});
	}

	private void shelfCreate(String user, String password, String name,
			String remark) throws ParseException, IOException,
			ClientProtocolException, IOException {

		HttpClient httpClient = new DefaultHttpClient();

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("number", user));
		formparams.add(new BasicNameValuePair("passwd", password));
		formparams.add(new BasicNameValuePair("returnUrl", ""));
		formparams.add(new BasicNameValuePair("select", "cert_no"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");
		try {
			HttpPost post = new HttpPost(
					"http://58.154.49.3:8080/reader/redr_verify.php");
			post.setEntity(entity);
			HttpContext localContext = new BasicHttpContext();
			httpClient.execute(post, localContext);
			post.abort();
			HttpGet httpget = new HttpGet(
					"http://58.154.49.3:8080/reader/book_shelf_man.php?cls_name="
							+ name + "&remark=" + remark + "&action=0&classid=");
			httpClient.execute(httpget).getEntity();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

}
