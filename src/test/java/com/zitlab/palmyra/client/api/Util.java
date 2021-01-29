/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.api;

import com.zitlab.palmyra.client.PalmyraClient;

/**
 * @author ksvraja
 *
 */
public class Util {
	private static String host = "http://127.0.0.1:6060/palmyra/api/v2";
	private static String baseUrl = host ;
	private static String username = "admin";
	private static String password = "ad";
	private static String appn="pharma";
	
	public static final PalmyraClient getClient() {
		return  new PalmyraClient(baseUrl, username, password, appn);
	}
}
