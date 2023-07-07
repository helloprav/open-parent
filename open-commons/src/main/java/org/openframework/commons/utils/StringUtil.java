/**
 * 
 */
package org.openframework.commons.utils;

import java.util.concurrent.ThreadLocalRandom;

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

	/**
	 * Shuffle the original word by randomly swapping characters 10 times
	 * 
	 * @param original
	 * @return
	 */
	public static String getShuffledWord(String original) {
		String shuffledWord = original; // start with original
		int wordSize = original.length();
		int shuffleCount = 10; // let us randomly shuffle letters 10 times
		for (int i = 0; i < shuffleCount; i++) {
			// swap letters in two indexes
			int position1 = ThreadLocalRandom.current().nextInt(0, wordSize);
			int position2 = ThreadLocalRandom.current().nextInt(0, wordSize);
			shuffledWord = swapCharacters(shuffledWord, position1, position2);
		}
		return shuffledWord;
	}

	/**
	 * Swaps characters in a string using the given character positions
	 * 
	 * @param shuffledWord
	 * @param position1
	 * @param position2
	 * @return
	 */
	public static String swapCharacters(String shuffledWord, int position1, int position2) {
		char[] charArray = shuffledWord.toCharArray();
		// Replace with a "swap" function, if desired:
		char temp = charArray[position1];
		charArray[position1] = charArray[position2];
		charArray[position2] = temp;
		return new String(charArray);
	}
}
