package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Order {
	private String user = null;
	private String password = null;
	private String marcno = null;
	private String number = null;
	private String location = null;

	public Order(String user, String password, String marcno, String number,
			String location) {
		super();
		this.user = user;
		this.password = password;
		this.marcno = marcno;
		this.number = number;
		this.location = location;
	}

	Document doc = null;

	String doOrder() throws ParseException, IOException,
			ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("number", user));
		formparams.add(new BasicNameValuePair("passwd", password));
		formparams.add(new BasicNameValuePair("returnUrl", ""));
		formparams.add(new BasicNameValuePair("select", "cert_no"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");
		HttpPost post = new HttpPost(
				"http://58.154.49.3:8080/reader/redr_verify.php");
		post.setEntity(entity);
		HttpContext localContext = new BasicHttpContext();
		httpClient.execute(post, localContext);
		post.abort();
		String html = null;
		try {
			HttpGet httpget = new HttpGet(
					"http://58.154.49.3:8080/opac/userpreg_result.php?marc_no="
							+ marcno + "&count=1&preg_days1=30&take_loca1="
							+ location + "&callno1=" + number + "&location1="
							+ location + "&pregKeepDays1=2&check=1");
			try {
				HttpEntity entity1 = httpClient.execute(httpget).getEntity();
				html = EntityUtils.toString(entity1, "UTF-8");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		doc = Jsoup.parse(html);
		Element e = doc.getElementById("wrap");
		return e.child(1).child(2).ownText();
	}
}
