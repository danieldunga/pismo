package io.pismo.test.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.pismo.test.util.deserializer.CustomEnumOperatonTypeDeserializer;

@JsonDeserialize(using = CustomEnumOperatonTypeDeserializer.class)
public enum OperationType {
	
	COMPRA_A_VISTA(1), 
	COMPRA_PARCELADA(2),
	SAQUE(3),
	PAGAMENTO(4);

	@JsonValue
	private final int id;
	
	OperationType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
