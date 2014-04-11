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

public class BookDetailLoader {
	Document doc = null;
	String url = null;

	Book load(String url) throws ParseException,
			IOException {

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet(url);
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
		Book book = new Book();
		Elements es = doc.getElementsByClass("booklist");
		for (Element e : es) {
			if("题名/责任者:".equals(e.child(0).text())){
				book.setName(e.child(1).select("a").text());
			}else if("个人责任者:".equals(e.child(0).text())){
				book.setWriter(e.child(1).text());
			}else if("出版发行项:".equals(e.child(0).text())){
				book.setPublisher(e.child(1).text());
			}else if("ISBN及定价::".equals(e.child(0).text())){
				book.setIsbn(e.child(1).text());
			}
		}
		Elements es1 = doc.getElementsByTag("tr");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i=1;i<es1.size();i++) {
			Element e = es1.get(i);
			Map<String,String> m = new HashMap<String, String>();
			m.put("number1", "索书号："+e.child(0).text());
			m.put("number2", "索书号："+e.child(1).text());
			m.put("place1", e.child(3).text());
			m.put("place2", e.child(4).text());
			m.put("state", e.child(5).text());
			list.add(m);
		}
		book.setBookList(list);

		return book;
	}
}
