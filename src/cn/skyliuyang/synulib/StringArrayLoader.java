package cn.skyliuyang.synulib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringArrayLoader {
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<Map<String, String>> loadHotBooks(){
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("name", "热门借阅");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("name", "热门评价");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("name", "热门收藏");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("name", "热门浏览");
		list.add(m4);
		return list;
	}
	List<Map<String, String>> loadCategories(){
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("name", "A 马列、毛泽东、邓小平");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("name", "B 哲学、宗教");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("name", "C 社会科学总论");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("name", "D 政治、法律");
		list.add(m4);
		Map<String,String> m5 = new HashMap<String, String>();
		m5.put("name", "E 军事");
		list.add(m5);
		Map<String,String> m6 = new HashMap<String, String>();
		m6.put("name", "F 经济");
		list.add(m6);
		Map<String,String> m7 = new HashMap<String, String>();
		m7.put("name", "G 文化、科学、教育、体育");
		list.add(m7);
		Map<String,String> m8 = new HashMap<String, String>();
		m8.put("name", "H 语言、文字");
		list.add(m8);
		Map<String,String> m9 = new HashMap<String, String>();
		m9.put("name", "I 文学");
		list.add(m9);
		Map<String,String> m10 = new HashMap<String, String>();
		m10.put("name", "J 艺术");
		list.add(m10);
		Map<String,String> m11 = new HashMap<String, String>();
		m11.put("name", "K 历史、地理");
		list.add(m11);
		Map<String,String> m12 = new HashMap<String, String>();
		m12.put("name", "N 自然科学总论");
		list.add(m12);
		Map<String,String> m13 = new HashMap<String, String>();
		m13.put("name", "O 数理科学与化学");
		list.add(m13);
		Map<String,String> m14 = new HashMap<String, String>();
		m14.put("name", "P 天文学、地球科学");
		list.add(m14);
		Map<String,String> m15 = new HashMap<String, String>();
		m15.put("name", "Q 生物科学");
		list.add(m15);
		Map<String,String> m16 = new HashMap<String, String>();
		m16.put("name", "R 医药、卫生");
		list.add(m16);
		Map<String,String> m17 = new HashMap<String, String>();
		m17.put("name", "S 农业科学");
		list.add(m17);
		Map<String,String> m18 = new HashMap<String, String>();
		m18.put("name", "T 工业技术");
		list.add(m18);
		Map<String,String> m19 = new HashMap<String, String>();
		m19.put("name", "U 交通运输");
		list.add(m19);
		Map<String,String> m20 = new HashMap<String, String>();
		m20.put("name", "V 航空、航天");
		list.add(m20);
		Map<String,String> m21 = new HashMap<String, String>();
		m21.put("name", "X 环境科学、安全科学");
		list.add(m21);
		Map<String,String> m22 = new HashMap<String, String>();
		m22.put("name", "Z 综合性图书");
		list.add(m22);

		return list;
	}
	List<Map<String, String>> loadRecommendations(){
		return list;
	}
}
