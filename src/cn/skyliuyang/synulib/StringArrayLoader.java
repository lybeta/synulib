package cn.skyliuyang.synulib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringArrayLoader {
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<Map<String, String>> loadHotBooks(){
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("name", "���Ž���");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("name", "��������");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("name", "�����ղ�");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("name", "�������");
		list.add(m4);
		return list;
	}
	List<Map<String, String>> loadCategories(){
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("name", "A ���С�ë�󶫡���Сƽ");
		list.add(m1);
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("name", "B ��ѧ���ڽ�");
		list.add(m2);
		Map<String,String> m3 = new HashMap<String, String>();
		m3.put("name", "C ����ѧ����");
		list.add(m3);
		Map<String,String> m4 = new HashMap<String, String>();
		m4.put("name", "D ���Ρ�����");
		list.add(m4);
		Map<String,String> m5 = new HashMap<String, String>();
		m5.put("name", "E ����");
		list.add(m5);
		Map<String,String> m6 = new HashMap<String, String>();
		m6.put("name", "F ����");
		list.add(m6);
		Map<String,String> m7 = new HashMap<String, String>();
		m7.put("name", "G �Ļ�����ѧ������������");
		list.add(m7);
		Map<String,String> m8 = new HashMap<String, String>();
		m8.put("name", "H ���ԡ�����");
		list.add(m8);
		Map<String,String> m9 = new HashMap<String, String>();
		m9.put("name", "I ��ѧ");
		list.add(m9);
		Map<String,String> m10 = new HashMap<String, String>();
		m10.put("name", "J ����");
		list.add(m10);
		Map<String,String> m11 = new HashMap<String, String>();
		m11.put("name", "K ��ʷ������");
		list.add(m11);
		Map<String,String> m12 = new HashMap<String, String>();
		m12.put("name", "N ��Ȼ��ѧ����");
		list.add(m12);
		Map<String,String> m13 = new HashMap<String, String>();
		m13.put("name", "O �����ѧ�뻯ѧ");
		list.add(m13);
		Map<String,String> m14 = new HashMap<String, String>();
		m14.put("name", "P ����ѧ�������ѧ");
		list.add(m14);
		Map<String,String> m15 = new HashMap<String, String>();
		m15.put("name", "Q �����ѧ");
		list.add(m15);
		Map<String,String> m16 = new HashMap<String, String>();
		m16.put("name", "R ҽҩ������");
		list.add(m16);
		Map<String,String> m17 = new HashMap<String, String>();
		m17.put("name", "S ũҵ��ѧ");
		list.add(m17);
		Map<String,String> m18 = new HashMap<String, String>();
		m18.put("name", "T ��ҵ����");
		list.add(m18);
		Map<String,String> m19 = new HashMap<String, String>();
		m19.put("name", "U ��ͨ����");
		list.add(m19);
		Map<String,String> m20 = new HashMap<String, String>();
		m20.put("name", "V ���ա�����");
		list.add(m20);
		Map<String,String> m21 = new HashMap<String, String>();
		m21.put("name", "X ������ѧ����ȫ��ѧ");
		list.add(m21);
		Map<String,String> m22 = new HashMap<String, String>();
		m22.put("name", "Z �ۺ���ͼ��");
		list.add(m22);

		return list;
	}
	List<Map<String, String>> loadRecommendations(){
		return list;
	}
}
