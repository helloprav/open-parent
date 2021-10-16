package org.openframework.commons.config.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;

public interface I18nService {

	String DEFAULT = "DEFAULT";

	Map<String, MessageResourceLocale> getMessageResourceMap();

	Map<String, String> getMessageProperties(String language);

	List<LanguageBean> retrieveSupportedLanguages();

	Map<String, String> getMessageProperties(String language, String messageType);

	Map<String, String> getMessageProperties(String language, String messageType, boolean inheritanceRequired);

	void updateMessageProperties(String language, String messageType, String key, String value);

	Map<String, Properties> getAppConfigsMap();

	Properties getAppConfigsMap(String configName);

	void updateConfigProperties(String configName, String key, String value);

}
