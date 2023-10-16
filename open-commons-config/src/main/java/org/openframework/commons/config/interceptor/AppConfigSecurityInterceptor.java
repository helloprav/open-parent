package org.openframework.commons.config.interceptor;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// stale: remove this
@Deprecated
//@Component
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
