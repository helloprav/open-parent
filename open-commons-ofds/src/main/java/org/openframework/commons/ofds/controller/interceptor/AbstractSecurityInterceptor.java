package org.openframework.commons.ofds.controller.interceptor;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.constants.EnvironmentEnum;
import org.openframework.commons.encrypt.EncryptionUtil;
import org.openframework.commons.ofds.constant.LoggingConstants;
import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.ofds.constant.OfdsCookieConstants;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.utils.SpringUtils;
import org.openframework.commons.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractSecurityInterceptor implements HandlerInterceptor {

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

	protected static void setUserProfile(UserVO userProfile) {
		userProfileHolder.set(userProfile);
	}

	protected static void removeUserProfile() {
		userProfileHolder.remove();
	}

	public static ThreadLocal<UserVO> getUserProfileHolder() {
		return userProfileHolder;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public OfdsSecurityProps getOfdsSecurityProps() {
		return ofdsSecurityProps;
	}

	public MasterDataProps getMasterDataProps() {
		return masterDataProps;
	}

	public EncryptionUtil getEncryptionUtil() {
		return encryptionUtil;
	}

	protected boolean isUnprotectedRequest(HttpServletRequest request, String path) {

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

	protected String getRequestUrl(HttpServletRequest request) {

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

	protected UserVO initUserProfile(HttpServletRequest request) throws IOException {

		UserVO userProfile = null;
		String encryptedCookieValue = AbstractSecurityInterceptor.getCookieValue(request);
		if(isValidAuthString(encryptedCookieValue)) {

			String plainText = encryptionUtil.decrypt(encryptedCookieValue);
			if(isValidAuthString(plainText)) {
	
				ObjectMapper objectMapper = new ObjectMapper();
				userProfile = objectMapper.readValue(plainText, UserVO.class);
				setUserProfile(userProfile);
				MDC.put(LoggingConstants.USER_ID, Long.toString(userProfile.getId()));
			}
		}
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

	public abstract boolean isValidAuthString(String authString);

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
