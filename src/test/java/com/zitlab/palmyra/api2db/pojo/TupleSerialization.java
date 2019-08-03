/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.api2db.pojo;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public class TupleSerialization {
	public static void main(String args[]) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		
		Tuple tuple = new Tuple("mrci_exam", "23");
		
		Tuple series = new Tuple("mrci_series", "23");
		
		tuple.addChildren("series", series);
		
		StringWriter sw = new StringWriter();

		objectMapper.writeValue(sw, tuple);

		System.out.println(sw.toString());
	}
}
