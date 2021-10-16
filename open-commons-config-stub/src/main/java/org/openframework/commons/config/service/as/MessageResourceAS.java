/**
 * 
 */
package org.openframework.commons.config.service.as;

import java.util.List;

import java.util.Map;
import java.util.Properties;

import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;

/**
 * @author pmis30
 *
 */
public interface MessageResourceAS {

	public static final String DEFAULT_LOCALE = "en";

	public Map<String, MessageResourceLocale> getMessageResourceMap();

	List<LanguageBean> getSupportedLanguages();

	Map<String, Properties> getAppConfigsMap();

	public void updateMessageProperties(String language, String messageType, String key, String value);

	public void updateConfigProperties(String configName, String key, String value);

	public String getErrorMessageValue(String locale, String key);

	public String getDashboardMessageValue(String locale, String key);

	Properties getAppConfigsMap(String configName);

	Object getPropertyValue(String configName, String key);

	String getPropertyValueAsString(String configName, String key);

	int getPropertyValueAsInteger(String configName, String key);

}
