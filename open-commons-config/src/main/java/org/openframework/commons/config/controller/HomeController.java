package org.openframework.commons.config.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gconfig/home")
public class HomeController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	private I18nService i18nService;

	@GetMapping()
	public String init(HttpServletRequest request) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "gconfig/html/home/index";
	}

	@GetMapping("/contact")
	public String contactUs(HttpServletRequest request) {

		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "gconfig/html/home/contact";
	}

	@GetMapping("/about")
	public String about(HttpServletRequest request) {

		System.out.println("CONFIG_APP_NAME:: "+ request.getServletContext().getAttribute(ConfigAppConstants.CONFIG_APP_NAME));
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "gconfig/html/home/about";
	}

	@GetMapping("/index1")
	public String init1(HttpServletRequest request) {

		System.out.println("CONFIG_APP_NAME:: "+ request.getServletContext().getAttribute(ConfigAppConstants.CONFIG_APP_NAME));
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "index1";
	}

	@GetMapping("/contact1")
	public String contact1(HttpServletRequest request) {

		System.out.println("CONFIG_APP_NAME:: "+ request.getServletContext().getAttribute(ConfigAppConstants.CONFIG_APP_NAME));
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "contact1";
	}

	@GetMapping("/about1")
	public String about1(HttpServletRequest request) {

		System.out.println("CONFIG_APP_NAME:: "+ request.getServletContext().getAttribute(ConfigAppConstants.CONFIG_APP_NAME));
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);

		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());
		return "about1";
	}

	@GetMapping("/languages")
	public String getLanguages(String messageType) {
		printServlets(request);
		Map<String, MessageResourceLocale> messageResources = i18nService.getMessageResourceMap();
		request.setAttribute("messageResources", messageResources);
		request.setAttribute("configNames", i18nService.getAppConfigsMap().keySet());

		List<LanguageBean> languages = i18nService.retrieveSupportedLanguages();
		request.setAttribute("languages", languages);
		request.setAttribute("messageType", messageType);
		return "gconfig/html/home/languagesAjax";
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

	@GetMapping("/messages/{messageType}/{language}")
	public String getMessagesFromTypeAndLocale(HttpServletRequest request, @PathVariable String messageType,
			@PathVariable String language) {

		Map<String, String> messages = i18nService.getMessageProperties(language, messageType);
		request.setAttribute("messages", messages);
		return "messagesAjax";
	}

	@PostMapping("/messages/{messageType}/{language}")
	public String updateMessagesForTypeAndLocale(HttpServletRequest request, @PathVariable String messageType,
			@PathVariable String language) {

		i18nService.updateMessageProperties(language, messageType, request.getParameter("key"),
				request.getParameter("value"));
		return getMessagesFromTypeAndLocale(request, messageType, language);
	}

	@GetMapping("/config/{configName}")
	public String getConfig(HttpServletRequest request, @PathVariable String configName, Model model) {

		model.addAttribute("configName", configName);
		model.addAttribute("configTemp", i18nService.getAppConfigsMap().get(configName));
		model.addAttribute("config", i18nService.getAppConfigsMap().get(configName));
		return "configAjax";
	}

	@PostMapping("/config/{configName}")
	public String updateConfig(HttpServletRequest request, @PathVariable String configName, Model model) {

		i18nService.updateConfigProperties(configName, request.getParameter("key"), request.getParameter("value"));
		return getConfig(request, configName, model);
	}

}
