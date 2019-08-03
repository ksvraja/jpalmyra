/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.pojo;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zitlab.palmyra.client.json.FieldListDeserializer;
import com.zitlab.palmyra.client.json.FieldListSerializer;

/**
 * @author ksvraja
 *
 */
@JsonDeserialize(using = FieldListDeserializer.class)
@JsonSerialize(using = FieldListSerializer.class)
public class FieldList {
	private ArrayList<String> attributes = new ArrayList<String>();
	private HashMap<String, FieldList> reference;
	
	public FieldList() {
		
	}
	
	public ArrayList<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}
	
	public void addField(String field) {
		int index = field.indexOf('.');
		if(index < 0) {
			this.attributes.add(field);	
		}else {
			String ref = field.substring(0, index);
			String _field = field.substring(index +1);
			FieldList list = getFieldList(ref);
			list.addField(_field);
		}		
	}
	
	private FieldList getFieldList(String key) {
		FieldList result = null;
		
		if(null != reference) {			
			result = reference.get(key);
		}else
			reference = new HashMap<String, FieldList>();
		
		if(null == result) {
			result = new FieldList();
			reference.put(key, result);
		}
		return result;
	}

	@JsonIgnore
	public HashMap<String, FieldList> getReference() {
		return reference;
	}

	@JsonIgnore
	public void setReference(HashMap<String, FieldList> reference) {
		this.reference = reference;
	}
}
