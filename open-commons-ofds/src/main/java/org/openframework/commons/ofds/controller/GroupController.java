/**
 * 
 */
package org.openframework.commons.ofds.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.management.ServiceNotFoundException;
import javax.validation.Valid;

import org.openframework.commons.domain.exceptions.ApplicationValidationException;
import org.openframework.commons.rest.beans.ResponseBean;
import org.openframework.commons.rest.controller.BaseController;
import org.openframework.commons.rest.exception.KeywordNotFoundException;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.service.bo.GroupService;
import org.openframework.commons.ofds.vo.GroupHistoryVO;
import org.openframework.commons.ofds.vo.GroupVO;
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
@RequestMapping(GroupController.GROUPS_URL)
@Api(value = "Group Controller", consumes = "JSON", produces = "JSON")
public class GroupController extends BaseController {

	public static final String GROUPS_URL = "/ofds/api/groups";

	@Inject
	private GroupService groupService;

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
	// TODO Check an article if the url "" OR "/" is valid
	@GetMapping(path = { "", "/" })
	public List<GroupVO> findGroups() {
		return groupService.findGroups();
	}

	/**
	 * This is version [application/vnd.shop.app-v0.2+xml,json] of the url /cat,
	 * which returns parent cat.
	 * 
	 * @return
	 */
	@GetMapping(path = { "/status" })
	public List<GroupVO> findGroupsByStatus(Pagination pagingDetails) {
		return findGroupsByStatus(Boolean.TRUE, pagingDetails);
	}

	/**
	 * This is version [application/vnd.shop.app-v0.2+xml,json] of the url /cat,
	 * which returns parent cat.
	 * 
	 * @return
	 */
	@GetMapping(path = "/status/{status}")
	public List<GroupVO> findGroupsByStatus(@PathVariable Boolean status, Pagination pagingDetails) {
		
		return groupService.findGroupsByStatus(status, pagingDetails);
	}

	@GetMapping(path = "/{id}")
	public GroupVO findGroupById(@PathVariable Long id) throws ServiceNotFoundException {

		if (id == 0) {
			throw new KeywordNotFoundException("The ID is: " + id);
		}
		if (id == -1) {
			List<String> validationErrors = new ArrayList<>();
			validationErrors.add("First Name is required");
			validationErrors.add("Last Name is required");
			throw new ApplicationValidationException(validationErrors);
		}
		if (id == -2) {
			throw new ServiceNotFoundException("Custome error caused in doing something");
		}
		return groupService.findGroupById(id);
	}

	@GetMapping(path = "/{id}/history")
	public List<GroupHistoryVO> findGroupHistoryById(@PathVariable Long id) {

		return groupService.findGroupHistoryById(id);
	}

	@PostMapping
	public ResponseBean<Object> createGroup(@Valid @RequestBody GroupVO groupVO, @ApiIgnore UserVO loggedInUser) {

		groupVO.setLoggedInUserId(loggedInUser.getId());
		Long id = groupService.createGroup(groupVO);
		setLocationHeader(GROUPS_URL + "/" + id);
		return new ResponseBean<>(HttpStatus.CREATED.value(), String.format("Group [%s] created successfully", id), id);
	}

	@PutMapping(path = "/{id}")
	public GroupVO updateGroup(@PathVariable Long id, @Valid @RequestBody GroupVO groupVO, @ApiIgnore UserVO loggedInUser) {

		groupVO.setId(id);
		groupVO.setLoggedInUserId(loggedInUser.getId());
		return groupService.updateGroup(groupVO);
	}

	@PatchMapping(path = "/{id}/status/{status}")
	public GroupVO updateStatus(@PathVariable Long id, @PathVariable Boolean status, @ApiIgnore UserVO loggedInUser) {

		GroupVO groupVO = new GroupVO();
		groupVO.setId(id);
		groupVO.setIsValid(status);
		groupVO.setLoggedInUserId(loggedInUser.getId());
		return groupService.updateStatus(groupVO);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseBean<Object> deleteGroup(@PathVariable Long id, @ApiIgnore UserVO loggedInUser) {

		GroupVO groupVO = new GroupVO();
		groupVO.setId(id);
		groupVO.setLoggedInUserId(loggedInUser.getId());
		groupService.deleteGroup(groupVO);
		return new ResponseBean<>(HttpStatus.OK.value(), String.format("Group [%s] deleted successfully", id), id);
	}
}
