/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.api;

import java.io.IOException;

import com.zitlab.palmyra.client.PalmyraClient;
import com.zitlab.palmyra.client.auth.PalmyraAuthClient;
import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public class TestTuple {
	public static void main(String args[]) throws IOException{
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "3128");
		PalmyraClient client = Util.getClient();
		client.setAuthClient(new PalmyraAuthClient());
		Tuple result = client.findById("879", "mrci_series");
		
		
		System.out.println(result.getId() + " " + result.getAttributeAsString("code"));
	}
}
