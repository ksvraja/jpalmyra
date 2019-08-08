/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author ksvraja
 *
 */
public class BasicAuthClient implements AuthClient{

	@Override
	public HashMap<String, String> getHeaders(String username, String password, String context, String deviceId) {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(HEADER_BASIC_AUTH, authHeader);
		if(null != deviceId)
			result.put(HEADER_DEVICE, deviceId);
		return result;
	}

}
