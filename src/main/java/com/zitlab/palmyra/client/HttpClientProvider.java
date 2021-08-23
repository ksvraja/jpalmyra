package com.zitlab.palmyra.client;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientProvider {
	private static CloseableHttpClient httpClient = null;
	private static final Logger logger = LoggerFactory.getLogger(HttpClientProvider.class);

	public static CloseableHttpClient getClient() {
		if (null != httpClient)
			return httpClient;

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(getProperty("jpalmyra.maxroute", 256));
		cm.setDefaultMaxPerRoute(getProperty("jpalmyra.maxroute.perhost", 256));

		String proxyEnabled = getProperty("proxy.enabled", "true");

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm);
		if ("true".equals(proxyEnabled.toLowerCase())) {
			ProxySelector selector = ProxySelector.getDefault();
			try {
				List<Proxy> proxies = selector.select(new URI("https://www.google.com"));
				if (proxies.size() > 0) {
					if (1 == proxies.size() && "DIRECT".equalsIgnoreCase(proxies.get(0).toString())) {
						logger.info("Direct connection to Internet, No proxies configured");
					} else {
						logger.info("Initializing with {} number of proxies", proxies.size());

						for (Proxy proxy : proxies) {
							logger.info(" proxy: {}", proxy.toString());
						}
					}
				} else {
					logger.info("No system proxies provided");
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(selector);
			builder.setRoutePlanner(routePlanner);
		} else {
			logger.info("initializing without proxy");
		}
		return builder.build();
	}

	private static int getProperty(String property, int defValue) {
		String value = System.getProperty(property);
		if (null == value)
			return defValue;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defValue;
		}
	}

	private static String getProperty(String property, String defValue) {
		String value = System.getProperty(property);
		return null != value ? value : defValue;
	}
}
