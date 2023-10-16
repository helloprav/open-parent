/**
 * 
 */
package org.openframework.commons.ofds.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.ofds.service.bo.AuthenticationService;
import org.openframework.commons.ofds.vo.UserCredentialsVO;
import org.openframework.commons.rest.beans.ResponseBean;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 
 * CRUD Operations:
 * 
 * 
 * @author JavaDeveloper
 *
 */
@RestController
@RequestMapping("/ofds/api/auth")
@Tag( name = "Authentication Controller", description = "API to manage user's authentication and session")
public class AuthenticationController extends OfdsBaseController {

	@Autowired
	private AuthenticationService authenticationService;

//	@Inject
//	private HttpServletRequest request;

	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		// do nothing
	}

	@PostMapping("/login")
	@Operation(summary = "Create a clone to fight in the clones war")
	public ResponseBean<UserVO> login(@Valid @RequestBody UserCredentialsVO userVO, HttpServletResponse response) {

		logger.debug("Login Requested");
		System.out.println(this.getMasterDataProps());
		System.out.println(this.getEncryptionProps());
		logger.debug(""+this.getMasterDataProps());
		logger.debug(""+this.getEncryptionProps());
		Map<String, Object> validUserDetailsMap = authenticationService.authenticateUser(userVO);
		UserVO loggedInUser = (UserVO)validUserDetailsMap.get(OfdsConstants.USERVO);
//		request.setAttribute("loggedInUser", loggedInUser);

		@SuppressWarnings("unchecked")
		List<Cookie> loginCookieList = (List<Cookie>)validUserDetailsMap.get(OfdsConstants.LOGIN_COOKIE_LIST);
		CookieUtils.createLoginCookies(loginCookieList, response);
		setLocationHeader("/api/profile");
		return new ResponseBean<>(HttpStatus.CREATED.value(),
				String.format("User [%s] created successfully", userVO.getUsername()), loggedInUser);
	}

	@DeleteMapping("/logout")
	public ResponseBean<String> logout(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("Logout Requested");

		CookieUtils.deleteLoginCookies(request, response);
		return new ResponseBean<>(HttpStatus.OK.value(), "OK");
	}

}
