/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.io.IOException;
import java.util.Map;

/**
 * @author ksvraja
 *
 */
public class UnAuthorizedException extends ApplicationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public UnAuthorizedException(final String message) {
		super(401, message, null);
	}
	
	public UnAuthorizedException(Map<String, Object> response) {
		super(401, null, response);
	}
	
	public UnAuthorizedException(Map<String, Object> response, String message) {
		super(401, message, response);
	}
	
	public UnAuthorizedException(final String s, final Throwable t) {
		super(401, s, null, t);
	}
	
	public UnAuthorizedException(Map<String, Object> response, final Throwable t) {
		super(401, null, response, t);
	}
}
