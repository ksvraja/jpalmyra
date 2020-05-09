/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.api;

import java.io.IOException;

import com.zitlab.palmyra.client.PalmyraClient;
import com.zitlab.palmyra.client.auth.BasicAuthClient;
import com.zitlab.palmyra.client.auth.PalmyraAuthClient;
import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public class TestTuple {
	public static void main(String args[]) throws IOException{
		PalmyraClient client = Util.getClient();
		client.setAuthClient(new PalmyraAuthClient());
//		client.executeAction("getTeleradSession");		
		//Tuple result = client.findById("100", "zit_users");
		Tuple result = client.findById("879", "mrci_series");
		
		
		System.out.println(result.getId() + " " + result.getAttributeAsString("code"));
	}
}
