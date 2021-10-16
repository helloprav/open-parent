/**
 * 
 */
package org.openframework.commons.config.service.as.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.config.constants.AppConstants;
import org.openframework.commons.config.model.LanguageBean;
import org.openframework.commons.config.model.MessageResourceLocale;
import org.openframework.commons.config.service.I18nService;
import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.rest.Constants;
import org.openframework.commons.spring.utils.ClasspathUtils;
import org.openframework.commons.spring.utils.YamlUtils;
import org.openframework.commons.utils.FileFolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author its_me
 *
 */
@Service
public class MessageResourceASImpl implements MessageResourceAS {

	private static final Logger logger = LoggerFactory.getLogger(MessageResourceASImpl.class);

	private static final String PROPERTY_NOT_FOUND_IN = "Property not found in ";

	private static final String PROPERTY_FILE = " property file!";

	private static final int INVALID_VAL = -999;

	private Map<String, MessageResourceLocale> messageResourceMap;
	private List<LanguageBean> supportedLanguages = new ArrayList<>();
	private Map<String, Properties> appConfigsMap = new TreeMap<>();

	public MessageResourceASImpl() {
		System.out.println("MessageResourceASImpl() called");
	}

	/**
	 * Service Initialization
	 */
	@PostConstruct
	public void init() {
		String sharedPath = getSharedPath();
		logger.warn("sharedPath1 from environment variable: "+sharedPath);
		if(!FileFolderUtils.isDirectoryExists(sharedPath)) {
			logger.warn("sharedPath1 from environment variable does not exists.");
			sharedPath = null;
		}
		initMessage(sharedPath);
		initAppConfig(sharedPath);
		validateLanguageProperties();
	}

	private void initMessage(String sharedPath) {

		String messageDir = getMessageDir(sharedPath);
		logger.warn("messageDir: "+messageDir);
		initMessageResources(messageDir);
	}

	private String getMessageDir(String sharedPath) {

		return getResourcePath(sharedPath, AppConstants.APPLICATION_MESSAGE_DIR);
	}

	private String getConfigDir(String sharedPath) {

		return getResourcePath(sharedPath, AppConstants.APPLICATION_CONFIG_DIR);
	}

	private String getResourcePath(String sharedPath, String resourceDir) {

		String resourcePath = null;
		logger.warn("sharedPath2: "+sharedPath);
		if(null != sharedPath) {
			logger.warn("resourceDir: "+sharedPath.concat(File.separator).concat(resourceDir));
		}
		if (FileFolderUtils.isDirectoryExists(sharedPath) && FileFolderUtils.isDirectoryExists(
				sharedPath.concat(File.separator).concat(resourceDir))) {
			resourcePath = sharedPath.concat(File.separator).concat(resourceDir);
			logger.warn("resourcePath from environment variable: "+resourcePath);
		} else {

			if(AppConstants.APPLICATION_MESSAGE_DIR.equals(resourceDir)) {

				resourcePath = getMessageDirFromClasspath(resourceDir);
				logger.warn("resourcePath (message) from classpath: "+resourcePath);
			} else if(AppConstants.APPLICATION_CONFIG_DIR.equals(resourceDir)) {

				resourcePath = getConfigDirFromClasspath(resourceDir);
				logger.warn("resourcePath (config) from classpath: "+resourcePath);
			}
		}
		if(null == resourcePath) {
			throw new RuntimeException("message/config dir not found");
		}
		return resourcePath;
	}

	private String getMessageDirFromClasspath(String resourceDir) {

		String messageDirFromClasspath = System.getProperty(AppConstants.MESSAGE_DIR_KEY, resourceDir);
		logger.warn("messageDirFromClasspath: "+messageDirFromClasspath);
		if(null != messageDirFromClasspath) {
			messageDirFromClasspath = ClasspathUtils.getPathFromClasspathFolder(messageDirFromClasspath);
		}
		return messageDirFromClasspath;
	}

	private String getConfigDirFromClasspath(String resourceDir) {

		/* 
		 * get messageDirName from System Property supplied using -DmessageKey=xyz in jvm arguments
		 * if "messageKey" system property not found, then use the default value i.e. "message"
		 */
		String configDirFromClasspath = System.getProperty(AppConstants.CONFIG_DIR_KEY, resourceDir);
		logger.warn("configDirFromClasspath: "+configDirFromClasspath);
		if(null != configDirFromClasspath) {
			configDirFromClasspath = ClasspathUtils.getPathFromClasspathFolder(configDirFromClasspath);
		}
		return configDirFromClasspath;
	}

