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

public class AnnouncementLoader {
	Document doc = null;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	List<Map<String, String>> load() throws ParseException, IOException {

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet("http://210.30.208.249/2level/Showlist.asp");
		HttpEntity entity;
		String html = null;
		try {
			entity = httpClient.execute(get, localContext).getEntity();
			html = EntityUtils.toString(entity, "GBK");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		doc = Jsoup.parse(html);

		Elements es = doc.getElementsByClass("tabletd3");
		for (int i=0;i<5;i++) {
			if(es.size() == i) break;
			Element e = es.get(i);
			Map<String,String> m = new HashMap<String, String>();
			m.put("name", e.select("a").text().toString());
			m.put("url", "http://210.30.208.249/2level/"+e.select("a").attr("href").toString());
			list.add(m);
		}

		return list;
	}
}
