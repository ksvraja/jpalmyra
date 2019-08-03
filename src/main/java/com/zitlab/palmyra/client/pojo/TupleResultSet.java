/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ksvraja
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class TupleResultSet<T> {
	List<T> result;
	private Tuple relation;
	private Integer count;
	private Integer offset;
	private Long total;

	private String Error;
	
	public Tuple getRelation() {
		return relation;
	}
	public void setRelation(Tuple relation) {
		this.relation = relation;
	}
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
		if(null != result)
			count = result.size();
		else
			count = null;
	}
	public Integer getCount() {
		return count;
	}	
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
}
