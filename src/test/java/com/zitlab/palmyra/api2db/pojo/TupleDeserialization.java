/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.api2db.pojo;

import java.io.File;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public class TupleDeserialization {
	public static void main(String args[]) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File("D:/Tuple.txt");

		Tuple tuple = objectMapper.readValue(file, Tuple.class);

		StringWriter sw = new StringWriter();

		objectMapper.writeValue(sw, tuple.getChildren());
		

		System.out.println(sw.toString());

	}
}
