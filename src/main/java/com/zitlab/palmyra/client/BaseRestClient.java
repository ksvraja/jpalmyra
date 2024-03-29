/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.zitlab.palmyra.client.pojo.TupleResultSet;

/**
 * @author ksvraja
 *
 */
public abstract class BaseRestClient {
	private static CloseableHttpClient httpclient = HttpClientProvider.getClient();
	private static final Logger logger = LoggerFactory.getLogger(BaseRestClient.class);
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
			EntityUtils.consume(entity);
			throw new ClientException("Gateway timedout while accessing the url ");
		}
		case HttpStatus.SC_NOT_FOUND: {
			EntityUtils.consume(entity);
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
			EntityUtils.consume(entity);
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

	protected final <T> T deserialize(HttpEntity entity, TypeReference<T> valueTypeRef) throws IOException {
		return objectMapper.readValue(entity.getContent(), valueTypeRef);
	}
	
	protected final <T> ArrayList<T> deserializeList(HttpEntity entity, Class<T> valueType) throws IOException {
		return objectMapper.readValue(entity.getContent(), objectMapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, valueType));
	}
	
	protected final <T> TupleResultSet<T> deserializeResult(HttpEntity entity, Class<T> valueType) throws IOException {		
		return objectMapper.readValue(entity.getContent(), objectMapper.constructType(getType(TupleResultSet.class, valueType)));
	}

	protected Type getType(Class<?> rawClass, Class<?> parameter) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameter};
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
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
}
