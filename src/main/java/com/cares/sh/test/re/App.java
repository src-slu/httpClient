package com.cares.sh.test.re;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cares.sh.httpClient.tools.Md5Util;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main1(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		HttpPost httpPost = new HttpPost("http://xxxxxx/router/rest");
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("app_key", "re_hdky_sc"));
		nameValuePair.add(new BasicNameValuePair("secret", "Secret@2019#"));

		nameValuePair.add(new BasicNameValuePair("trace", "true"));
		nameValuePair.add(new BasicNameValuePair("whetherStopOver", "0"));
		nameValuePair.add(new BasicNameValuePair("sign_method", "md5"));

		nameValuePair.add(new BasicNameValuePair("method", "xxxxxx.realtime"));

		/**
		 * MD5加密，返回sign号
		 */
		Map<String, String> params = new HashMap<>();
		String sign = Md5Util.signRequest(params, "Secret@2019#", "md5");
		logger.info("sign号  ========> " + sign);
		nameValuePair.add(new BasicNameValuePair("sign", sign));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
			response = httpclient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			String responseResult = EntityUtils.toString(responseEntity);
			// System.out.println(response.toString());
			logger.info("doPostJson执行完成  status={} result={}", response.getStatusLine().getStatusCode(),
					responseResult);
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			logger.info("post在关闭CloseableHttpResponse时发生异常. ex={}", e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			if (response != null) {
				try {
					response.close();
					// httpclient.close();
				} catch (IOException e) {
					logger.error("doPostJson在关闭CloseableHttpResponse时发生异常. ex={}", e);
				}
			}
		}
	}
}
