/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.auth;

import java.util.HashMap;

/**
 * @author ksvraja
 *
 */
public interface AuthClient {
	public static final String HEADER_USER = "X-Palmyra-user";
	public static final String HEADER_DEVICE = "X-Palmyra-device";
	public static final String HEADER_TOKEN = "X-Palmyra-token";
	public static final String HEADER_RANDOM = "X-Palmyra-random";
	public static final String HEADER_SECRET = "X-Palmyra-Authorization";
	public static final String HEADER_SYSTEM = "X-Palmyra-system";
	public static final String HEADER_BASIC_AUTH = "Authorization";
	
	public HashMap<String, String> getHeaders(String username, String password, String context, String deviceId);
}
