/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.api;

import java.io.IOException;

import com.zitlab.palmyra.client.pojo.Tuple;
import com.zitlab.palmyra.client.rest.PalmyraClient;

/**
 * @author ksvraja
 *
 */
public class TestTuple {
	public static void main(String args[]) throws IOException{
		PalmyraClient client = Util.getClient();
		Tuple result = client.findById("100", "zit_users");
		System.out.println(result.getId());
	}
}
