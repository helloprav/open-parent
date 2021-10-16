package org.openframework.commons.ofds.controller.interceptor;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.constants.EnvironmentEnum;
import org.openframework.commons.domain.exceptions.AuthenticationException;
import org.openframework.commons.encrypt.EncryptionUtil;
import org.openframework.commons.ofds.constant.LoggingConstants;
import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.ofds.constant.OfdsCookieConstants;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.utils.SpringUtils;
import org.openframework.commons.utils.CookieUtils;
import org.openframework.commons.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OfdsSecurityInterceptor implements HandlerInterceptor {

	final Logger logger = LoggerFactory.getLogger(OfdsSecurityInterceptor.class);

	private static ThreadLocal<UserVO> userProfileHolder = new ThreadLocal<>();

	@Autowired
	private Environment environment;

	@Autowired
	private OfdsSecurityProps ofdsSecurityProps;

	@Autowired
	private MasterDataProps masterDataProps;

	@Autowired
	private EncryptionUtil encryptionUtil;

	public static UserVO getUserProfile() {
		return userProfileHolder.get();
	}

	private static void setUserProfile(UserVO userProfile) {
		userProfileHolder.set(userProfile);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("Entered preHandle:: "+masterDataProps);
		request.setAttribute("startTime", System.nanoTime());
		userProfileHolder.remove();
		String path = getRequestUrl(request);
		MDC.put(LoggingConstants.CLIENT_IP, RequestUtils.getClientIp(request));
		// RequestUtils.getRequestHeadersInMap() can be used to print all request headers
		logger.debug("Entered preHandle()");

		return isUnprotectedRequest(request, path) || null != initUserProfile(request);
	}

	private boolean isUnprotectedRequest(HttpServletRequest request, String path) {

		logger.debug("Request Path: {}", path);
		CookieUtils.printRequestDetails(request);
		for(String url: ofdsSecurityProps.getUnprotectedUrls()) {
			if(path.startsWith(url)) {
				return true;
			}
		}
		for(String urlPattern: ofdsSecurityProps.getUnprotectedUris()) {
			String url = StringUtils.removeEnd(urlPattern, OfdsConstants.SLASH_ASTRIX);
			if(path.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	private String getRequestUrl(HttpServletRequest request) {

		String path = null;

		// get request url based on env
		if(SpringUtils.isActiveProfile(environment, EnvironmentEnum.test.toString())) {
			path = request.getRequestURI();
			setUserProfile(new UserVO(1l));
		} else {
			path = request.getServletPath();
		}
		MDC.put(LoggingConstants.URL, path);
		return path;
	}

	private UserVO initUserProfile(HttpServletRequest request) throws IOException {

		String encryptedCookieValue = OfdsSecurityInterceptor.getCookieValue(request);
		validateAuthString(encryptedCookieValue);

		String plainText = encryptionUtil.decrypt(encryptedCookieValue);
		validateAuthString(plainText);

		ObjectMapper objectMapper = new ObjectMapper();
		UserVO userProfile = objectMapper.readValue(plainText, UserVO.class);
		setUserProfile(userProfile);
		MDC.put(LoggingConstants.USER_ID, Long.toString(userProfile.getId()));
		return userProfile;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		logger.debug("Entered postHandle()");
		MDC.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {

		logger.debug("Entered afterCompletion()");
	}

	private void validateAuthString(String authString) {
		if (StringUtils.isBlank(authString)) {
			throw new AuthenticationException("auth.login.sessionExpired");
		}
	}

	private static String getCookieValue(HttpServletRequest request) {

		String cookieValue = null;
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(OfdsCookieConstants.COOKIE_LIU)) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}
}