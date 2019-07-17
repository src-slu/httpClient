package com.cares.sh.httpClient.tools;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用org.apache.http.impl.client.CloseableHttpClient发送请求、接收响应结果步骤如下：
 * ①创建HttpClient对象
 * 
 * ②创建请求方法实例并指定请求URL。GET请求则使用HttpGet对象，POST请求则使用HttpPost对象。
 * 例如： HttpGet httpGet = new HttpGet(url);
 * 
 * ③设置请求相关头信息以及参数信息
 * 
 * ④调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
 * 
 * ⑤根据HttpResponse获取请求结果的相关信息
 * 例如：调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。
 * 
 * ⑥释放连接。无论执行方法是否成功，都必须释放连接
 */
public class HttpClientHelper {
	private HttpClientHelper() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
	private static final int CONNECTION_TIMEOUT = 35000;
	private static final int CONNECTION_REQUEST_TIMEOUT = 35000;
	private static final int SOCKET_TIMEOUT = 60000;
	private static final int MAX_TOTAL = 200;
	private static final int DEFAULT_MAX_PER_ROUTE = 20;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0";

	/**
	 * @Title: newHttpClient
	 * @Description: 创建httpClient
	 * @return
	 */
	public static CloseableHttpClient newHttpClient() {
		PoolingHttpClientConnectionManager manage = null;
		SSLContextBuilder builder = new SSLContextBuilder();

		try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(builder.build());
			/**
			 * 同时兼容http和https
			 */
			Registry<ConnectionSocketFactory> socketFactory = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", factory)
					.build();
			// HTTPClient创建
			manage = new PoolingHttpClientConnectionManager(socketFactory);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			manage.setMaxTotal(MAX_TOTAL);
			// 设置最大路由
			manage.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
			return HttpClients.custom().setConnectionManager(manage).build();

		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

	/**
	 * @Title: doPostForm
	 * @Description:
	 * @param httpClient
	 * @param requestDataItem
	 * @param paramMap
	 * @param encoding
	 * @return
	 */
	public static String doPostForm(CloseableHttpClient httpClient, RequestDataItem requestDataItem,
			Map<String, String> paramMap, String encoding) {
		CloseableHttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(requestDataItem.getUrl());
		String responseResult = "";
		try {
			// 创建httpPost远程连接实例
			// 配置请求参数实例
			RequestConfig requestConfig = RequestConfig.custom().
					setConnectTimeout(CONNECTION_TIMEOUT)// 设置连接主机服务超时时间
					.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)// 设置连接请求超时时间
					.setSocketTimeout(SOCKET_TIMEOUT)// 设置读取数据连接超时时间
					.build();
			// 为httpPost实例设置配置
			httpPost.setConfig(requestConfig);
			// 设置请求头
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			if (requestDataItem.isXmlHttpRequest()) {
				httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
			}
			if (StringUtils.isNotBlank(requestDataItem.getReferer())) {
				httpPost.addHeader("Referer", requestDataItem.getReferer());
			}
			for (Entry<String, String> header : requestDataItem.getHttpHearderItem().entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
			httpPost.addHeader("User-Agent", USER_AGENT);
			// 封装post请求参数
			if (null != paramMap && paramMap.size() > 0) {
				List<NameValuePair> nvps = new ArrayList<>();
				// 通过map集成entrySet方法获取entity
				Set<Entry<String, String>> entrySet = paramMap.entrySet();
				// 循环遍历，获取迭代器
				Iterator<Entry<String, String>> iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> mapEntry = iterator.next();
					nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue()));
				}
				// 为httpPost设置封装好的请求参数
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			}
			// httpClient对象执行post请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpPost);
			// 从响应对象中获取响应内容
			HttpEntity entity = httpResponse.getEntity();
			responseResult = EntityUtils.toString(entity);
			LOGGER.info("doPostForm执行完成. url={}, status={}", requestDataItem.getUrl(),
					httpResponse.getStatusLine().getStatusCode());
			System.out.println("doPostForm执行完成. url={}, status={}" + requestDataItem.getUrl()
					+","+ httpResponse.getStatusLine().getStatusCode());
		} catch (IOException ex) {
			LOGGER.error("doPost执行时发生异常. ex={}", ex);
		} finally {
			httpPost.releaseConnection();
			// 关闭资源
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					LOGGER.error("doPost在关闭CloseableHttpResponse时发生异常. ex={}", e);
				}
			}
		}
		return responseResult;
	}

}
