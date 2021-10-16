/**
 * 
 */
package org.openframework.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.constants.NumberConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Java Developer
 *
 */
public class StringUtil {

	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(substringBetweenChar("/api/users", "/api/", true));
		System.out.println(substringBetweenChar("/api/users", "/api/", false));
	}

	/**
	 * Replaces token with the value in string. 
	 * 
	 * @param updatedString String from which token is to be replaced.
	 * @param token token in the string.
	 * @param value with which token is to be replaced.
	 * @return String
	 */
	public static String replaceToken(String inputString, String token, String value) {

		logger.debug("replaceToken start ::::" + Thread.currentThread().getId());
		logger.debug("Input String [{}], Token [{}], Value [{}]", inputString, token, value);
		String updatedString = inputString;
		while (updatedString.indexOf(token) != -1) {
			StringBuilder sb = new StringBuilder(updatedString);
			sb.replace(updatedString.indexOf(token),
					updatedString.indexOf(token) + token.length(), value);
			updatedString = sb.toString();
		}
		return updatedString;
	}

	public static String substringBetweenChar(String srcString, String betweenString, boolean includeBetweenString) {

		if(null == srcString) {
			throw new NullPointerException("srcString cannot be null");
		}
		if(srcString.startsWith(betweenString)) {
			int nextIndex = srcString.indexOf(betweenString);
			if(nextIndex == -1) {
				return StringUtils.EMPTY;
			}
			if(includeBetweenString) {
				return srcString.substring(NumberConstants.INT_ZERO, nextIndex+1);
			} else {
				return srcString.substring(NumberConstants.INT_ONE, nextIndex);
			}
		}
		return StringUtils.EMPTY;
	}

}
