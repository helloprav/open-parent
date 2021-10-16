package org.openframework.commons.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class StatelessSecurityFilter implements Filter {

	public static final String COOKIE_LIU_VALUE = "COOKIE_LIU_VALUE";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		getUserRole(request);
		String encryptedCookieValue = getCookieValue(request);
		request.setAttribute(COOKIE_LIU_VALUE, encryptedCookieValue);
		chain.doFilter(servletRequest, servletResponse);
	}

	private String getUserRole(HttpServletRequest request) {

		String cookieValue = null;
		boolean flag = request.isUserInRole("role");
		System.out.println("User Role: " + flag);
		return cookieValue;
	}

	private String getCookieValue(HttpServletRequest request) {

		String cookieValue = null;
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("liu")) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}

	@Override
	public void destroy() {
		// Auto-generated method stub

	}

}
