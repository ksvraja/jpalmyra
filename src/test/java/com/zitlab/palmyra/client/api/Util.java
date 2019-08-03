/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.api;

import com.zitlab.palmyra.client.rest.PalmyraClient;

/**
 * @author ksvraja
 *
 */
public class Util {
	private static String baseUrl = "http://localhost:8080/palmyra/v1";
	private static String username = "raja";
	private static String password = "zit";
	private static String appn="atsm";
	
	public static final PalmyraClient getClient() {
		return  new PalmyraClient(baseUrl, username, password, appn);
	}
}
