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

public class NewBooksLoader {
	Document doc = null;
	String url = null;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	List<Map<String, String>> load(int urlNum, int page) throws ParseException,
			IOException {

		switch (urlNum) {
		case 0:
			url = Urls.newBooksA;
			break;
		case 1:
			url = Urls.newBooksB;
			break;
		case 2:
			url = Urls.newBooksC;
			break;
		case 3:
			url = Urls.newBooksD;
			break;
		case 4:
			url = Urls.newBooksE;
			break;
		case 5:
			url = Urls.newBooksF;
			break;
		case 6:
			url = Urls.newBooksG;
			break;
		case 7:
			url = Urls.newBooksH;
			break;
		case 8:
			url = Urls.newBooksI;
			break;
		case 9:
			url = Urls.newBooksJ;
			break;
		case 10:
			url = Urls.newBooksK;
			break;
		case 11:
			url = Urls.newBooksN;
			break;
		case 12:
			url = Urls.newBooksO;
			break;
		case 13:
			url = Urls.newBooksP;
			break;
		case 14:
			url = Urls.newBooksQ;
			break;
		case 15:
			url = Urls.newBooksR;
			break;
		case 16:
			url = Urls.newBooksS;
			break;
		case 17:
			url = Urls.newBooksT;
			break;
		case 18:
			url = Urls.newBooksU;
			break;
		case 19:
			url = Urls.newBooksV;
			break;
		case 20:
			url = Urls.newBooksX;
			break;
		case 21:
			url = Urls.newBooksZ;
			break;
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet(url + "&page=" + page);
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

		Elements es = doc.getElementsByClass("list_books");
		for (Element e : es) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("name", e.child(0).text());
			m.put("writer", e.child(1).text());
			m.put("url", "http://58.154.49.3:8080/opac/"+e.child(0).child(0).select("a").attr("href").toString());
			list.add(m);
		}

		return list;
	}
}
