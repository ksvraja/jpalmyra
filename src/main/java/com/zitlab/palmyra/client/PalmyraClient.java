/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zitlab.palmyra.client.exception.ClientException;
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
public class PalmyraClient extends TupleRestClient {
//	private static final Logger logger = LoggerFactory.getLogger(PalmyraClient.class);

	public PalmyraClient(String baseUrl, String username, String password, String appn) {
		super(baseUrl, username, password, appn);
	}

	public <T> void delete(String id, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);
		HttpEntity entity = delete(getUrl(type, id));
		if (null == entity) {
			throw new NoRecordException(type);
		}
	}

	public <T> T findById(String id, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);

		HttpEntity entity = get(getUrl(type, id));
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		return null;

	}

	public <T> T findUnique(TupleFilter<T> tuple, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);
		HttpEntity entity = get(uniqueUrl(type));
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		return null;
	}

	public <T> T findUnique(String key, String value, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);
		TupleFilter<Tuple> filter = new TupleFilter<Tuple>();
		Tuple tuple = new Tuple(type);
		tuple.setAttribute(key, value);
		filter.setCriteria(tuple);
		HttpEntity entity = get(uniqueUrl(type));
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		return null;
	}

	public <T> TupleResultSet<T> query(TupleFilter<T> filter, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);	
		HttpEntity entity = post(queryUrl(type), filter);
		if (null != entity) {
			return deserialize(entity, new TypeReference<TupleResultSet<T>>() {
			});
		}
		throw new NoRecordException(type);		
	}
	
	public <T> ArrayList<T> list(TupleFilter<T> filter, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);	
		HttpEntity entity = post(listUrl(type), filter);
		if (null != entity) {
			return deserialize(entity, new TypeReference<ArrayList<T>>() {
			});
		}
		throw new NoRecordException(type);		
	}

	public <T> T save(Object obj, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);		
		HttpEntity entity = post(getUrl(type), obj);
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		throw new NoRecordException(type);
	}
	
	public <T> List<T> save(List<T> objs, String type,  Class<T> valueType) throws IOException {	
		HttpEntity entity = post(getMultiUrl(type), objs);
		if (null != entity) {
			return deserialize(entity, new TypeReference<ArrayList<T>>() {
			});
		}
		throw new NoRecordException(type);
	}
	
	public <T> List<T> save(List<Object> objs, Class<T> valueType) throws IOException {
		String type = getAnnotation(valueType);		
		HttpEntity entity = post(getMultiUrl(type), objs);
		if (null != entity) {
			return deserialize(entity, new TypeReference<ArrayList<T>>() {
			});
		}
		throw new NoRecordException(type);
	}

	public <T> T save(Object obj, Class<T> valueType, String id) throws IOException {
		if (null == id)
			throw new ClientException("'id' cannot be null while saving the data");
		String type = getAnnotation(valueType);		
		HttpEntity entity = post(getUrl(type, id), obj);
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		throw new NoRecordException(type);
	}

	public <T> ArrayList<T> list(String url, Object obj, Class<T> valueType) throws IOException {
		HttpEntity entity = post(customUrl(url), obj);
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, new TypeReference<ArrayList<T>>() {
		});
	}
	
	public <T> T postCustom(String url, Object obj, Class<T> valueType) throws IOException {
		HttpEntity entity = post(customUrl(url), obj);
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, valueType);
	}
	
	public <T> T getCustom(String url, Class<T> valueType) throws IOException {
		HttpEntity entity = get(customUrl(url));
		if (null == entity) {
			throw new EmptyResultException(url);
		}
		return deserialize(entity, valueType);
	}
	
	public <T> T execute(String action, Object obj, Class<T> valueType) throws IOException {		
		HttpEntity entity = post(actionUrl(action), obj);
		if (null != entity) {
			return deserialize(entity, valueType);
		}
		throw new NoActionResultException(action);
	}

	private static String getAnnotation(Class<?> t) {
		if (t.isAnnotationPresent(PalmyraType.class)) {
			PalmyraType type = t.getAnnotation(PalmyraType.class);
			;
			return type.value();
		} else {
			return t.getSimpleName();
		}
	}
}
