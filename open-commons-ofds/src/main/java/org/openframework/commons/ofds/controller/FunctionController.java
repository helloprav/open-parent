/**
 * 
 */
package org.openframework.commons.ofds.controller;

import java.util.List;

import javax.inject.Inject;

import org.openframework.commons.domain.exceptions.ApplicationRuntimeException;
import org.openframework.commons.rest.controller.BaseController;
import org.openframework.commons.ofds.service.bo.FunctionService;
import org.openframework.commons.ofds.vo.FunctionVO;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/ofds/api/functions")
@Api(value = "Function Controller", consumes = "JSON", produces = "JSON")
public class FunctionController extends BaseController {

	@Inject
	private FunctionService functionService;

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
	@GetMapping(path = { "", "/" })
	public List<FunctionVO> findFunctions() {
		throw new ApplicationRuntimeException("Not Supported");
	}

	/**
	 * This is version [application/vnd.shop.app-v0.1+xml,json] of the url /cat,
	 * which returns all cat
	 * 
	 * @return
	 */
	@GetMapping(path = { "/status/{isValid}" })
	public List<FunctionVO> findFunctionsByStatus(@PathVariable boolean isValid) {
		return functionService.findFunctions(isValid);
	}

}
