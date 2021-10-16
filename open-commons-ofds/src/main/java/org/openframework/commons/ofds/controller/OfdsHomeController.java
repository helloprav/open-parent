package org.openframework.commons.ofds.controller;

import java.util.Map;
import java.util.Properties;

import org.openframework.commons.config.service.I18nService;
import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.rest.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ofds")
public class OfdsHomeController extends BaseController {

	// inject via application.properties
	@Value("${spring.application.name:OFDS}")
	private String applicationName;

	@Autowired
	private I18nService i18nService;

	@GetMapping("")
	public String welcome(Map<String, Object> model) {

		model.put("message", "OFDS App is running");
		model.put("appName", applicationName);

		Properties prop = i18nService.getAppConfigsMap().get(OfdsConstants.CONFIG_APP);
		String redirectPath = (String)prop.get("home.redirect.path");
		logger.debug("redirectPath: {}", redirectPath);
		return redirectPath;
	}
}
