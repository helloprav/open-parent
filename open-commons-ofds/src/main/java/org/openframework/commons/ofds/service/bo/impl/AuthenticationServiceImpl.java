/**
 * 
 */
package org.openframework.commons.ofds.service.bo.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.domain.exceptions.ApplicationValidationException;
import org.openframework.commons.email.EmailApplication;
import org.openframework.commons.email.service.EmailService;
import org.openframework.commons.email.vo.EmailEvent;
import org.openframework.commons.encrypt.EncryptionUtil;
import org.openframework.commons.encrypt.HashingUtils;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.ofds.constant.OfdsCookieConstants;
import org.openframework.commons.ofds.email.LoginEmailServiceImpl;
import org.openframework.commons.ofds.entity.Function;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.service.as.FunctionAS;
import org.openframework.commons.ofds.service.as.UserAS;
import org.openframework.commons.ofds.service.bo.AuthenticationService;
import org.openframework.commons.ofds.utils.OfdsCookieUtils;
import org.openframework.commons.utils.CookieUtils;
import org.openframework.commons.ofds.vo.UserCredentialsVO;
import org.openframework.commons.ofds.service.adaptor.UserAdaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Java Developer
 *
 */
@Service
public class AuthenticationServiceImpl extends BaseServiceImpl implements AuthenticationService {

	@Inject
	private UserAS userAS;

	@Inject
	private FunctionAS functionAS;

	@Inject
	private UserAdaptor userAdaptor;

	@Inject
	EncryptionUtil encryptionUtil;

	@Inject
	private ApplicationContext appContext;

	@Inject
	private EmailApplication emailApplication;

	@Override
	public Map<String, Object> authenticateUser(UserCredentialsVO userCredentialsVO) {

		UserVO userVO = validateUserCrenetials(userCredentialsVO);

		Map<String, Object> validUserDetailsMap = new HashMap<>();
		validUserDetailsMap.put(OfdsConstants.USERVO, userVO);

		validUserDetailsMap.put(OfdsConstants.LOGIN_COOKIE_LIST, getLoginCookieList(userVO));
		triggerEmailEvent(userVO);
		//sendEmailForLoginSuccess(userVO);
		return validUserDetailsMap;
	}

	private void triggerEmailEvent(UserVO userVO) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("key", userVO.getFirstName());
		map.put("email", userVO.getEmail());
		getPublisher().publishEvent("userVO");
		getPublisher().publishEvent(new EmailEvent("Email Event", map));
		getPublisher().publishEvent(map);
	}

	private void sendEmailForLoginSuccess(UserVO userVO) {

		EmailService emailService = appContext.getBean(LoginEmailServiceImpl.class);
		emailService.init(org.openframework.commons.ofds.constant.EmailConstants.TEMPLATE_LOGIN_SUCCESS, userVO, userVO.getEmail());
		emailApplication.execute(emailService);
	}

	private List<Cookie> getLoginCookieList(UserVO userVO) {

		List<Cookie> loginCookieList = new ArrayList<>();
		String loggedInUserCookieValue = OfdsCookieUtils.getLoggedInUserCookieValue(userVO);
		Cookie uid = CookieUtils.createCookie(OfdsCookieConstants.COOKIE_UID, Long.toString(userVO.getId()));
		Cookie liu = CookieUtils.createCookie(OfdsCookieConstants.COOKIE_LIU,
				encryptionUtil.encrypt(loggedInUserCookieValue));
		loginCookieList.add(uid);
		loginCookieList.add(liu);

		return loginCookieList;
	}

	private UserVO validateUserCrenetials(UserCredentialsVO userCredentialsVO) {

		validateLoginParameters(userCredentialsVO);
		boolean loginSuccess = false;
		UserVO userVO = null;

		User user = findByUsernameOrEmailOrMobile(userCredentialsVO);
		// if user entered email
		if (checkIfValidUser(user) && isValidPassword(userCredentialsVO, user)) {
			loginSuccess = true;
		}
		if (loginSuccess) {
			userVO = userAdaptor.toVO(user);
			if(userVO.getIsSuperAdmin()) {
				// get all functions for super admin
				List<Function> functionList = functionAS.findFunctions(true);
				userAdaptor.populateUserAccess(functionList, userVO);
			} else {
				User userWithAccess = userAS.findUserGroupAndAccess(user);
				userAdaptor.populateUserAccess(userWithAccess, userVO);
			}
		} else {
			throw new ApplicationValidationException().addValidationErrors("auth.login.invalidCredentials");
		}

		return userVO;
	}

	/**
	 * Validates that 1) both of email & mobile are not empty, 2) Password is not
	 * empty
	 * 
	 * @param userCredentialsVO
	 */
	private void validateLoginParameters(UserCredentialsVO userCredentialsVO) {

		boolean usernameMissing = StringUtils.isBlank(userCredentialsVO.getUsername());
		boolean passwordMissing = null == userCredentialsVO.getPassword() || StringUtils.isBlank(String.valueOf(userCredentialsVO.getPassword()));

		if((usernameMissing || passwordMissing)) {
			throw new ApplicationValidationException("username or email or mobile and password is required");
		}
	}

	private boolean isValidPassword(UserCredentialsVO userVO, User user) {

		String hashedPassword;
		try {
			String plainMessage = String.valueOf(userVO.getPassword());
			hashedPassword = HashingUtils.sha1(plainMessage);
			return hashedPassword.equals(user.getPassword());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {

			logger.error("Error caught while validating password for username {}", userVO.getUsername(), ex);
			throw new ApplicationValidationException("There is some error matching password");
		}
	}

	private User findByUsernameOrEmailOrMobile(UserCredentialsVO userVO) {

		return userAS.findUserByUsernameOrEmailOrMobile(userAdaptor.fromVO(userVO));
	}

	private boolean checkIfValidUser(User user) {

		boolean validUser = true;
		if (null == user || null == user.getPassword()) {
			validUser = false;
		}
		return validUser;
	}

}
