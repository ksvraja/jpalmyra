package com.zitlab.palmyra.client;

class ProxyConfig {
	ProxyConfig(String host, int port, String scheme) {
		this.host = host;
		this.port = port;
		this.scheme = scheme;
	}

	String host;
	int port;
	String scheme;
}
