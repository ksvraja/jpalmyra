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
	private static String host = "http://gaussline-biocliq-syn21-fluwiz-2f314f7936d2acc9.elb.ap-south-1.amazonaws.com/fluwiz/api/v2";
	private static String baseUrl = host ;
	private static String username = "orch";
	private static String password = "password";
	private static String appn="atsmdev";
	
	public static final PalmyraClient getClient() {
		return  new PalmyraClient(baseUrl, username, password, appn);
	}
}
