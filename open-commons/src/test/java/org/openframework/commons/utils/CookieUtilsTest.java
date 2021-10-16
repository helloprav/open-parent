package org.openframework.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.openframework.commons.constants.CookieConstants;

public class CookieUtilsTest {

	@Test
	public void testCreateCookie() {

		Cookie cookie = CookieUtils.createCookie("cookieName", "cookieValue");

		assertEquals(cookie.getName(), "cookieName");
		assertEquals(cookie.getValue(), "cookieValue");
		assertEquals(cookie.getPath(), CookieConstants.COOKIE_PATH);
		assertEquals(cookie.getMaxAge(), CookieConstants.COOKIE_EXPIRY_TIME * 60);
	}

	//@Test
	public void testGetCookieValue() {

		String returnVal = CookieUtils.getCookieValue(null, "cookieValue");

		assertEquals(returnVal, "cookieValue");
	}


	public static String getCookieValue(HttpServletRequest request, String cookieName) {

		String cookieValue = null;
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}

}
