package io.pismo.test.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for generic use
 * @author danielbraga
 */
public class GenericResponse {
	
	@JsonProperty()
	private String mensage;

	public GenericResponse(String mensage) {
		super();
		this.mensage = mensage;
	}

	public String getMensage() {
		return mensage;
	}

}
