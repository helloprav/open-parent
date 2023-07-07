package org.openframework.commons.ofds.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.ofds.constant.LoggingConstants;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.RequestUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class OfdsWebSecurityInterceptor extends AbstractSecurityInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("Entered preHandle:: ");
		request.setAttribute("startTime", System.nanoTime());
		removeUserProfile();
		String path = getRequestUrl(request);
		MDC.put(LoggingConstants.CLIENT_IP, RequestUtils.getClientIp(request));
		// RequestUtils.getRequestHeadersInMap() can be used to print all request headers
		logger.debug("Entered preHandle()");

		boolean flag = false;
		boolean isUnprotected = isUnprotectedRequest(request, path);
		UserVO userVO = null;
		if(isUnprotected) {
			flag = true;
		} else {
			// if this is a protected URL, then initialize user profile
			userVO = initUserProfile(request);
			if(null == userVO) {
				String encodedRedirectURL = response.encodeRedirectURL(request.getContextPath() + "/ofds");
				response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
				response.setHeader("Location", encodedRedirectURL);
			} else {
				flag = true;
			}
		}
		return flag;
	}

	public boolean isValidAuthString(String authString) {
		if (StringUtils.isBlank(authString)) {
			return false;
		}
		return true;
	}

}