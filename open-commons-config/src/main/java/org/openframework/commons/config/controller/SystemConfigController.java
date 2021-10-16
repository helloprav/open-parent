package org.openframework.commons.config.controller;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.openframework.commons.spring.utils.SpringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/secure")
//@ApiIgnore
public class SystemConfigController {

	@RequestMapping(method = RequestMethod.GET, value = "/system/properties")
	public String getSystemProperties(HttpServletRequest request) {

		request.setAttribute("messages", new TreeMap<Object, Object>(System.getProperties()));
		return "sysPropsAjax";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/system/beans")
	public String getSystemBeans(HttpServletRequest request) {

		request.setAttribute("messages", SpringUtils.getSpringBeans());
		return "sysBeansAjax";
	}

}
