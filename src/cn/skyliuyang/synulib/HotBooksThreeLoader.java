package cn.skyliuyang.synulib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HotBooksThreeLoader {
	Document doc = null;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	List<Map<String, String>> load() throws ParseException, IOException {

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet("http://58.154.49.3:8080/opac/vs_rank.php");
		HttpEntity entity;
		String html = null;
		try {
			entity = httpClient.execute(get, localContext).getEntity();
			html = EntityUtils.toString(entity, "UTF-8");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		doc = Jsoup.parse(html);

		Elements es = doc.getElementsByTag("tr");
		for (int i=1;i<es.size();i++) {
			Element e = es.get(i);
			Map<String,String> m = new HashMap<String, String>();
			m.put("name", e.child(1).text());
			m.put("writer", e.child(2).text());
			m.put("number", "索书号："+e.child(4).text());
			m.put("times", "收藏人次："+e.child(5).text());
			m.put("url", e.child(1).select("a").attr("href").toString());
			list.add(m);
		}

		return list;
	}
}
