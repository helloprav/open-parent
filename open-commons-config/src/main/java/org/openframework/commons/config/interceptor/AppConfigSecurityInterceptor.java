package org.openframework.commons.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AppConfigSecurityInterceptor implements HandlerInterceptor {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Object secureStatus = request.getAttribute(ConfigAppConstants.SECURITY_SECURE_MAPPING);
		if (null != secureStatus && (boolean) secureStatus) {
			return true;
		}
		logger.error("Application Settings page invaded by User for the URI: " + request.getRequestURI());
		return false;
	}
}
