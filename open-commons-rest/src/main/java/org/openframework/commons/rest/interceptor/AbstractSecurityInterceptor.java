package org.openframework.commons.rest.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openframework.commons.constants.EnvironmentEnum;
import org.openframework.commons.encrypt.EncryptionUtil;
import org.openframework.commons.rest.CommonsRestConstants;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractSecurityInterceptor implements HandlerInterceptor {

	public static final String GROUP_LIST = "groupList";
	public static final String FUNCTION_LIST = "functionList";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static ThreadLocal<UserVO> userProfileHolder = new ThreadLocal<>();
	private static ThreadLocal<List<String>> userAccessHolder = new ThreadLocal<>();

	@Autowired
	private Environment environment;

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

	public static List<String> getUserAccess() {
		return userAccessHolder.get();
	}

	protected void setUserAccess(List<String> accessList) {
		userAccessHolder.set(accessList);
	}

	protected void removeUserAccess() {
		userAccessHolder.remove();
	}

	public static ThreadLocal<UserVO> getUserProfileHolder() {
		return userProfileHolder;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public EncryptionUtil getEncryptionUtil() {
		return encryptionUtil;
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
		MDC.put(CommonsRestConstants.URL, path);
		return path;
	}

	@SuppressWarnings("unchecked")
	protected UserVO getUserProfileFromCookie(HttpServletRequest request) throws IOException {

		UserVO userProfile = null;
		String encryptedCookieValue = CookieUtils.getCookieValue(request, CommonsRestConstants.STR_LIU);
		if(isValidAuthString(encryptedCookieValue)) {

			String plainText = encryptionUtil.decrypt(encryptedCookieValue);
			if(isValidAuthString(plainText)) {
	
				ObjectMapper objectMapper = new ObjectMapper();
				userProfile = objectMapper.readValue(plainText, UserVO.class);
				setUserProfile(userProfile);
				List<String> accessList = new ArrayList<>();
				Map<String, Object> otherData = userProfile.getOtherData();
				if(otherData.containsKey(FUNCTION_LIST)) {
					accessList = (List<String>) otherData.get(FUNCTION_LIST);
				}
				if(null == accessList) {
					accessList = new ArrayList<>();
				}
				//accessList.add(userProfile.getRole());
				setUserAccess(accessList);
				MDC.put(CommonsRestConstants.STR_UID, Long.toString(userProfile.getId()));
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

}
