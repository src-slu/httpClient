package com.cares.sh.httpClient.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

public class Test {
	
	public static void main(String[] args) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		
		HttpPost httpPost = new HttpPost("http://172.21.126.106:82/amcp/user");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mode", "login"));
		nvps.add(new BasicNameValuePair("userId", "admin"));
		nvps.add(new BasicNameValuePair("pass", "admin2018"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response = httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			String entity = EntityUtils.toString(httpEntity);
			System.out.println(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
