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
	//private static String host = "http://13.234.203.190/fluwiz/api/v2";
	private static String host = "http://localhost:8080/fluwiz/api/v2";
	private static String baseUrl = host ;
	private static String username = "orch";
	private static String password = "password";
	private static String appn="atsmdev";
	
	public static final PalmyraClient getClient() {
		return  new PalmyraClient(baseUrl, username, password, appn);
	}
}
