/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.io.IOException;

/**
 * @author ksvraja
 *
 */
public class UnAuthorizedException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public UnAuthorizedException(final String s) {
		super(s);
	}
	
	public UnAuthorizedException(final String s, final Throwable t) {
		super(s,t);
	}
	
	public UnAuthorizedException(int errorcode, final String s) {
		super(s);
	}
}
