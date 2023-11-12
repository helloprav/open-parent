package org.openframework.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private JsonUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> String convertObjectToJsonString(T instance) {

		String jsonString = null;
		ObjectMapper obj = new ObjectMapper();
		obj.setSerializationInclusion(Include.NON_NULL);
		try {
			// Converting the Java object into a JSON string
			jsonString = obj.writeValueAsString(instance);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

}
