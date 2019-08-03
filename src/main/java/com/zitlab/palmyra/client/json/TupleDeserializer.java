/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.json;

import com.zitlab.palmyra.client.pojo.Tuple;

/**
 * @author ksvraja
 *
 */
public class TupleDeserializer extends AbstractTupleDeserializer<Tuple>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5887623414574835867L;

	@Override
	protected Tuple create() {
		// TODO Auto-generated method stub
		return new Tuple();
	}

}
