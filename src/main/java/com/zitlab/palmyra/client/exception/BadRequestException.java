/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.util.Map;

/**
 * @author ksvraja
 *
 */
public class BadRequestException extends ApplicationException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public BadRequestException(final String message) {
		super(400, message, null);
	}
	
	public BadRequestException(Map<String, Object> response) {
		super(400, null, response);
	}
	
	public BadRequestException(Map<String, Object> response, final String s) {
		super(400, s, response);
	}
	
	public BadRequestException(final String s, final Throwable t) {
		super(400, s, null, t);
	}
	
	public BadRequestException(Map<String, Object> response, final Throwable t) {
		super(400, null, response, t);
	}
}
