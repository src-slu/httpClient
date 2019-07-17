package com.cares.sh.httpClient.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Utils {

	static Properties prop = new Properties();
	static InputStream input = Object.class.getResourceAsStream("/config.properties");

	// 读取properties文件
	@SuppressWarnings("unused")
	public static Properties readConfig() {
		InputStreamReader reader = null;
		try {
			// 设置读取编码，防止乱码
			reader = new InputStreamReader(input, "UTF-8");
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static void save(String str) {
		File file = new File("E:\\test.txt");
		BufferedWriter bfw = null;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			// 逐行写入
			bfw.write(str + "\r\n");
			bfw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @Title: parseHtml
	 * @Description: Jsoup解析HTML数据
	 * @param str
	 *            html
	 * @return
	 */
	public static void parseHtml(String str) {
		Document document = Jsoup.parse(str);
		// Element elementById = document.getElementById("a");
		Elements elements = document.select("a");
		String element = "";
		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i).text();
			save(element);
		}
	}

}
