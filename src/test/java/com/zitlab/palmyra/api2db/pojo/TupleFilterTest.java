/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.api2db.pojo;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.client.pojo.TupleFilter;

/**
 * @author ksvraja
 *
 */
public class TupleFilterTest {
	public static void main(String args[]) throws Exception {
		TupleFilter filter = new TupleFilter();
		ObjectMapper objectMapper = new ObjectMapper();

		StringWriter sw = new StringWriter();

		objectMapper.writeValue(sw, filter);

		System.out.println(sw.toString());

	}
}
