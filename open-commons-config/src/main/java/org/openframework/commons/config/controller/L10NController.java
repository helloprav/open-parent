package org.openframework.commons.config.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openframework.commons.config.constants.AppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(AppConstants.CONFIG_API_L10N_PATH)
@Api(value = "L10N Controller", description = "REST APIs related to Student Entity!!!!")
public class L10NController {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private I18nService i18nService;

	@Autowired
	private LocaleResolver localeResolver;

	@GetMapping(path = "/languages")
	public @ResponseBody Set<LanguageBean> getLanguages() {

		logger.debug("Retrieve all available languages");
		Set<LanguageBean> languages = new HashSet<>();
		languages.addAll(i18nService.retrieveSupportedLanguages());
		return languages;
	}

	@PutMapping(path = "/switchLocale")
	public String switchLocale(final HttpSession session, String language) {

		logger.debug("Change locale in : [{}]", language);
		return language;
	}

	@GetMapping(value = "/messages", produces = { "application/json" })
	public @ResponseBody Map<String, String> getMessages(HttpServletRequest request) {
		return getMessagesByTypeAndLocale(request, AppConstants.MESSAGE_TYPE_DASHBOARD, null);
	}

	@GetMapping(path = "/messages/{messageType}")
	public @ResponseBody Map<String, String> getMessagesByTypeAndLocale(HttpServletRequest request,
			@PathVariable String messageType, String lang) {

		// get the below boolean value from config
		if(null == lang) {
			
			final Locale locale = localeResolver.resolveLocale(request);
			lang = locale.getLanguage();
		}
		return i18nService.getMessageProperties(lang, messageType, true);
	}

	@GetMapping(path = "/config")
	public @ResponseBody Map<String, Properties> getConfig() {

		return i18nService.getAppConfigsMap();
	}

	@GetMapping(path = "/config/{configName}")
	public @ResponseBody Properties getConfig(@PathVariable String configName) {

		return i18nService.getAppConfigsMap().get(configName);
	}

}
