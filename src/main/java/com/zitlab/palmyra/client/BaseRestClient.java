/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.client.exception.ApplicationException;
import com.zitlab.palmyra.client.exception.BadRequestException;
import com.zitlab.palmyra.client.exception.ClientException;
import com.zitlab.palmyra.client.exception.ServerErrorException;
import com.zitlab.palmyra.client.exception.UnAuthorizedException;
import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public abstract class BaseRestClient {
	private static CloseableHttpClient httpclient = getHttpClient();
	private static final Logger logger = LoggerFactory.getLogger(BaseRestClient.class);
	// private static IdleConnectionMonitor monitor = null;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	protected abstract void setAuthentication(HttpMessage request);

	public BaseRestClient() {

	}

	protected HttpEntity post(String URL, Object data) throws IOException {
		return post(URL, objectMapper.writeValueAsString(data));
	}

	private static CloseableHttpClient getHttpClient() {

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(256);
		cm.setDefaultMaxPerRoute(128);
		// monitor = new IdleConnectionMonitor(cm);
//        Thread monitorThread = new Thread(monitor);
//        monitorThread.setDaemon(true);
//        monitorThread.start();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		return httpClient;
	}

	protected HttpEntity post(String URL, String data) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(URL);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("content-type", "application/json");

		setAuthentication(httpPost);
		try {
			StringEntity body = new StringEntity(data, ContentType.create("application/json", "UTF-8"));
			httpPost.setEntity(body);
			response = httpclient.execute(httpPost);
			entity = processHttpCode(response, URL);
		} catch (UnAuthorizedException ue) {
			throw ue;
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability", ce);
		} catch (ClientProtocolException e1) {
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol", e1);
		}
		return entity;
	}

	protected HttpEntity get(String URL) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(URL);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("content-type", "application/json");
		setAuthentication(httpGet);
		try {
			response = httpclient.execute(httpGet);
			entity = processHttpCode(response, URL);
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability", ce);
		} catch (ClientProtocolException e1) {
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol", e1);
		}
		return entity;
	}

	protected HttpEntity delete(String URL) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = new HttpDelete(URL);
		httpDelete.addHeader("accept", "application/json");
		httpDelete.addHeader("content-type", "application/json");
		setAuthentication(httpDelete);
		try {
			response = httpclient.execute(httpDelete);
			entity = processHttpCode(response, URL);
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability", ce);
		} catch (ClientProtocolException e1) {
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol", e1);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	private HttpEntity processHttpCode(HttpResponse response, String url) throws IOException {
		StatusLine status = response.getStatusLine();
		int code = status.getStatusCode();
		if (logger.isTraceEnabled())
			logger.trace("Server Status Code : {}", code);
		HttpEntity entity = response.getEntity();
		if (code == HttpStatus.SC_OK)
			return entity;

		switch (code) {
		case HttpStatus.SC_UNAUTHORIZED: {
			logger.info("Un Authorized error message from server");
			Map<String, Object> val = null;
			String message = EntityUtils.toString(entity);
			try {
				val = deserialize(message, HashMap.class);
			} catch (Throwable e) {
			}
			throw new UnAuthorizedException(val, message);
		}
		case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
			logger.info("Internal Server error message from server");
			String message = EntityUtils.toString(entity);
			if (null == message || message.length() == 0) {
				message = status.getReasonPhrase();
			}
			throw new ServerErrorException(message);
		}
		case HttpStatus.SC_GATEWAY_TIMEOUT: {
			logger.info("Gateway timeout -- {}", url);
			throw new ClientException("Gateway timedout while accessing the url ");
		}
		case HttpStatus.SC_NOT_FOUND: {
			logger.info("Requested URL not found message from server url -- {}", url);
			throw new ClientException("Requested URL not found from the server -- " + url);
		}
		case HttpStatus.SC_BAD_REQUEST: {
			Map<String, Object> val = null;
			String message = EntityUtils.toString(entity);
			try {
				val = deserialize(message, HashMap.class);
			} catch (Throwable e) {
			}
			throw new BadRequestException(val, message);
		}
		case HttpStatus.SC_NO_CONTENT: {
			logger.trace("Empty response received for the request");
			return null;
		}
		default: {
			Map<String, Object> val = null;
			String message = EntityUtils.toString(entity);
			try {
				val = deserialize(message, HashMap.class);
			} catch (Throwable e) {
			}
			throw new ApplicationException(code, message, val);
		}
		}
	}

	@SuppressWarnings({ "rawtypes" })
	protected final <T> T deserialize(HttpEntity entity, TypeReference valueTypeRef) throws IOException {
		return objectMapper.readValue(entity.getContent(), valueTypeRef);
	}

	protected final <T> T deserialize(HttpEntity entity, Class<T> valueType) throws IOException {
		return objectMapper.readValue(entity.getContent(), valueType);
	}

	protected final <T> T deserialize(String content, Class<T> valueType) throws IOException {
		return objectMapper.readValue(content, valueType);
	}

	public void close() {
		try {
			httpclient.close();
		} catch (Throwable e) {
		}
	}

	// Based on -
	// https://stackoverflow.com/questions/25666995/apache-httpclient-need-to-use-multithreadedhttpconnectionmanager
	private static class IdleConnectionMonitor implements Runnable {
		// The manager to watch.
		private final PoolingHttpClientConnectionManager cm;
		// Use a BlockingQueue to stop everything.
		private final BlockingQueue<Stop> stopSignal = new ArrayBlockingQueue<Stop>(1);

		IdleConnectionMonitor(PoolingHttpClientConnectionManager cm) {
			this.cm = cm;
		}

		public void run() {
			try {
				// Holds the stop request that stopped the process.
				Stop stopRequest;
				// Every 5 seconds.
				while ((stopRequest = stopSignal.poll(5, TimeUnit.SECONDS)) == null) {
					// Close expired connections
					cm.closeExpiredConnections();
					// Optionally, close connections that have been idle too long.
					cm.closeIdleConnections(30, TimeUnit.SECONDS);
				}
				// Acknowledge the stop request.
				stopRequest.stopped();
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		// Pushed up the queue.
		private static class Stop {
			// The return queue.
			private final BlockingQueue<Stop> stop = new ArrayBlockingQueue<Stop>(1);

			// Called by the process that is being told to stop.
			public void stopped() {
				// Push me back up the queue to indicate we are now stopped.
				stop.add(this);
			}

			// Called by the process requesting the stop.
			public void waitForStopped() throws InterruptedException {
				// Wait until the callee acknowledges that it has stopped.
				stop.take();
			}
		}

		public void shutdown() throws InterruptedException, IOException {
			// Signal the stop to the thread.
			Stop stop = new Stop();
			stopSignal.add(stop);
			// Wait for the stop to complete.
			stop.waitForStopped();
			// Close the connection manager.
			cm.close();
		}

	}
}
