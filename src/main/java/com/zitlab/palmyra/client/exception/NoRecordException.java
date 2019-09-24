package com.zitlab.palmyra.client.exception;

import java.io.IOException;

public class NoRecordException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3981139258651831459L;
	private String type;
	
	
	public NoRecordException(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
}
