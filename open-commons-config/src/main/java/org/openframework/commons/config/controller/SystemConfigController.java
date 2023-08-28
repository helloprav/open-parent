package org.openframework.commons.config.controller;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.openframework.commons.spring.utils.SpringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secure")
public class SystemConfigController {

	@GetMapping(value = "/system/properties")
	public String getSystemProperties(HttpServletRequest request) {

		request.setAttribute("messages", new TreeMap<Object, Object>(System.getProperties()));
		return "sysPropsAjax";
	}

	@GetMapping(value = "/system/beans")
	public String getSystemBeans(HttpServletRequest request) {

		request.setAttribute("messages", SpringUtils.getSpringBeans());
		return "sysBeansAjax";
	}

}
