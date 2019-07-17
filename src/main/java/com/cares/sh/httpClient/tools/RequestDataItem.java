package com.cares.sh.httpClient.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;

public class RequestDataItem {
	private String url;
	private String referer;
	private String host;
	private String queryString; // GET请求的QueryString【setQueryString(String queryString)】
	private NameValuePair[] queryString2; // GET请求的QueryString【setQueryString(NameValuePair[] params)】
	private boolean isXmlHttpRequest = false;
	private String origin;
	private String xlang;
	private Map<String, String> cookies = new HashMap<>();
	private Map<String, String> httpHearderItem = new HashMap<>();
	 
	public RequestDataItem() {
		this.getUrl();
	}

	public RequestDataItem(String url) {
		this.url = url;
	}

	public String getUrl() {
		this.setUrl(url);
		return url;
	}

	public void setUrl(String url) {
		url = Utils.readConfig().getProperty("url");
		this.url = url;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public NameValuePair[] getQueryString2() {
		return queryString2;
	}

	public void setQueryString2(NameValuePair[] queryString2) {
		this.queryString2 = queryString2;
	}

	public boolean isXmlHttpRequest() {
		return isXmlHttpRequest;
	}

	public void setXmlHttpRequest(boolean isXmlHttpRequest) {
		this.isXmlHttpRequest = isXmlHttpRequest;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getXlang() {
		return xlang;
	}

	public void setXlang(String xlang) {
		this.xlang = xlang;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getHttpHearderItem() {
		return httpHearderItem;
	}

	public void setHttpHearderItem(Map<String, String> httpHearderItem) {
		this.httpHearderItem = httpHearderItem;
	}
	
}
