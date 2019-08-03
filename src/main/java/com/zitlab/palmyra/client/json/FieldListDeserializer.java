/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.zitlab.palmyra.client.pojo.FieldList;

/**
 * @author ksvraja
 *
 */
public class FieldListDeserializer extends StdDeserializer<FieldList> {

	private static final long serialVersionUID = -8978657427175466913L;

	public FieldListDeserializer() {
		this(null);
	}

	public FieldListDeserializer(Class<FieldList> vc) {
		super(vc);
	}

	@Override
	public FieldList deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode root = jp.getCodec().readTree(jp);
		FieldList obj = getObj(root, jp);
		return obj;
	}

	private FieldList getObj(JsonNode root, JsonParser jp) throws IOException {
		FieldList obj = new FieldList();
		obj.setAttributes(new ArrayList<String>());
		HashMap<String, FieldList> subMap = new LinkedHashMap<String, FieldList>();
		Iterator<JsonNode> elements = root.elements();
		JsonNode node;
		while (elements.hasNext()) {
			node = elements.next();
			switch (node.getNodeType()) {
			case STRING:
				obj.getAttributes().add(node.textValue());
				break;
			case OBJECT:
				Iterator<Entry<String, JsonNode>> fieldList = node.fields();
				while (fieldList.hasNext()) {
					Entry<String, JsonNode> field = fieldList.next();
					subMap.put(field.getKey(), getObj(field.getValue(), jp));
				}
				break;
			default:
				// Do None
			}
		}
		if (subMap.size() > 0)
			obj.setReference(subMap);
		return obj;
	}
}
