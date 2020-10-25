package io.pismo.test.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationResponse {

	@JsonProperty
	private List<String> errors = new ArrayList<String>();
	
	public void add(String error) {
		errors.add(error);
	}
}
