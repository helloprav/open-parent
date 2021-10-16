/**
 * 
 */
package org.openframework.commons.ofds.controller;

import org.openframework.commons.enums.AddressType;
import org.openframework.commons.enums.Gender;
import org.openframework.commons.enums.RelationType;
import org.openframework.commons.enums.UserStatus;
import org.openframework.commons.rest.annotations.GetMappingProduces;
import org.openframework.commons.rest.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 
 * CRUD Operations:
 * 
 * 
 * @author JavaDeveloper
 *
 */
@RestController
@RequestMapping("/ofds/api/validvalues")
@Api(value = "User Controller", description = "REST APIs related to Valid Values in the System!!!!", consumes = "JSON", produces = "JSON")
public class ValidValueController extends BaseController {

	/**
	 * This is version [application/vnd.shop.app-v0.1+xml,json] of the url /cat,
	 * which returns all cat
	 * 
	 * @return
	 */
	@GetMappingProduces ("/addresstypes")
	public AddressType[] findAddressTypes() {
		return AddressType.values();
	}

	@GetMappingProduces ("/genders")
	public Gender[] findGenders() {
		return Gender.values();
	}

	@GetMappingProduces ("/relationtypes")
	public RelationType[] findRelationTypes() {
		return RelationType.values();
	}

	/*
	 * @GetMappingProduces ("/userroles") public UserRole[] findUserRoles() { return
	 * UserRole.values(); }
	 */

	@GetMappingProduces ("/userstatuses")
	public UserStatus[] findUserStatuses() {
		return UserStatus.values();
	}

}
