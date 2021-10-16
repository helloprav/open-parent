/**
 * 
 */
package org.openframework.commons.config.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.openframework.commons.config.constants.AppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.I18nService;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.springframework.stereotype.Service;


/**
 * @author pmis30
 *
 */
@Service
public class I18nServiceImpl implements I18nService {

	// @Value("${language.resources}")
	// private String prefixes;

	@Inject
	MessageResourceAS messageResourceAS;

	@Override
	public Map<String, MessageResourceLocale> getMessageResourceMap() {

		return messageResourceAS.getMessageResourceMap();
	}

	@Override
	public Map<String, Properties> getAppConfigsMap() {

		return messageResourceAS.getAppConfigsMap();
	}

	@Override
	public Properties getAppConfigsMap(String configName) {

		return messageResourceAS.getAppConfigsMap(configName);
	}

	/**
	 * Build the message (key, value) pair for the given language
	 * 
	 * @param language
	 * @param messageResource
	 * @param result
	 * @param prefixes
	 */
	protected void buildMessages(final String language, final Map<String, MessageResourceLocale> messageResource,
			final Map<String, String> result, String messageType, boolean inheritanceRequired) {

		if (messageResource.containsKey(messageType)) {

			// get the MessageResourceLocal for the given prefix
			final MessageResourceLocale messageResourceLocale = messageResource.get(messageType);
			// get the properties for the given language in the prefix
			Properties properties = messageResourceLocale.getPropertiesMap().get(language.toLowerCase());
			if (properties != null) {
				// iterate and keep the properties (key, value) in the result map
				for (Map.Entry<Object, Object> objectEntry : properties.entrySet()) {
					result.put(String.valueOf(objectEntry.getKey()), String.valueOf(objectEntry.getValue()));
				}
			}
			// get the properties which belongs to DEFAULT language
			if(inheritanceRequired) {
				properties = messageResourceLocale.getPropertiesMap().get(I18nService.DEFAULT);
				if (properties != null) {
					for (Map.Entry<Object, Object> objectEntry : properties.entrySet()) {
						final String key = String.valueOf(objectEntry.getKey());
						// if the key of DEFAULT properties not already found in result map
						if (!result.containsKey(key)) {
							result.put(key, String.valueOf(objectEntry.getValue()));
						}
					}
				}
			}
		}
	}

	@Override
	public Map<String, String> getMessageProperties(String language) {

		return getMessageProperties(language, AppConstants.MESSAGE_TYPE_DASHBOARD);
	}

	@Override
	public Map<String, String> getMessageProperties(String language, String messageType) {

		// TODO - get the inheritanceRequired property from config / properties file
		return getMessageProperties(language, messageType, false);
	}

	@Override
	public Map<String, String> getMessageProperties(String language, String messageType, boolean inheritanceRequired) {

		Map<String, MessageResourceLocale> messageResource = getMessageResourceMap();

		final Map<String, String> result = new LinkedHashMap<>();
		buildMessages(language, messageResource, result, messageType, inheritanceRequired);
		return result;
	}

	@Override
	public List<LanguageBean> retrieveSupportedLanguages() {

		return messageResourceAS.getSupportedLanguages();
	}

	@Override
	public void updateMessageProperties(String language, String messageType, String key, String value) {

		messageResourceAS.updateMessageProperties(language, messageType, key, value);
	}

	@Override
	public void updateConfigProperties(String configName, String key, String value) {

		messageResourceAS.updateConfigProperties(configName, key, value);
	}

}
