package com.zitlab.palmyra.client.exception;

import java.io.IOException;
import java.util.Map;

public class ApplicationException extends IOException{
	private static final long serialVersionUID = 1L;
	
	private int httpCode;
	private Map<String, Object> response;
	
	public ApplicationException(int code, String message, Map<String, Object> tuple) {
		super(message);
		this.httpCode = code;
		this.response = tuple;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public Map<String, Object> getResponse() {
		return response;
	}
}
