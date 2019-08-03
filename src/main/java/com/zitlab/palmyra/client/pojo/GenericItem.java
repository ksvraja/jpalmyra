package com.zitlab.palmyra.client.pojo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class GenericItem {
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	@JsonIgnore
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@JsonIgnore
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@JsonAnyGetter
	public Map<String, Object> getAllAttributes() {
		return this.attributes;
	}
	
	@JsonAnySetter
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}
}
