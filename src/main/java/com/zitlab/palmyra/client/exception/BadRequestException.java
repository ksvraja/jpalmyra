/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.io.IOException;

/**
 * @author ksvraja
 *
 */
public class BadRequestException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public BadRequestException(final String s) {
		super(s);
	}
	
	public BadRequestException(final String s, final Throwable t) {
		super(s,t);
	}
	
	public BadRequestException(int errorcode, final String s) {
		super(s);
	}
}
