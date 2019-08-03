/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.rest;

import java.io.IOException;
import java.net.ConnectException;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private static final Logger logger = LoggerFactory.getLogger(BaseRestClient.class);
	private ObjectMapper objectMapper;
	
	protected abstract void setAuthentication(HttpMessage request);
	
	public BaseRestClient() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}	
	
	protected HttpEntity post(String URL, Object data) throws IOException {
		return post(URL, objectMapper.writeValueAsString(data));
	}
	
	protected HttpEntity post(String URL, String data) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(URL);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("content-type", "application/json");

		setAuthentication(httpPost);
		try {
			StringEntity body = new StringEntity(data);
			httpPost.setEntity(body);
			response = httpclient.execute(httpPost);
			entity = processHttpCode(response, URL);
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(600, "IOException occured");
		}
		return entity;
	}
	
	protected HttpEntity get(String URL) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(URL);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("content-type", "application/json");
		logger.info(URL);
		setAuthentication(httpGet);
		try {			
			response = httpclient.execute(httpGet);
			entity = processHttpCode(response, URL);
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(600, "IOException occured");
		}
		return entity;
	}

	protected HttpEntity delete(String URL) throws IOException {
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = new HttpDelete(URL);
		httpDelete.addHeader("accept", "application/json");
		httpDelete.addHeader("content-type", "application/json");
		logger.info(URL);
		setAuthentication(httpDelete);
		try {
			response = httpclient.execute(httpDelete);
			entity = processHttpCode(response, URL);
		} catch (ConnectException ce) {
			logger.info("Server Connection refused !!");
			logger.info(ce.getMessage());
			throw new ClientException(HttpStatus.SC_SERVICE_UNAVAILABLE,
					"Server Connection refused !! Please check server reachability");
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(HttpStatus.SC_BAD_REQUEST, "Invalid protocol");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ClientException(600, "IOException occured");
		}
		return entity;
	}

	private HttpEntity processHttpCode(HttpResponse response, String url) throws IOException {
		StatusLine status = response.getStatusLine();
		int code = status.getStatusCode();
		logger.trace("Server Status Code : " + code);
		HttpEntity entity = response.getEntity();
		if (code == HttpStatus.SC_OK)
			return entity;

		switch (code) {
		case HttpStatus.SC_UNAUTHORIZED: {
			logger.info("Un Authorized error message from server");
			String message = EntityUtils.toString(entity);
			if (null == message || message.length() == 0) {
				message = status.getReasonPhrase();
			}
			throw new UnAuthorizedException(message);
		}
		case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
			logger.info("Internal Server error message from server");
			String message = EntityUtils.toString(entity);
			if (null == message || message.length() == 0) {
				message = status.getReasonPhrase();
			}
			throw new ServerErrorException(message);
		}
		case HttpStatus.SC_NOT_FOUND: {
			logger.info("Requested URL not found message from server url -- {}");
			return null;
		}
		case HttpStatus.SC_BAD_REQUEST:{
			Tuple tuple = deserialize(entity, Tuple.class);
			String responseMessage = tuple.getAttributeAsString("message");
			logger.info("Bad Request sent to the server url -- {}, response -- {}", url, responseMessage);			
			throw new BadRequestException("Bad Request sent to the server -- error message " + responseMessage );
		}
		default:
			return response.getEntity();
		}
	}

	@SuppressWarnings({ "rawtypes" })
	protected final <T> T deserialize(HttpEntity entity, TypeReference valueTypeRef) throws IOException {
		return objectMapper.readValue(entity.getContent(), valueTypeRef);
	}

	protected final <T> T deserialize(HttpEntity entity, Class<T> valueType) throws IOException {
		return objectMapper.readValue(entity.getContent(), valueType);
	}

//	protected final String serialize(Object obj) throws IOException {
//		return objectMapper.writeValueAsString(obj);
//	}	
	
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
