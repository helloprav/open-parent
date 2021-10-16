package org.openframework.commons.email.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	private StringUtil() {
	}

	public static boolean emptyOrNull(final String input) {
		return (null == input || input.equals(StringUtils.EMPTY)) ? true : false;
	}

	/**
	 * Replaces token with the value in string. 
	 * 
	 * @param string String from which token is to be replaced.
	 * @param token token in the string.
	 * @param value with which token is to be replaced.
	 * @return String
	 */
	public static String replaceToken(String string, String token, String value) {

		while (string.indexOf(token) != -1) {
			StringBuilder sb = new StringBuilder(string);
			sb.replace(string.indexOf(token),
					string.indexOf(token) + token.length(), value);
			string = sb.toString();
		}
		return string;
	}

}
