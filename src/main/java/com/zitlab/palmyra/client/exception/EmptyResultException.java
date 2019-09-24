package com.zitlab.palmyra.client.exception;

import java.io.IOException;

public class EmptyResultException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3981139258651831459L;
	private String type;
	
	
	public EmptyResultException(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
}
