/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.exception;

import java.io.IOException;

/**
 * @author ksvraja
 *
 */
public class ServerErrorException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905001874835886596L;

	public ServerErrorException(final String s) {
		super(s);
	}
	
	public ServerErrorException(final String s, final Throwable t) {
		super(s,t);
	}
	
	public ServerErrorException(int errorcode, final String s) {
		super(s);
	}
}
