/**
 * 
 */
package org.openframework.commons.masterdata.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.openframework.commons.masterdata.service.bo.AdminService;
import org.openframework.commons.masterdata.vo.AdminStatusVO;
import org.openframework.commons.rest.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @author praveen
 *
 */
@RestController
@ApiIgnore
@RequestMapping("/admin")
public class AdminController extends BaseController {

	private static final String MESSAGE = "message";

	@Autowired
	private AdminService adminService;

    @PostConstruct
    public void init() { //throws InterruptedException {

        logger.debug("init-method");
    	adminService.initAdminStatus();
    }

	@GetMapping(path = { "", "/" })
	public ModelAndView home(ModelAndView mav) {

		mav.setViewName("admin/home");
		List<AdminStatusVO> statusList = adminService.getStatus();
		mav.addObject("itemList", statusList);
		return mav;
	}

	@GetMapping(path = { "/error" })
	public ModelAndView error(ModelAndView mav) {

		mav.setStatus(HttpStatus.NO_CONTENT);
		mav.setViewName("admin/error");
		mav.addObject(MESSAGE, "Please use admin console to complete application setup and initialization");
		return mav;
	}

	@GetMapping(path = { "/create-master-data" })
    public ModelAndView initMasterData(ModelAndView mav) {

    	logger.debug("Entered create-master-data");
    	boolean status = adminService.initMasterData();
    	if(status) {
    		mav.addObject(MESSAGE, "Master Data Created Successfully");
    	} else {
    		mav.addObject(MESSAGE, "Master Data Creation Failed");
    	}
		return home(mav);
    }

}
