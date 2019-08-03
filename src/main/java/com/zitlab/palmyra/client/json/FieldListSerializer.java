package com.zitlab.palmyra.client.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.zitlab.palmyra.client.pojo.FieldList;

public class FieldListSerializer extends StdSerializer<FieldList> {
	private static final long serialVersionUID = 1L;

	public FieldListSerializer() {
		this(null);
	}

	public FieldListSerializer(Class<FieldList> vc) {
		super(vc);
	}

	@Override
	public void serialize(FieldList value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		writeFieldList(value, gen);
	}

	private void writeFieldList(FieldList value, JsonGenerator gen) throws IOException {
		if (null != value) {
			gen.writeStartArray();
	
			List<String> attributes = value.getAttributes();
			if (null != attributes && attributes.size() > 0) {
				for (String field : attributes) {
					gen.writeString(field);
				}
			}

			HashMap<String, FieldList> reference = value.getReference();
			if(null != reference && reference.size() > 0) {
				for(Entry<String, FieldList> entry : reference.entrySet()) {
					String key = entry.getKey();
					FieldList fieldList = entry.getValue();
					if(null != fieldList) {
						gen.writeStartObject();
						gen.writeFieldName(key);
						writeFieldList(fieldList, gen);
						gen.writeEndObject();
					}
				}
			}
			gen.writeEndArray();
		}
	}
}
