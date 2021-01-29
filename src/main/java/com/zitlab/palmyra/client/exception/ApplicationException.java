package com.zitlab.palmyra.client.exception;

import java.io.IOException;
import java.util.Map;

public class ApplicationException extends IOException{
	private static final long serialVersionUID = 1L;
	
	private int httpCode;
	private Map<String, Object> response;
	
	public ApplicationException(int code, String message, Map<String, Object> response) {
		super(message);
		this.httpCode = code;
		this.response = response;
	}

	public ApplicationException(int code, String message, Map<String, Object> response, Throwable t) {
		super(message, t);
		this.httpCode = code;
		this.response = response;
	}
	
	public int getHttpCode() {
		return httpCode;
	}
	
	public String errorCode() {
		if(null != response) {
			Object errorCode = response.get("errorCode");
			return null != errorCode ? errorCode.toString() : null;
		}
		return null;
	}

	public Map<String, Object> getResponse() {
		return response;
	}
	
	public String getMessage() {
		if(null != response) {
			if(null != response.get("message")) {
				return response.get("message").toString();
			}
		}
		return super.getMessage();
	}
}