	private void initAppConfig(String sharedPath) {

		String configDir = getConfigDir(sharedPath);
		File[] globalPropertyFileList = FileFolderUtils.getMessagePropertyFileList(configDir);

		for (File file : globalPropertyFileList) {
			final String fileName = file.getName();
			if(fileName.endsWith(AppConstants.FILE_EXTENSION_PROPERTIES)) {
				appConfigsMap.put(FilenameUtils.removeExtension(fileName).toLowerCase(), FileFolderUtils.loadPropFromFile(file));
			} else if(fileName.endsWith(AppConstants.FILE_EXTENSION_YML)) {
				appConfigsMap.put(FilenameUtils.removeExtension(fileName).toLowerCase(), YamlUtils.loadYamlFromFile(file));
			}
		}
	}

	private String getSharedPath() {

		return System.getProperty(AppConstants.SHARED_PATH);
	}

	private void initMessageResources(String messageDir) {

		logger.warn("messageDir::: "+messageDir);
		Map<String, MessageResourceLocale> messageResourceMapTemp = new HashMap<>();
		List<LanguageBean> newSupportedLanguages = new ArrayList<>();
		final File[] messageSubDirList = FileFolderUtils.getMessagePropertyFileList(messageDir);

		for (File file : messageSubDirList) {
			if (file.isDirectory()) {
				MessageResourceLocale messageResourceLocale = new MessageResourceLocale();
				String[] extensions = new String[] { "properties", "yaml" };
				//Collection<File> listFiles = FileUtils.listFiles(file, new RegexFileFilter("^.+\\.properties"), DirectoryFileFilter.DIRECTORY);
				Collection<File> listFiles = FileUtils.listFiles(file, extensions, false);
				for (File innerFile : listFiles) {
					Properties props = FileFolderUtils.loadPropFromFile(innerFile);
					String localeCode = getLocale(innerFile.getName())[1];
					messageResourceLocale.getPropertiesMap().put(localeCode, props);
					if (!I18nService.DEFAULT.equals(localeCode)
							&& AppConstants.MESSAGE_TYPE_DASHBOARD.equalsIgnoreCase(file.getName())) {

						// add above supported language/localeCode in a list
						String languageName = props.getProperty("languageName");
						String nameAsImgProperty = props.getProperty("languageImg");
						Boolean nameAsImg = false;
						if (nameAsImgProperty != null && !nameAsImgProperty.equalsIgnoreCase("no")) {
							nameAsImg = true;
						}
						Locale languageLocale = new Locale(localeCode);
						LanguageBean toAdd = new LanguageBean(languageName, languageLocale.getLanguage(), nameAsImg);
						newSupportedLanguages.add(toAdd);
					}
				}
				if (AppConstants.MESSAGE_TYPE_DASHBOARD.equalsIgnoreCase(file.getName())) {
					supportedLanguages.addAll(newSupportedLanguages);
				}
				messageResourceMapTemp.put(file.getName(), messageResourceLocale);
			}
		}
		messageResourceMap = messageResourceMapTemp;
		logger.info("message resources initialization completed: {}", messageResourceMap);
	}

	private void validateLanguageProperties() {

		List<String> languageList = getLocaleList();
		Iterator<Entry<String, MessageResourceLocale>> messageEntrySetIterator = getMessageResourceMap().entrySet()
				.iterator();
		while (messageEntrySetIterator.hasNext()) {
			Entry<String, MessageResourceLocale> messageEntry = messageEntrySetIterator.next();
			if (!AppConstants.MESSAGE_TYPE_DASHBOARD.equals(messageEntry.getKey())) {

				Map<String, Properties> propertiesMap = messageEntry.getValue().getPropertiesMap();
				Iterator<String> propertiesMapIterator = propertiesMap.keySet().iterator();
				while (propertiesMapIterator.hasNext()) {
					String propertiesMapLanguage = propertiesMapIterator.next();
					if (languageList.contains(propertiesMapLanguage)) {
						languageList.remove(propertiesMapLanguage);
					} else {
						logger.error("The Properties of type {} is NOT supported for language {}",
								messageEntry.getKey(), propertiesMapLanguage);
					}
				}
				logger.error("No properties of languages {} found for the type {}", languageList,
						messageEntry.getKey());
			}
		}
	}

	private List<String> getLocaleList() {

		List<String> localeList = new ArrayList<>();
		ListIterator<LanguageBean> iterator = supportedLanguages.listIterator();
		while (iterator.hasNext()) {
			localeList.add(iterator.next().getLocale().toLowerCase());
		}
		return localeList;
	}

	public boolean containsName(final List<LanguageBean> list, final String name) {
		return list.stream().anyMatch(o -> o.getLocale().equals(name));
	}

	@Override
	public List<LanguageBean> getSupportedLanguages() {
		return supportedLanguages;
	}

	@Override
	public Map<String, MessageResourceLocale> getMessageResourceMap() {
		return messageResourceMap;
	}

	public MessageResourceLocale getErrorMessageResourceMap() {
		return messageResourceMap.get(Constants.MESSAGE_TYPE_ERRORS);
	}

