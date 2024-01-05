package org.openframework.commons.ofds.controller.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.domain.exceptions.AuthenticationException;
import org.openframework.commons.rest.CommonsRestConstants;
import org.openframework.commons.rest.interceptor.AbstractSecurityInterceptor;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.RequestUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OfdsApiSecurityInterceptor extends OfdsBaseHandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("Entered preHandle:: ");
		request.setAttribute("startTime", System.nanoTime());
		removeUserProfile();
		String path = getRequestUrl(request);
		MDC.put(CommonsRestConstants.CLIENT_IP, RequestUtils.getClientIp(request));
		// RequestUtils.getRequestHeadersInMap() can be used to print all request headers
		logger.debug("Entered preHandle()");

		boolean flag = false;
		boolean isUnprotected = isUnprotectedRequest(request, path);
		UserVO userVO = null;
		if(isUnprotected) {
			flag = true;
			if(null!=request.getAttribute("loggedInUser")) {
				userVO = (UserVO) request.getAttribute("loggedInUser");
				AbstractSecurityInterceptor.setUserProfile(userVO);
			}
		} else {
			// if this is a protected URL, then initialize user profile
			userVO = getUserProfileFromCookie(request);
			if(null != userVO) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean isValidAuthString(String authString) {
		if (StringUtils.isBlank(authString)) {
			throw new AuthenticationException("auth.login.sessionExpired");
		}
		return true;
	}

}