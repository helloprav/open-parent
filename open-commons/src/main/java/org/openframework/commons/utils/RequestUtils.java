package org.openframework.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

	private RequestUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getClientIp(HttpServletRequest request) {

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
				if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
		            InetAddress localip;
					try {
						localip = java.net.InetAddress.getLocalHost();
			            remoteAddr = localip.getHostAddress();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
		        }
			}
		}

		return remoteAddr;
	}

	public static Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {

		Map<String, String> result = new HashMap<>();

		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			result.put(key, value);
			System.out.println(key + ":" + value);
		}

		return result;
	}
}
