package com.zitlab.palmyra.client.pojo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class TupleMetaInfo {
	
	String type;
	Integer action;
	String label;
		
	@JsonIgnore
	private String labelFormat;
	
	@JsonIgnore
	private Map<String, Object> modAttributes;
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	
	public String getLabelFormat() {
		return labelFormat;
	}
	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}
	public Map<String, Object> getModAttributes() {
		return modAttributes;
	}
	public void setModAttributes(Map<String, Object> modAttributes) {
		this.modAttributes = modAttributes;
	}	
	public String getLabel() {
		return label;
	}
	@JsonIgnore
	public void setLabel(String label) {
		this.label = label;
	}	
	
	@JsonIgnore
	public boolean forCreate() {
		return null == this.action ? false : this.action == Action.CREATE;
	}

	@JsonIgnore
	public boolean forDelete() {
		return null == this.action ? false : this.action == Action.DELETE;
	}

//	@JsonIgnore
//	public boolean forRelationDelete() {
//		return (null == this.relAction) ? false : (this.relAction == Action.DELETE);
//	}

	@JsonIgnore
	public boolean forUpdate() {
		return null == this.action ? false : this.action == Action.UPDATE;
	}

	@JsonIgnore
	public Integer getActionCode() {
		return action;
	}

	public void setAction(String actn) {		
		if (null == actn) {
			this.action = null;
			return;
		}

		this.action = Action.getInt(actn);
		if(this.action == Action.UPDATE && null == modAttributes)
			modAttributes = new HashMap<String, Object>();		
	}

	public void setActionCode(Integer action) {
		this.action = action;
		if (Action.UPDATE == action && null == modAttributes)
			modAttributes = new HashMap<String, Object>();
	}

}
