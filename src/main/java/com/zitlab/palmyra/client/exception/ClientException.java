/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.io.IOException;

/**
 * @author ksvraja
 *
 */
public class ClientException extends IOException{

	private int errorCode;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public ClientException(final String s) {
		super(s);
	}
	
	public ClientException(final String s, final Throwable t) {
		super(s,t);
	}
	
	public ClientException(int errorcode, final String s, Throwable t) {
		super(s, t);
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
