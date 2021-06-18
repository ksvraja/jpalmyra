package com.zitlab.palmyra.client;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientProvider {
	private static CloseableHttpClient httpClient = null;
	private static final Logger logger = LoggerFactory.getLogger(HttpClientProvider.class);
	public static CloseableHttpClient getClient() {
		if (null != httpClient)
			return httpClient;

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(256);
		cm.setDefaultMaxPerRoute(128);

		ProxyConfig proxy = getProxy();

		if (null != proxy) {
			logger.info("Initializing with Proxy {}://{}:{}", proxy.scheme, proxy.host, proxy.port);
			httpClient = initWithProxy(cm, proxy.host, proxy.port, proxy.scheme);			
		}
		else {
			logger.info("Initializing direct connection");
			httpClient = HttpClients.custom().setConnectionManager(cm).build();
		}

		return httpClient;
	}

	private static CloseableHttpClient initWithProxy(PoolingHttpClientConnectionManager cm, String proxyServer,
			int port, String scheme) {
		HttpHost proxy = new HttpHost(proxyServer, port, scheme);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);		
		return HttpClients.custom().setConnectionManager(cm).setRoutePlanner(routePlanner).build();
	}

	private static ProxyConfig getProxy() {
		int proxyPort = 80;
		String proxyScheme = "http";
		String proxyServer = System.getProperty("http.proxyHost");
		if (null == proxyServer) {
			proxyServer = System.getProperty("https.proxyHost");
			if (null == proxyServer)
				return null;
			else {
				proxyPort = getInt(System.getProperty("https.proxyPort"), 443);
				proxyScheme = "https";
			}
		} else {
			proxyPort = getInt(System.getProperty("http.proxyPort"), 80);
		}
		return new ProxyConfig(proxyServer, proxyPort, proxyScheme);
	}

	private static int getInt(String value, int defValue) {
		try {
			return Integer.parseInt(value);
		} catch (Throwable e) {
			return defValue;
		}
	}
}
