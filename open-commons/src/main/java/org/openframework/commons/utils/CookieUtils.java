package org.openframework.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.openframework.commons.constants.CookieConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CookieUtils {

	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	private CookieUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Cookie createCookie(String name, String value) {

		Cookie cookie = new Cookie(name, value);
		cookie.setPath(CookieConstants.COOKIE_PATH);
		cookie.setMaxAge(CookieConstants.COOKIE_EXPIRY_TIME * 60);
		cookie.setHttpOnly(true);
		return cookie;
	}

	public static Cookie createCookieEncoded(String name, String value) {

		Cookie cookie;
		try {
			cookie = new Cookie(name, URLEncoder.encode( value, "UTF-8" ));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		cookie.setPath(CookieConstants.COOKIE_PATH);
		cookie.setMaxAge(CookieConstants.COOKIE_EXPIRY_TIME * 60);
		cookie.setHttpOnly(true);
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

	public static void deleteCookiesByName(HttpServletRequest req, HttpServletResponse resp, List<String> cookieName) {

		if(null == cookieName) {
			cookieName = Collections.<String>emptyList();
		}
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookieName.contains(cookie.getName())) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					resp.addCookie(cookie);
				}
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
		logger.info("Cookie Value for the cookie: {} is {}", cookieName, cookieValue);
		return cookieValue;
	}

	public static <T> T readObjectFromCookie(String cookieStr, Class<T> clazz) {

		T obj;
		try {
			cookieStr = URLDecoder.decode(cookieStr, "UTF-8");
			// Converting the JSON string into Java object
			obj = new ObjectMapper().readValue(cookieStr, clazz);
		} catch (UnsupportedEncodingException|JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return obj;
	}

}
