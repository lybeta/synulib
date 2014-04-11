package cn.skyliuyang.synulib;

public class EncodingTools {
	public static String convertToChinese(String dataStr) {
		if(dataStr == null || dataStr.length() == 0) {
			return dataStr;
		}
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			int system = 10;// 进制
			if (start == 0) {
				int t = dataStr.indexOf("&#");
				if (start != t)
					start = t;
				if(start > 0) {
					buffer.append(dataStr.substring(0, start));
				}
				if(start == -1) {
					return dataStr;
				}
			}
			end = dataStr.indexOf(";", start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = dataStr.substring(start + 2, end);
				// 判断进制
				char s = charStr.charAt(0);
				if (s == 'x' || s == 'X') {
					system = 16;
					charStr = charStr.substring(1);
				}
				// 转换
				try {
					char letter = (char) Integer.parseInt(charStr, system);
					buffer.append(new Character(letter).toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			// 处理当前unicode字符到下一个unicode字符之间的非unicode字符
			start = dataStr.indexOf("&#", end);
			if (start - end > 1) {
				buffer.append(dataStr.substring(end + 1, start));
			}
			// 处理最后面的非 unicode字符
			if (start == -1) {
				int length = dataStr.length();
				if (end + 1 != length) {
					buffer.append(dataStr.substring(end + 1, length));
				}
			}
		}
		return buffer.toString();
	}
}
