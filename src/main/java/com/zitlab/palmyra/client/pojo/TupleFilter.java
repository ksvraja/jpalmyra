/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ksvraja
 *
 */

@JsonInclude(Include.NON_EMPTY)
public class TupleFilter<T> {
	private T criteria;
	private FieldList fields;
	private List<String> orderBy = new ArrayList<String>();
	private List<String> groupBy = new ArrayList<String>();
	private Integer offset;
	private Integer limit;
	private boolean total;
	
	public TupleFilter() {
		
	}
	
	public T getCriteria() {
		return criteria;
	}
	
	public void setCriteria(T criteria) {
		this.criteria = criteria;
	}
	public List<String> getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(ArrayList<String> orderBy) {
		this.orderBy = orderBy;
	}
	public List<String> getGroupBy() {
		return groupBy;
	}	
	public void setGroupBy(ArrayList<String> groupBy) {
		this.groupBy = groupBy;
	}	
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {		
		this.offset = offset;		
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {		
		this.limit = limit;		
	}
	public FieldList getFields() {
		return fields;
	}
	public void setFields(FieldList fields) {
		this.fields = fields;
	}
	
	public boolean isTotal() {
		return total;
	}

	public void addFields(String fieldList) {
		String[] fieldArray = fieldList.split(",");
		if(null == fields)
			fields = new FieldList();
		String field;
		for(int i=0; i < fieldArray.length; i++) {
			field = fieldArray[i].trim();
			fields.addField(field);
		}
	}
	
	public void setTotal(boolean total) {
		this.total = total;
	}
}
