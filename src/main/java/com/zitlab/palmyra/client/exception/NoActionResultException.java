package com.zitlab.palmyra.client.exception;

import java.io.IOException;

public class NoActionResultException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3981139258651831459L;
	private String action;
	
	public NoActionResultException(String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}
	
}
