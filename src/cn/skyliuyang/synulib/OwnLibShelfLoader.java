package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jsoup.select.Elements;

public class OwnLibShelfLoader {
	Document doc = null;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<Map<String, String>> load(String user,String password) throws ParseException, IOException,
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
					"http://58.154.49.3:8080/reader/book_shelf_man.php");
			HttpEntity entity1 = httpClient.execute(httpget).getEntity();
			html = EntityUtils.toString(entity1, "UTF-8");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		doc = Jsoup.parse(html);
		Elements es = doc.getElementsByTag("tr");
		for (int i = 1; i < es.size()-2; i++) {
			Element e = es.get(i);
			Map<String, String> m = new HashMap<String, String>();
			m.put("classid", e.child(0).select("a").attr("href").substring(23));
			m.put("name", e.child(0).select("a").text());
			m.put("count",  "书目数："+e.child(1).text());
			m.put("date", "创建日期："+e.child(2).text());
			m.put("remark", "备注："+e.child(3).text());
			m.put("classid", e.child(0).select("a").attr("href").toString().substring(23));
			m.put("url", "http://58.154.49.3:8080/reader/"+e.child(0).select("a").attr("href").toString());
			m.put("deleteurl", "http://58.154.49.3:8080/reader/"+e.child(4).select("a").attr("href").toString());
			list.add(m);
		}

		return list;

	}
}

