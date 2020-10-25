package io.pismo.test.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import io.pismo.test.entity.OperationType;

public class CustomEnumOperatonTypeDeserializer extends StdDeserializer<OperationType> {

	/** serialVersionUID */
	private static final long serialVersionUID = -2525625250640575040L;

	public CustomEnumOperatonTypeDeserializer() {
		super(CustomEnumOperatonTypeDeserializer.class);
	}

	@Override
	public OperationType deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		 
        int typeId = node.asInt();
 
        for (OperationType type : OperationType.values()) {
            if (type.getId() == typeId)  {
                return type;
            }
        }
 
		return null;
	}

}
