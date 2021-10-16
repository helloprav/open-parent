package org.openframework.commons.utils;

import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.constants.CookieConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {

	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	private CookieUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Cookie createCookie(String name, String value) {

		Cookie cookie = new Cookie(name, value);
		cookie.setPath(CookieConstants.COOKIE_PATH);
		cookie.setMaxAge(CookieConstants.COOKIE_EXPIRY_TIME * 60);
		return cookie;
	}

	public static void createLoginCookies(List<Cookie> loginCookieList, HttpServletResponse response) {

		ListIterator<Cookie> iterator = loginCookieList.listIterator();
		while (iterator.hasNext()) {
			Cookie cookie = iterator.next();
			response.addCookie(cookie);
		}
	}

	public static void deleteLoginCookies(HttpServletRequest req, HttpServletResponse resp) {

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
			}
		}
	}

	public static void printRequestDetails(HttpServletRequest request) {

		logger.debug("ContextPath: {}", request.getContextPath());
		logger.debug("PathInfo: {}",request.getPathInfo());
		logger.debug("ServletPath: {}",request.getServletPath());
		logger.debug("RequestURI: {}",request.getRequestURI());
		logger.debug("RequestURL: {}",request.getRequestURL());
		logger.debug("UserPrincipal: {}",request.getUserPrincipal());
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
		logger.info("Cookie Value: {}", cookieValue);
		return cookieValue;
	}

}
