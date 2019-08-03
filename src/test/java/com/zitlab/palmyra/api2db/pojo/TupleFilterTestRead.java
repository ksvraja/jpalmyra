/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.api2db.pojo;

import java.io.File;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.client.pojo.TupleFilter;

/**
 * @author ksvraja
 *
 */
public class TupleFilterTestRead {
	public static void main(String args[]) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File("D:/TupleFilter.txt");

		TupleFilter tuple = objectMapper.readValue(file, TupleFilter.class);

		StringWriter sw = new StringWriter();

		objectMapper.writeValue(sw, tuple.getFields().getAttributes());

		System.out.println(sw.toString());

	}
}