	public String getProperty(String messageType, String locale, String key) {
		String value = null;
		Properties localeProperties = messageResourceMap.get(messageType).getPropertiesMap().get(locale.toLowerCase());
		if(null == localeProperties) {
			logger.error("Invalid locale [{}] requested", locale);
			return null;
		}
		value = localeProperties.getProperty(key);
		if(null == value && !DEFAULT_LOCALE.equals(locale)) {
			Properties defaultProperties = messageResourceMap.get(messageType).getPropertiesMap().get(DEFAULT_LOCALE);
			value = defaultProperties.getProperty(key);
		}
		return value;
	}

	@Override
	public String getErrorMessageValue(String locale, String key) {

		return getProperty(Constants.MESSAGE_TYPE_ERRORS, locale, key);
	}

	@Override
	public String getDashboardMessageValue(String locale, String key) {

		return getProperty(Constants.MESSAGE_TYPE_DASHBOARD, locale, key);
	}

	/**
	 * This method return an array that contains in position 0 base filename, in
	 * position 1 locale code. For example getLocale(messages_IT.properties) return
	 * {messages,IT}
	 * 
	 * @param fileName
	 * @return
	 * @throws I18nServiceException
	 */
	public String[] getLocale(String fileName) {
		try {
			logger.trace("Start getLocale");
			if (fileName == null || fileName.trim().isEmpty())
				throw new IllegalArgumentException("wrong input parameter: " + fileName);
			String[] split = fileName.split(".properties");
			int fromIndex = split[0].indexOf("_");
			if (fromIndex == -1) {
				logger.error("Unable to get Locale for file: {}", fileName);
				//return new String[] { split[0], "DEFAULT" };
			}
			return new String[] { split[0].substring(0, fromIndex), (split[0].substring(++fromIndex).toLowerCase()) };
		} catch (Exception e) {
			logger.error("Unable to getLocale");
			throw new IllegalArgumentException("Unable to getLocale", e);
		} finally {
			logger.trace("End getLocale");
		}
	}

	@Override
	public Map<String, Properties> getAppConfigsMap() {
		return appConfigsMap;
	}

	@Override
	public Properties getAppConfigsMap(String configName) {
		if(appConfigsMap.containsKey(configName)) {
			return appConfigsMap.get(configName);
		} else {
			return new Properties();
		}
	}

	@Override
	public void updateMessageProperties(String language, String messageType, String key, String value) {

		if (messageResourceMap.get(messageType).getPropertiesMap().containsKey(language.toLowerCase())) {
			messageResourceMap.get(messageType).getPropertiesMap().get(language.toLowerCase()).put(key, value);
		} else {
			logger.error("Message Properties NOT found for messageType: {} and language {}", messageType, language);
		}
	}

	@Override
	public void updateConfigProperties(String configName, String key, String value) {

		appConfigsMap.get(configName).put(key, value);
	}

    /**
     * This method will return the object value for the given brand and key.
     * 
     * @param configName
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value Returns property value as Object.
     */
    @Override
    public Object getPropertyValue(String configName, String key) {
        Object value = null;
        if (StringUtils.isEmpty(configName) || StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Config name and key is required!");
        } else {
            final Properties props = appConfigsMap.get(configName.toLowerCase());

            if (props != null && props.containsKey(key)) {
            	// TODO update below to support XssSani
                //value = XssSanitizerUtil.stripXSS(props.get(key).toString());
            	value = props.get(key).toString();
            }
            if (value instanceof List && props.containsKey(key)) {
            	// TODO update below to support XssSani
                //value = XssSanitizerUtil.stripXSS(props.getProperty(key));
            	value = props.get(key).toString();
            }
        }
        return value;
    }

    /**
     * This method will return the object value for the given brand and key.
     * 
     * @param configName
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value Returns property value as String.
     */
    @Override
    public String getPropertyValueAsString(String configName, String key) {
        Object value = this.getPropertyValue(configName, key);
        if (value == null && !AppConstants.GLOBAL_CONFIG.equals(configName)) {
            value = this.getPropertyValue(AppConstants.GLOBAL_CONFIG, key);
        }
        if (value == null) {
            logger.info(key + PROPERTY_NOT_FOUND_IN + configName + " property file!");
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * This method will return the integer value for the given brand and key.
     * 
     * @param configName
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value the property value as integer.
     */
    @Override
    public int getPropertyValueAsInteger(String configName, String key) {
        Object value = this.getPropertyValue(configName, key);
        if (value == null && !AppConstants.GLOBAL_CONFIG.equals(configName)) {
            value = this.getPropertyValue(AppConstants.GLOBAL_CONFIG, key);
        }
        if (value == null) {
            logger.info(key + PROPERTY_NOT_FOUND_IN + configName + PROPERTY_FILE);
            return INVALID_VAL;
        } else {

            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                throw new NumberFormatException(key + " is not an integer value in " + configName + PROPERTY_FILE);
            }

        }
    }

}
