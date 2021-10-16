/**
 * 
 */
package org.openframework.commons.ofds.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.openframework.commons.rest.annotations.GetMappingProduces;
import org.openframework.commons.rest.beans.ResponseBean;
import org.openframework.commons.rest.controller.BaseController;
import org.openframework.commons.rest.vo.UserHistoryVO;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.PageList;
import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.service.bo.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * CRUD Operations:
 * 
 * 
 * @author JavaDeveloper
 *
 */
@RestController
@RequestMapping(UserController.USERS_URL)
@Api(value = "User Controller", consumes = "JSON", produces = "JSON")
public class UserController extends BaseController {

	public static final String USERS_URL = "/ofds/api/users";

	@Inject
	private UserService userService;

	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		// do nothing
	}

	/**
	 * This is version [application/vnd.shop.app-v0.1+xml,json] of the url /cat,
	 * which returns all cat
	 * 
	 * @return
	 */
	//@SecuredPermissions("student")
	@GetMappingProduces("")
	public PageList<UserVO> findUsers(@ApiIgnore UserVO userProfile, Pagination pagingDetails) {
		return userService.findUsers(pagingDetails);
	}

	//@SecuredPermissions("student")
	@GetMappingProduces("/status/{isValid}")
	public PageList<UserVO> findUsersByStatus(@PathVariable boolean isValid, Pagination pagingDetails) {
		return userService.findUsersByStatus(isValid, pagingDetails);
	}

	@GetMapping(path = "/{id}")
	public UserVO findUserById(@PathVariable Long id) {
		return userService.findUserById(id);
	}

	@GetMapping(path = "/{id}/history")
	public PageList<UserHistoryVO> findUserHistoryById(@PathVariable Long id, Pagination pagingDetails) {
		return userService.findUserHistoryById(id, pagingDetails);
	}

	@PostMapping()
	public ResponseBean<Object> createUser(@Valid @RequestBody UserVO userVO, @ApiIgnore UserVO loggedInUser) {

		if(null == userVO.getStatus()) {
			userVO.setStatus("active");
		}
		if(null == userVO.getGender()) {
			userVO.setGender("male");
		}
		userVO.setLoggedInUserId(loggedInUser.getId());
		Long id = userService.createUser(userVO);
		setLocationHeader(USERS_URL+"/"+id);
		return new ResponseBean<>(HttpStatus.CREATED.value(), String.format("User %s created successfully", id), id);
	}

	@PutMapping(path = "/{id}")
	public UserVO updateUser(@PathVariable Long id, @Valid @RequestBody UserVO userVO, @ApiIgnore UserVO loggedInUser) {
		userVO.setId(id);
		userVO.setLoggedInUserId(loggedInUser.getId());
		return userService.updateUser(userVO);
	}

	@PatchMapping(path = "/{id}/status/{status}")
	public UserVO updateStatus(@PathVariable Long id, @PathVariable boolean status, @ApiIgnore UserVO loggedInUser) {
		UserVO userVO = new UserVO();
		userVO.setId(id);
		userVO.setIsValid(status);
		userVO.setLoggedInUserId(loggedInUser.getId());
		return userService.updateStatus(userVO);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseBean<Object> deleteUser(@PathVariable Long id, @ApiIgnore UserVO loggedInUser) {

		UserVO userVO = new UserVO();
		userVO.setId(id);
		userVO.setLoggedInUserId(loggedInUser.getId());
		userService.deleteUser(userVO);
		return new ResponseBean<>(HttpStatus.OK.value(), String.format("User [%s] deleted successfully", id), id);
	}

}
