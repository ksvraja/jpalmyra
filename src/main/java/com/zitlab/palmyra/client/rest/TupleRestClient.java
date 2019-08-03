/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zitlab.palmyra.client.pojo.Tuple;
import com.zitlab.palmyra.client.pojo.TupleFilter;
import com.zitlab.palmyra.client.pojo.TupleResultSet;

/**
 * @author ksvraja
 *
 */
public class TupleRestClient extends BaseRestClient {
//	private static final Logger logger = LoggerFactory.getLogger(TupleRestClient.class);
	private String baseUrl;
	private String username;
	private String password;
	private String appn;	

	public TupleRestClient(String baseUrl, String username, String password, String appn) {
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
		this.appn = appn;		
	}

	public Tuple findById(String id, String type) throws IOException {
		HttpEntity entity = get(getUrl(type, id));
		if (null != entity) {
			return deserialize(entity, Tuple.class);
		}
		return null;
	}

	public Tuple findUnique(String key, String value, String type) throws IOException {
		TupleFilter<Tuple> filter = new TupleFilter<Tuple>();
		Tuple tuple = new Tuple(type);
		tuple.setAttribute(key, value);
		filter.setCriteria(tuple);
		HttpEntity entity = post(uniqueUrl(type), filter);
		if (null != entity) {
			return deserialize(entity, Tuple.class);
		}
		return null;
	}
	
	public Tuple findUnique(TupleFilter<Tuple> tuple, String type) throws IOException {		
		HttpEntity entity = post(uniqueUrl(type), tuple);
		if (null != entity) {
			return deserialize(entity, Tuple.class);
		}
		return null;
	}

	public TupleResultSet<Tuple> query(TupleFilter<Tuple> filter) throws IOException {

		if (null == filter.getCriteria() || null == filter.getCriteria().getType()) {
			throw new IOException("Criteria / Tupletype  cannot be null");
		}

		HttpEntity entity = post(queryUrl(filter.getCriteria().getType()), filter);
		if (null == entity) {
			throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
		}
		TupleResultSet<Tuple> result = deserialize(entity, new TypeReference<TupleResultSet<Tuple>>() {
		});

		return result;
	}

	public Tuple save(Tuple tuple) throws IOException {
		String type = tuple.getType();		
		HttpEntity entity = post(getUrl(type), tuple);
		if (null == entity) {
			throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
		}
		return deserialize(entity, Tuple.class);
	}

	@Override
	protected final void setAuthentication(HttpMessage request) {
		String md5password = DigestUtils.md5Hex(password);
		String random = getUniqueRef();
		StringBuilder auth = new StringBuilder(username).append("@").append(appn).append(":").append(md5password)
				.append(random);
		String md5Auth = DigestUtils.md5Hex(auth.toString());
		String authHeader = "Basic " + md5Auth;
		request.addHeader("Authorization", authHeader);
		request.addHeader("userId", username);
		request.addHeader("random", random);
	}

	public final HashMap<String, String> getAuthHeaders() {
		String md5password = DigestUtils.md5Hex(password);
		String random = getUniqueRef();
		StringBuilder auth = new StringBuilder(username).append("@").append(appn).append(":").append(md5password)
				.append(random);
		String md5Auth = DigestUtils.md5Hex(auth.toString());
		String authHeader = "Basic " + md5Auth;
		HashMap<String, String> authMap = new HashMap<String, String>();

		authMap.put("Authorization", authHeader);
		authMap.put("userId", username);
		authMap.put("random", random);
		return authMap;
	}

	private String getUniqueRef() {
		return Long.toString(new Date().getTime());
	}

	protected final String getUrl(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/data/").append(type);
		return sb.toString();
	}

	protected final String getUrl(String type, String id) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/data/").append(type).append("/").append(id);
		return sb.toString();
	}

	protected final String queryUrl(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/query/").append(type);
		return sb.toString();
	}

	protected final String actionUrl(String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/action/").append(action);
		return sb.toString();
	}

	protected final String listUrl(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/query/").append(type).append("/list");
		return sb.toString();
	}

	protected final String uniqueUrl(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/query/").append(type).append("/unique");
		return sb.toString();
	}

	public final Tuple executeAction(String action) throws IOException {
		HttpEntity entity = post(actionUrl(action), "{}");
		if (null == entity) {
			throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
		}
		return deserialize(entity, Tuple.class);
	}
	
	public final Tuple executeAction(String action, Tuple tuple) throws IOException {		
		HttpEntity entity = post(actionUrl(action), tuple);
		if (null == entity) {
			throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
		}
		return deserialize(entity, Tuple.class);
	}
	
	public final ArrayList<Tuple> executeActionList(String action, Tuple tuple) throws IOException {		
		HttpEntity entity = post(actionUrl(action), tuple);
		if (null == entity) {
			throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
		}
		ArrayList<Tuple> result = deserialize(entity, new TypeReference<ArrayList<Tuple>>() {
		});
		return result;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppn() {
		return appn;
	}

	public void setAppn(String appn) {
		this.appn = appn;
	}

}
