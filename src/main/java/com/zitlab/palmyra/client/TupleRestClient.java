/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zitlab.palmyra.client.auth.AuthClient;
import com.zitlab.palmyra.client.auth.BasicAuthClient;
import com.zitlab.palmyra.client.exception.EmptyResultException;
import com.zitlab.palmyra.client.exception.NoActionResultException;
import com.zitlab.palmyra.client.exception.NoRecordException;
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
	private String deviceId;
	private AuthClient authClient = new BasicAuthClient();

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

	public Tuple findUnique(String key, String value, String type, String fields) throws IOException {
		TupleFilter<Tuple> filter = new TupleFilter<Tuple>();
		Tuple tuple = new Tuple(type);
		tuple.setAttribute(key, value);
		filter.setCriteria(tuple);
		if (null != fields)
			filter.addFields(fields);
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

	public TupleResultSet<Tuple> query(TupleFilter<Tuple> filter, String type) throws IOException {
		HttpEntity entity = post(queryUrl(type), filter);
		if (null == entity) {
			throw new NoRecordException(type);
		}
		return deserialize(entity, new TypeReference<TupleResultSet<Tuple>>() {
		});
	}

	public ArrayList<Tuple> list(TupleFilter<Tuple> filter, String type) throws IOException {
		HttpEntity entity = post(listUrl(type), filter);
		if (null == entity) {
			throw new NoRecordException(type);
		}
		return deserialize(entity, new TypeReference<ArrayList<Tuple>>() {
		});
	}

	public ArrayList<Tuple> listCustom(String url, Object obj) throws IOException {
		HttpEntity entity = post(customUrl(url), obj);
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, new TypeReference<ArrayList<Tuple>>() {
		});
	}
	
	public Tuple postCustom(String url, Object obj) throws IOException {
		HttpEntity entity = post(customUrl(url), obj);
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, Tuple.class);
	}
	
	public Tuple getCustom(String url) throws IOException {
		HttpEntity entity = get(customUrl(url));
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, Tuple.class);
	}
	
	public Tuple save(Tuple tuple) throws IOException {
		String type = tuple.getType();
		HttpEntity entity = post(getUrl(type), tuple);
		if (null == entity) {
			throw new NoRecordException(type);
		}
		return deserialize(entity, Tuple.class);
	}
	
	public List<Tuple> save(List<Tuple> objs, String type) throws IOException {	
		HttpEntity entity = post(getMultiUrl(type), objs);
		if (null != entity) {
			return deserialize(entity, new TypeReference<ArrayList<Tuple>>() {
			});
		}
		throw new NoRecordException(type);
	}
	
	@Override
	protected final void setAuthentication(HttpMessage request) {
		HashMap<String, String> headers = authClient.getHeaders(username, password, appn, deviceId);
		for (Entry<String, String> entry : headers.entrySet()) {
			request.addHeader(entry.getKey(), entry.getValue());
		}
	}

	public final HashMap<String, String> getAuthHeaders() {
		return authClient.getHeaders(username, password, appn, deviceId);
	}

	protected final String getMultiUrl(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/data/").append(type).append("/multi");
		return sb.toString();
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

	protected final String actionUrl(String action, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/exec/");
		sb.append("/").append(type).append("/").append(action);
		return sb.toString();
	}

	protected final String actionUrl(String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append("/")
				// .append("/api/")
				.append(appn).append("/exec/__common/").append(action);
		return sb.toString();
	}

	protected final String listUrl(String type) {
		StringBuilder sb = new StringBuilder(type.length() + 32);
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

	protected final String customUrl(String url) {
		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append("/").append(appn).append("/").append(url);
		return sb.toString();
	}

	public final Tuple executeAction(String action) throws IOException {
		HttpEntity entity = post(actionUrl(action), "{}");
		if (null == entity) {
			throw new NoActionResultException(action);
		}
		return deserialize(entity, Tuple.class);
	}

	public final Tuple executeAction(String action, Tuple tuple) throws IOException {
		HttpEntity entity = post(actionUrl(action), tuple);
		if (null == entity) {
			throw new NoActionResultException(action);
		}
		return deserialize(entity, Tuple.class);
	}

	public final ArrayList<Tuple> executeActionList(String action, Tuple tuple) throws IOException {
		HttpEntity entity = post(actionUrl(action), tuple);
		if (null == entity) {
			throw new NoActionResultException(action);
		}
		ArrayList<Tuple> result = deserialize(entity, new TypeReference<ArrayList<Tuple>>() {
		});
		return result;
	}

	public final Tuple executeAction(String action, String type) throws IOException {
		HttpEntity entity = post(actionUrl(action, type), "{}");
		if (null == entity) {
			throw new NoRecordException(type);
		}
		return deserialize(entity, Tuple.class);
	}

	public final Tuple executeAction(String action, Tuple tuple, String type) throws IOException {
		HttpEntity entity = post(actionUrl(action, type), tuple);
		if (null == entity) {
			throw new NoRecordException(type);
		}
		return deserialize(entity, Tuple.class);
	}

	public final ArrayList<Tuple> executeActionList(String action, Tuple tuple, String type) throws IOException {
		HttpEntity entity = post(actionUrl(action, type), tuple);
		if (null == entity) {
			throw new NoRecordException(type);
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

	public void setAuthClient(AuthClient authClient) {
		if (null != authClient)
			this.authClient = authClient;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
