package cn.skyliuyang.synulib;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class QuickRenew {
	private String code = null;
	Document doc = null;
	public QuickRenew(String code) {
		super();
		this.code = code;
	}
	String doQuickRenew(){
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet get = new HttpGet("http://58.154.49.3:8080/reader/ajax_renew.php?bar_code="+code);
		HttpEntity entity;
		String xml = null;
		try {
			entity = httpClient.execute(get, localContext).getEntity();
			xml = EntityUtils.toString(entity, "UTF-8");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		doc = Jsoup.parse(xml);

		Elements es = doc.getAllElements();
		
		return es.get(1).text();
	}
}
