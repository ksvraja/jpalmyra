package com.zitlab.palmyra.client;

import java.net.ProxySelector;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

public class HttpClientProvider {
	private static CloseableHttpClient httpClient = null;

	public static CloseableHttpClient getClient() {
		if (null != httpClient)
			return httpClient;

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(getProperty("jpalmyra.maxroute", 256));
		cm.setDefaultMaxPerRoute(getProperty("jpalmyra.maxroute.perhost", 256));
		
		String proxyEnabled = getProperty("proxy.enabled", "true");
		
		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm);
		if ("true".equals(proxyEnabled.toLowerCase())) {
			SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
			builder.setRoutePlanner(routePlanner);
		}
		return builder.build();
	}
	
	private static int getProperty(String property, int defValue) {
		String value = System.getProperty(property);
		if(null == value)
			return defValue;
		try {
			return Integer.parseInt(value);
		}catch(Exception e) {
			return defValue;
		}
	}
	
	private static String getProperty(String property, String defValue) {
		String value = System.getProperty(property);
		return null != value ? value : defValue;
	}
}
