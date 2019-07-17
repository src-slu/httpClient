package com.cares.sh.httpClient.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cares.sh.httpClient.tools.HttpClientHelper;
import com.cares.sh.httpClient.tools.RequestDataItem;
import com.cares.sh.httpClient.tools.Utils;

public class HttpClientTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientTest.class);

	public static void main(String[] args) {
		CloseableHttpClient client = HttpClientHelper.newHttpClient();

		/**
		 * httpClient Post
		 */
		/**
		 * 写死数据
		 */
		 String URL = "http://localhost:8080/login";
		 Map<String,String> parm = new HashMap<>();
		 RequestDataItem item = new RequestDataItem(URL);
		 parm.put("user", "admin");
		 parm.put("pass", "admin2018");
		 String postForm1 = HttpClientHelper.doPostForm(client, item, parm, "UTF-8");
		 LOGGER.info("amcp登陆接口 result={}", postForm1);

		 /**
		  * 通过配置文件设置参数
		  */
		 RequestDataItem propertieItem = new RequestDataItem();
		 Map<String, String> propertieParm = new HashMap<>();
		 Properties properties = Utils.readConfig();
		 parm.put("mode", properties.getProperty("mode"));
		 parm.put("userId", properties.getProperty("userId"));
		 parm.put("pass", properties.getProperty("pass"));
		
		 /**
		  * 调用自定义模拟请求方式的HttpClientHelper方法
		  */
		 String postForm2 = HttpClientHelper.doPostForm(client, propertieItem, propertieParm, "UTF-8");
		 LOGGER.info("amcp登陆接口 result={}", postForm2);
		 
/*******************************************************************************************************/
		/**
		 * httpClient Get
		 * Get求情抓取网页内容
		 */
		CloseableHttpResponse response = null;
		Properties properties1 = Utils.readConfig();
		// StringBuffer sbf = new StringBuffer();
		// sbf.append("?type=promoIcon").append(
		// "&keys=1418334620,1414736020,1415650420,1379549821,1418813220,1419952220,1354300621,1258829121,1417763220")
		// .append("&url=0%2F%3Fkey%3D%25D2%25C1%25DC%25BD%25C0%25F6%26act%3Dinput%26att%3D1000016%253A19&c=false&l=4f53d7b1821a57f1955f10676edbc153&timestamp=1553840118672");
		// HttpGet httpGet = new HttpGet(properties.getProperty("url") + sbf);
		HttpGet httpGet = new HttpGet(properties1.getProperty("url"));
		try {
			// LOGGER.info("StringBuff 拼接Get URL={}" + properties.getProperty("url") + sbf);
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
			//解析html网页内容,并将内容保存到本地
			Utils.parseHtml(str);
			LOGGER.info("doGet 方法完成 result={}", str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
