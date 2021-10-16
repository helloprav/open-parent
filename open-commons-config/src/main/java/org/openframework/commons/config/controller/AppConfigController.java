package org.openframework.commons.config.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

import org.openframework.commons.config.constants.AppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/secure")
//@ApiIgnore
public class AppConfigController {

	@Autowired
	private I18nService i18nService;

	@RequestMapping(method = RequestMethod.GET)
	public String init(HttpServletRequest request) {

		System.out.println("CONFIG_APP_NAME:: "+ request.getServletContext().getAttribute(AppConstants.CONFIG_APP_NAME));
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "config-home";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/languages")
	public String getLanguages(HttpServletRequest request) {
		printServlets(request);
		List<LanguageBean> languages = i18nService.retrieveSupportedLanguages();
		request.setAttribute("languages", languages);
		request.setAttribute("messageType", request.getParameter("messageType"));
		return "languagesAjax";
	}

	private void printServlets(HttpServletRequest request) {

		Map<String, ? extends ServletRegistration> servletReg = request.getServletContext().getServletRegistrations();
		System.out.println(servletReg);
		Iterator<?> iterator = servletReg.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			System.out.println("entry: "+object);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/messages/{messageType}/{language}")
	public String getMessagesFromTypeAndLocale(HttpServletRequest request, @PathVariable String messageType,
			@PathVariable String language) {

		Map<String, String> messages = i18nService.getMessageProperties(language, messageType);
		request.setAttribute("messages", messages);
		return "messagesAjax";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/messages/{messageType}/{language}")
	public String updateMessagesForTypeAndLocale(HttpServletRequest request, @PathVariable String messageType,
			@PathVariable String language) {

		i18nService.updateMessageProperties(language, messageType, request.getParameter("key"),
				request.getParameter("value"));
		return getMessagesFromTypeAndLocale(request, messageType, language);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/config/{configName}")
	public String getConfig(HttpServletRequest request, @PathVariable String configName, Model model) {

		model.addAttribute("configName", configName);
		model.addAttribute("configTemp", i18nService.getAppConfigsMap().get(configName));
		model.addAttribute("config", i18nService.getAppConfigsMap().get(configName));
		return "configAjax";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/config/{configName}")
	public String updateConfig(HttpServletRequest request, @PathVariable String configName, Model model) {

		i18nService.updateConfigProperties(configName, request.getParameter("key"), request.getParameter("value"));
		return getConfig(request, configName, model);
	}

}
