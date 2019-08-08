package com.zitlab.palmyra.client.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zitlab.palmyra.client.json.TupleDeserializer;
import com.zitlab.palmyra.client.json.TupleSerializer;

/**
 * The base class for all the data operations. This class will carry the
 * information from the json format and transfer all the way to the database
 * object.
 * 
 * @author ksvraja
 *
 */

@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(using = TupleDeserializer.class)
@JsonSerialize(using = TupleSerializer.class)

public class Tuple {
	// The primary key of the Tuple.
	private String id;

	protected String type;	
	
	private Integer action;	
	
	private String label;
	
	/**
	 * Parent configuration items. these tuples will not be altered unless mentioned
	 * by the action flag
	 * 
	 */
	protected HashMap<String, Tuple> parent = new LinkedHashMap<String, Tuple>();

	protected HashMap<String, List<Tuple>> children = new LinkedHashMap<String, List<Tuple>>();

	protected HashMap<String, Object> attributes = new LinkedHashMap<String, Object>();

	public Tuple() {

	}

	public Tuple(String type) {
		this.type = type;
	}

	public Tuple(String type, String id) {
		this.type = type;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(null != id) {
			if(0 == id.trim().length()) {
				id = null;
			}
		}
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Tuple> getParent() {
		return parent;
	}

	public Tuple getParent(String key) {
		return parent.get(key);
	}
	
	public void setParent(HashMap<String, Tuple> parent) {
		this.parent = parent;
	}

	public void addParent(String key, Tuple value) {
		this.parent.put(key, value);
	}
	
	public void removeParent(String key) {
		this.parent.remove(key);
	}

	public HashMap<String, List<Tuple>> getChildren() {
		return children;
	}

	public void setChildren(HashMap<String, List<Tuple>> children) {
		this.children = children;
	}
	
	public void addChildren(String key, Tuple tuple) {
		List<Tuple> childList = children.get(key);
		if(null != childList) {
			childList.add(tuple);
			return;
		}else {
			childList = new ArrayList<Tuple>();
			children.put(key, childList);
			childList.add(tuple);
		}		
	}
	
	public void setChildren(String key, List<Tuple> tuples) {
		children.remove(key);
		children.put(key, tuples);
	}

	@JsonAnyGetter
	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}	

	@JsonAnySetter
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@JsonIgnore
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public TupleMetaInfo getMetainfo() {
		if(null == label)
			return null;
		
		TupleMetaInfo metaInfo = new TupleMetaInfo();
		metaInfo.setType(type);
		metaInfo.setLabel(label);
		
		return metaInfo;
	}

	public void setMetainfo(TupleMetaInfo metainfo) {
		this.action = metainfo.getActionCode();
		this.type = metainfo.getType();
	}

	public boolean isIdEmpty() {
		return null == id;
	}
	
	@JsonIgnore
	public boolean forDelete() {
		return null == this.action ? false : this.action == Action.DELETE;
	}
	
	/**
	 * @return
	 */
	public int getActionCode() {
		return null == this.action ? Action.NONE : this.action;
	}
	
	public void setActionCode(int code) {
		this.action = code;
	}
	
	@JsonIgnore
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	@JsonIgnore
	public String getAttributeAsString(String name) {
		Object obj = getAttribute(name);
		if (null == obj)
			return null;
		else if (obj instanceof String)
			return (String) obj;
		else
			return obj.toString();
	}

	/**
	 * @return
	 */
	public boolean forCreate() {
		return null == this.action ? false : this.action == Action.CREATE;
	}
	
	@JsonIgnore
	public void setRefAttribute(String field, Object value) {
		int index = field.indexOf('.');
		if(index < 0) {
			this.setAttribute(field, value);	
		}else {
			String ref = field.substring(0, index);
			String _field = field.substring(index +1);
			Tuple reference = parent.get(ref);
			if(null == reference) {
				reference = new Tuple();
				parent.put(ref, reference);
			}
			if(_field.equals("id")) {				
				reference.setId(value);
			}else
				reference.setRefAttribute(_field, value);
		}		
	}
	
	@JsonIgnore
	public Object getRefAttribute(String field) {
		int index = field.indexOf('.');
		if(index < 0) {
			return this.getAttribute(field);	
		}else {
			String ref = field.substring(0, index);
			String _field = field.substring(index +1);
			Tuple reference = parent.get(ref);
			if(null == reference) {
				reference = new Tuple();
				parent.put(ref, reference);
			}
			if(_field.equals("id")) {				
				return reference.getId();
			}else
			return reference.getRefAttribute(_field);
		}		
	}
	
	/**
	 * @param value
	 */
	private void setId(Object value) {
		if(null == value)
			this.id = null;
		else if(value instanceof String)
			this.id = (String) value;
		else
			this.id = value.toString();
			
	}
}