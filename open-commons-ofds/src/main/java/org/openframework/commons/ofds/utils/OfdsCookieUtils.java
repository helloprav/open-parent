package org.openframework.commons.ofds.utils;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.rest.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OfdsCookieUtils {

	private static final Logger logger = LoggerFactory.getLogger(OfdsCookieUtils.class);

	private OfdsCookieUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getLoggedInUserCookieValue(UserVO userVO) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String value;
		try {
			value = objectMapper.writeValueAsString(userVO);
		} catch (JsonProcessingException e) {
			value = StringUtils.EMPTY;
		}
		logger.debug("getLoggedInUserCookieValue(): "+value);
		return value;
	}
}
